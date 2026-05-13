/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parrallel.merge.sort;

/**
 *
 * @author Basil
 */
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort implements sortAlgorithm {
    // ADDED PART FOR GUI
    
    public static ArrayVisualizer visualizer;
    public static int delay;
    public static boolean benchmarkMode; //ADDED BY SS
    private static int THRESHOLD = 2000; // segments smaller than this use normal MergeSort for more efficent


    public ParallelMergeSort(ArrayVisualizer v, int d) {
        visualizer = v;
        delay = d;
        THRESHOLD = 40;
        benchmarkMode = false; //ADDED BY SS
    }
    
    public ParallelMergeSort() {
        visualizer = null;
        delay = 0;
        benchmarkMode = true; //ADDED BY SS
    }
    // END OF ADDED PART

    
    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) 
            return;

        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new MergeSortTask(array, 0, array.length - 1));
        
        ComparisonCounter.combineLocal();
    }

    // class for Fork/Join
    private class MergeSortTask extends RecursiveAction {

        private final int[] array;
        private final int left;
        private final int right;
    
        MergeSortTask(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            // If segment is small use SEQUENTIAL MERGE
            if (right - left < THRESHOLD) {
                // Local sequential
                sequentialSort(array, left, right);
                ComparisonCounter.combineLocal();
                return;
            }

            int mid = (left + right) / 2;

            MergeSortTask leftTask = new MergeSortTask(array, left, mid);
            MergeSortTask rightTask = new MergeSortTask(array, mid + 1, right);

            // Run in parallel
            invokeAll(leftTask, rightTask);

            ParallelMergeSort.this.merge(array, left, mid, right);
            
            ComparisonCounter.combineLocal();
        }

        private void sequentialSort(int[] arr, int l, int r) {
            if (l < r) {
                int m = (l + r) / 2;
                sequentialSort(arr, l, m);
                sequentialSort(arr, m + 1, r);
                ParallelMergeSort.this.merge(arr, l, m, r); //  merge
            }
        }
    }

    // EDITED BE SS
    public void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            ComparisonCounter.increment();

            if (L[i] <= R[j]) {
                array[k++] = L[i++];
            } else {
                array[k++] = R[j++];
            }

            if (!benchmarkMode && visualizer != null) {
                updateVisualization();
            }
        }

        while (i < n1) {
            array[k++] = L[i++];
            if (!benchmarkMode && visualizer != null) {
                updateVisualization();
            }
        }
        
        while (j < n2) {
            array[k++] = R[j++];
            if (!benchmarkMode && visualizer != null) {
                updateVisualization();
            }
        }
    }
    
    // ADDED BY SS
    private void updateVisualization() {
        try {
            javax.swing.SwingUtilities.invokeAndWait(() -> visualizer.repaint());
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
