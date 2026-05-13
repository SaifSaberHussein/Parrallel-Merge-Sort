
public class main {
    //test only
    public static void main(String[] args) {

        int[] arr = {10, 2, 9, 1, 3, 6, 45,13,456,775,757,575,7,67,65,65,456,7,4,6,756,7,765,856,8,8,58,7,56,6, 11};

        System.out.println("Original array:");
        printArray(arr);

        // Choose which sorting algorithm to use
        sortAlgorithm sorter = new ParallelMergeSort();   // Parallel version
        //sortAlgorithm sorter = new Project_parallel();  // not working for now 

        long start = System.currentTimeMillis();
        sorter.sort(arr);
        long end = System.currentTimeMillis();

        System.out.println("\nSorted array:");
        printArray(arr);

        System.out.println("\nExecution Time: " + (end - start) + " ms");
    }

    private static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
