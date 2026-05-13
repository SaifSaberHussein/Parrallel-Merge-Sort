/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parrallel.merge.sort;

/**
 *
 * @author Basil
 */
import java.util.concurrent.atomic.AtomicLong;// added for comparator
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort implements sortAlgorithm {
    // ADDED PART FOR GUI
    
    public static AtomicLong comparisons = new AtomicLong(0); // added for comparator
    public static ArrayVisualizer visualizer; // Static so all tasks see it
    public static int delay = 5;

    public ParallelMergeSort(ArrayVisualizer v, int d) {
        visualizer = v;
        delay = d;
        THRESHOLD = 40;
    }
    
    public ParallelMergeSort() {
        visualizer = null;
        delay = 0;
        THRESHOLD = 2000; // ADDED
    }
    // END OF ADDED PART

    // added for comparator
    // Method to reset comparisons counter
    public static void resetComparisons() {
        comparisons.set(0);
    }
    
    // added for comparator
    // Method to get current comparison count
    public static long getComparisons() {
        return comparisons.get();
    }
    
    
    private static int THRESHOLD = 2000; // segments smaller than this use normal MergeSort for more efficent
    // it is recomended to use the parallel sort on big arrays
    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) return;

        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new MergeSortTask(array, 0, array.length - 1));
    }

    // class for Fork/Join
    private class MergeSortTask extends RecursiveAction {

        private final int[] array;
        private final int left;
        private final int right;
        // تعبت يما خربانه
        MergeSortTask(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            // If segment is small use normal MergeSort
            if (right - left < THRESHOLD) {
                MergeSort sequentialSorter = new MergeSort(visualizer, delay);// <--- CHANGED FOR GUI
                sequentialSorter.mergeSort(array, left, right);
                return;
            }

            int mid = (left + right) / 2;

            // subtasks (divide) for left and right halves
            MergeSortTask leftTask = new MergeSortTask(array, left, mid);
            MergeSortTask rightTask = new MergeSortTask(array, mid + 1, right);

            // Run in parallel
            invokeAll(leftTask, rightTask);

            // Merge the divided halves from merge that is in the merge sort file
            MergeSort sequentialSorter = new MergeSort(visualizer, delay);// <--- CHANGED FOR GUI
            sequentialSorter.merge(array, left, mid, right);
        }
    }
}

