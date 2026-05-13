public class MergeSort implements sortAlgorithm {

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
            if (L[i] <= R[j]) array[k++] = L[i++];
            else array[k++] = R[j++];
        }

        while (i < n1) array[k++] = L[i++];
        while (j < n2) array[k++] = R[j++];
    }
}


