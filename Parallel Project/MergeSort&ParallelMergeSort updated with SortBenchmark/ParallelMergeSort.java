import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort implements sortAlgorithm {

    private static final int THRESHOLD = 2000; // segments smaller than this use normal MergeSort for more efficent
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
                MergeSort sequentialSorter = new MergeSort();
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
            MergeSort sequentialSorter = new MergeSort();
            sequentialSorter.merge(array, left, mid, right);
        }
    }
}

