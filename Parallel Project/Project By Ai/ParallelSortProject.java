import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 * COMPREHENSIVE PARALLEL MERGE SORT PROJECT
 * * Includes:
 * 1. Sequential Merge Sort
 * 2. Parallel Merge Sort (Fork/Join)
 * 3. Benchmarking Suite
 * 4. BONUS: Generic Type Support (+2 points)
 * 5. BONUS: True Parallel GUI Visualization (+3 points)
 * - Now includes customization for Algorithm, Pattern, and Size.
 */
public class ParallelSortProject {

    // Threshold for Benchmark (High performance)
    private static final int BENCHMARK_THRESHOLD = 2048;

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Parallel Merge Sort Project & Bonus");
        System.out.println("============================================");
        System.out.println("Select Mode:");
        System.out.println("1. Run Benchmarks (Console)");
        System.out.println("2. Run GUI Visualization (Bonus)");
        System.out.print("Enter choice (1 or 2): ");

        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        try {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                choice = 1; // Default
            }
        } catch (Exception e) {
            choice = 1;
        }

        if (choice == 2) {
            // Run GUI on Event Dispatch Thread
            SwingUtilities.invokeLater(() -> new VisualizerFrame());
        } else {
            runBenchmarks();
        }
    }

    // ==========================================================
    // CORE INTERFACES & ALGORITHMS (OOP Design)
    // ==========================================================

    public interface SortAlgorithm {
        <T extends Comparable<? super T>> void sort(T[] array);
        String getName();
    }

    /**
     * Implementation 1: Sequential Merge Sort
     */
    public static class SequentialMergeSort implements SortAlgorithm {
        @Override
        public <T extends Comparable<? super T>> void sort(T[] array) {
            if (array == null || array.length <= 1) return;
            T[] temp = Arrays.copyOf(array, array.length);
            mergeSort(array, temp, 0, array.length - 1);
        }

        private <T extends Comparable<? super T>> void mergeSort(T[] array, T[] temp, int left, int right) {
            if (left >= right) return;
            int mid = left + (right - left) / 2;
            mergeSort(array, temp, left, mid);
            mergeSort(array, temp, mid + 1, right);
            MergeUtils.merge(array, temp, left, mid, right);
        }

        @Override
        public String getName() {
            return "Sequential Merge Sort";
        }
    }

    /**
     * Implementation 2: Parallel Merge Sort using Fork/Join Framework
     */
    public static class ParallelMergeSort implements SortAlgorithm {
        private static final ForkJoinPool pool = ForkJoinPool.commonPool();

        @Override
        public <T extends Comparable<? super T>> void sort(T[] array) {
            if (array == null || array.length <= 1) return;
            T[] temp = Arrays.copyOf(array, array.length);
            SortTask<T> task = new SortTask<>(array, temp, 0, array.length - 1);
            pool.invoke(task);
        }

        @Override
        public String getName() {
            return "Parallel Merge Sort (Fork/Join)";
        }

        private static class SortTask<T extends Comparable<? super T>> extends RecursiveAction {
            private final T[] array;
            private final T[] temp;
            private final int left;
            private final int right;

            public SortTask(T[] array, T[] temp, int left, int right) {
                this.array = array;
                this.temp = temp;
                this.left = left;
                this.right = right;
            }

            @Override
            protected void compute() {
                if (left >= right) return;

                if (right - left < BENCHMARK_THRESHOLD) {
                    new SequentialMergeSort().mergeSort(array, temp, left, right);
                    return;
                }

                int mid = left + (right - left) / 2;
                SortTask<T> leftTask = new SortTask<>(array, temp, left, mid);
                SortTask<T> rightTask = new SortTask<>(array, temp, mid + 1, right);
                invokeAll(leftTask, rightTask);
                MergeUtils.merge(array, temp, left, mid, right);
            }
        }
    }

    public static class MergeUtils {
        public static <T extends Comparable<? super T>> void merge(T[] array, T[] temp, int left, int mid, int right) {
            for (int i = left; i <= right; i++) {
                temp[i] = array[i];
            }
            int i = left;
            int j = mid + 1;
            int k = left;
            while (i <= mid && j <= right) {
                if (temp[i].compareTo(temp[j]) <= 0) {
                    array[k] = temp[i];
                    i++;
                } else {
                    array[k] = temp[j];
                    j++;
                }
                k++;
            }
            while (i <= mid) {
                array[k] = temp[i];
                k++;
                i++;
            }
        }
    }

    // ==========================================================
    // BENCHMARKING LOGIC
    // ==========================================================

    public static void runBenchmarks() {
        int[] sizes = {10_000, 100_000, 1_000_000, 5_000_000};
        SortAlgorithm sequential = new SequentialMergeSort();
        SortAlgorithm parallel = new ParallelMergeSort();

        System.out.println("\nStarting Benchmarks...");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s %-15s\n", 
            "Size", "Pattern", "Sequential(ms)", "Parallel(ms)", "Java Sort(ms)", "Speedup");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int size : sizes) {
            testSize(size, "Random", sequential, parallel);
            testSize(size, "Reverse", sequential, parallel);
            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    private static void testSize(int size, String pattern, SortAlgorithm seq, SortAlgorithm par) {
        Integer[] baseArray = generateArray(size, pattern);

        // 1. Sequential
        Integer[] arr1 = baseArray.clone();
        long start = System.nanoTime();
        seq.sort(arr1);
        long seqTime = (System.nanoTime() - start) / 1_000_000;

        // 2. Parallel
        Integer[] arr2 = baseArray.clone();
        start = System.nanoTime();
        par.sort(arr2);
        long parTime = (System.nanoTime() - start) / 1_000_000;

        // 3. Built-in Arrays.sort
        Integer[] arr3 = baseArray.clone();
        start = System.nanoTime();
        Arrays.sort(arr3);
        long javaTime = (System.nanoTime() - start) / 1_000_000;

        double speedup = (double) seqTime / (parTime == 0 ? 1 : parTime);

        System.out.printf("%-10d %-15s %-15d %-15d %-15d %-15.2fx\n", 
            size, pattern, seqTime, parTime, javaTime, speedup);
    }

    private static Integer[] generateArray(int size, String pattern) {
        Random rand = new Random();
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10);
        }
        if (pattern.equals("Reverse")) {
            Arrays.sort(arr, Collections.reverseOrder());
        }
        return arr;
    }

    // ==========================================================
    // BONUS: GUI VISUALIZATION (ENHANCED)
    // ==========================================================

    static class VisualizerFrame extends JFrame {
        private SortPanel panel;
        private Integer[] data;
        
        // UI Controls
        private JSlider speedSlider;
        private JComboBox<String> algoSelector;
        private JComboBox<String> patternSelector;
        private JSpinner sizeSpinner;
        
        public VisualizerFrame() {
            setTitle("Bonus: Sort Visualization");
            setSize(1000, 700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // 1. Setup Controls
            JPanel controls = new JPanel();
            controls.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            controls.setBorder(BorderFactory.createTitledBorder("Settings"));

            // Algorithm Selector
            algoSelector = new JComboBox<>(new String[]{"Parallel Merge Sort", "Sequential Merge Sort", "Java Arrays.sort"});
            controls.add(new JLabel("Algorithm:"));
            controls.add(algoSelector);

            // Pattern Selector
            patternSelector = new JComboBox<>(new String[]{"Random", "Reverse Sorted", "Already Sorted"});
            controls.add(new JLabel("Input:"));
            controls.add(patternSelector);

            // Size Spinner
            sizeSpinner = new JSpinner(new SpinnerNumberModel(150, 10, 1000, 10));
            controls.add(new JLabel("Size:"));
            controls.add(sizeSpinner);

            // Speed Slider
            speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 200, 40);
            speedSlider.setPreferredSize(new Dimension(150, 40));
            controls.add(new JLabel("Delay(ms):"));
            controls.add(speedSlider);

            // Action Buttons
            JButton startBtn = new JButton("Run Sort");
            startBtn.setBackground(new Color(100, 200, 100));
            JButton resetBtn = new JButton("Reset Array");

            startBtn.addActionListener(e -> startSort());
            resetBtn.addActionListener(e -> {
                resetData();
                panel.repaint();
            });

            controls.add(startBtn);
            controls.add(resetBtn);

            add(controls, BorderLayout.NORTH);

            // 2. Setup Visualization Panel
            data = new Integer[150]; // Default size
            resetData();

            panel = new SortPanel();
            add(panel, BorderLayout.CENTER);

            setVisible(true);
        }

        private void resetData() {
            int size = (Integer) sizeSpinner.getValue();
            data = new Integer[size];
            String pattern = (String) patternSelector.getSelectedItem();
            Random r = new Random();

            for (int i = 0; i < size; i++) {
                data[i] = r.nextInt(500) + 10;
            }

            if ("Reverse Sorted".equals(pattern)) {
                Arrays.sort(data, Collections.reverseOrder());
            } else if ("Already Sorted".equals(pattern)) {
                Arrays.sort(data);
            }
        }

        private void startSort() {
            String algo = (String) algoSelector.getSelectedItem();
            
            if ("Java Arrays.sort".equals(algo)) {
                // Instant sort, no animation
                long startTime = System.nanoTime();
                Arrays.sort(data);
                long endTime = System.nanoTime();
                
                panel.repaint();
                
                double seconds = (endTime - startTime) / 1_000_000_000.0;
                JOptionPane.showMessageDialog(this, String.format("Java Arrays.sort finished (Instant).\nTime: %.4f seconds", seconds));
                return;
            }

            boolean isParallel = "Parallel Merge Sort".equals(algo);

            new Thread(() -> {
                long startTime = System.nanoTime();
                
                ForkJoinPool visualPool = new ForkJoinPool(4);
                Integer[] temp = Arrays.copyOf(data, data.length);
                
                // Pass the 'isParallel' flag to the task
                VisualizerTask task = new VisualizerTask(
                    data, temp, 0, data.length - 1, 
                    panel, speedSlider, isParallel
                );
                
                visualPool.invoke(task);
                
                long endTime = System.nanoTime();
                double seconds = (endTime - startTime) / 1_000_000_000.0;
                
                JOptionPane.showMessageDialog(this, String.format("Sorting Complete!\nTime: %.2f seconds", seconds));
                visualPool.shutdown();
            }).start();
        }

        // Custom JPanel to draw the bars
        class SortPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (data == null) return;

                int width = Math.max(1, getWidth() / data.length);
                for (int i = 0; i < data.length; i++) {
                    float hue = (float) data[i] / 550f; 
                    g.setColor(Color.getHSBColor(hue, 1.0f, 0.8f));
                    g.fillRect(i * width, getHeight() - data[i], width, data[i]);
                }
            }
        }
    }

    /**
     * UNIFIED VISUALIZATION TASK
     * Handles both Sequential and Parallel visualization based on flag.
     */
    static class VisualizerTask extends RecursiveAction {
        private final Integer[] array;
        private final Integer[] temp;
        private final int left;
        private final int right;
        private final JPanel panel;
        private final JSlider speedSlider;
        private final boolean isParallel;
        
        // Threshold forced small to ensure visual recursion
        private static final int VISUAL_THRESHOLD = 15;

        public VisualizerTask(Integer[] array, Integer[] temp, int left, int right, 
                              JPanel panel, JSlider speedSlider, boolean isParallel) {
            this.array = array;
            this.temp = temp;
            this.left = left;
            this.right = right;
            this.panel = panel;
            this.speedSlider = speedSlider;
            this.isParallel = isParallel;
        }

        @Override
        protected void compute() {
            if (left >= right) return;

            // Base case for recursion (both parallel and sequential modes use this)
            if (right - left < VISUAL_THRESHOLD) {
                sequentialSort(left, right);
                return;
            }

            int mid = left + (right - left) / 2;
            
            VisualizerTask leftTask = new VisualizerTask(array, temp, left, mid, panel, speedSlider, isParallel);
            VisualizerTask rightTask = new VisualizerTask(array, temp, mid + 1, right, panel, speedSlider, isParallel);

            if (isParallel) {
                // Fork both tasks to separate threads
                invokeAll(leftTask, rightTask);
            } else {
                // Run sequentially on the same thread
                leftTask.compute();
                rightTask.compute();
            }

            // Merge is always visualized
            merge(left, mid, right);
        }

        private void sequentialSort(int l, int r) {
            if (l >= r) return;
            int mid = l + (r - l) / 2;
            sequentialSort(l, mid);
            sequentialSort(mid + 1, r);
            merge(l, mid, r);
        }

        private void merge(int left, int mid, int right) {
            for (int i = left; i <= right; i++) temp[i] = array[i];
            
            int i = left, j = mid + 1, k = left;
            while (i <= mid && j <= right) {
                if (temp[i].compareTo(temp[j]) <= 0) array[k++] = temp[i++];
                else array[k++] = temp[j++];
                updateUI();
            }
            while (i <= mid) {
                array[k++] = temp[i++];
                updateUI();
            }
        }

        private void updateUI() {
            try {
                SwingUtilities.invokeAndWait(() -> panel.repaint());
                int delay = speedSlider.getValue();
                Thread.sleep(delay); 
            } catch (Exception e) {
                // Ignore interruption
            }
        }
    }
}