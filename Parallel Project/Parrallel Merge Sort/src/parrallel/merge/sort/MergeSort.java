/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parrallel.merge.sort;
/**
 *
 * @author Basil
 */
import java.util.concurrent.atomic.AtomicLong; // <--- Import is for the comparator ADDED

public class MergeSort implements sortAlgorithm {
    
    public static AtomicLong comparisons = new AtomicLong(0);// ADDED FOR THE COMPARATOR
    
    // ADDED PART FOR GUI
    private ArrayVisualizer visualizer;
    private int delay = 5; // Default delay

    
    // Constructor to enable visualization
    public MergeSort(ArrayVisualizer v, int d) {
        this.visualizer = v;
        this.delay = d;
    }
    
    public MergeSort() {
        this.visualizer = null;
        this.delay = 0;
    }
    // END OF ADDED PART
   
    
    //ADDED FOR COMPARISON
        // Method to reset comparisons counter
    public static void resetComparisons() {
        comparisons.set(0);
    }
    
    // Method to get current comparison count
    public static long getComparisons() {
        return comparisons.get();
    }
    //END OF ADDED FOR COMPARISON

    
    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        mergeSort(array, 0, array.length - 1);
    }

    
    public  void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            // Sort left half
            mergeSort(array, left, mid);

            // Sort right half
            mergeSort(array, mid + 1, right);

            // Merge both halves
            merge(array, left, mid, right);
        }
    }
    
    // changed the merge here to a more efficent sort (omar amir)
    public  void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            
            comparisons.incrementAndGet(); // <--- ADDED FOR THE COMPARATOR
            ParallelMergeSort.comparisons.incrementAndGet(); // <--- ADDED FOR COMPARATOR
            
            if (L[i] <= R[j]) {
                array[k++] = L[i++];
            } else {
                array[k++] = R[j++];
            }
            step(); // <--- ADDDED FOR GUI
        }

        while (i < n1) {
            array[k++] = L[i++];
            step(); // <--- ADDDED FOR GUI
        }
        
        while (j < n2) {
            array[k++] = R[j++];
            step(); // <--- ADDDED FOR GUI
        }
    }
   
    
    // ADDED PART FOR GUI
    private void step() {
        if (visualizer == null) return;

        try {
            // 1. FORCE the GUI to repaint immediately (The Magic Fix)
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    visualizer.repaint();
                }
            });

            // 2. Sleep for the delay
            Thread.sleep(delay);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // END OF ADDED PART
}


