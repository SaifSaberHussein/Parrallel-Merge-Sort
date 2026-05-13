
public class main extends SortBenchmark {
    //test only
    public static void main(String[] args) {
        System.out.println("=== SORTING ALGORITHM BENCHMARK ===\n");
        
        // Test sizes
        int[] sizes = {10_000, 100_000, 1_000_000};
        int runs = 5;
        
        // Create sorting algorithms
        sortAlgorithm sequential = new MergeSort();
        sortAlgorithm parallel = new ParallelMergeSort();
        
        
        // Run benchmarks for each size
        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            
            System.out.println("=================================================");
            System.out.println("Array Size: " + size);
            System.out.println("=================================================");
            
            // Test with RANDOM array
            System.out.println("\n--- RANDOM ARRAY ---");
            int[] randomArray = generateRandomArray(size);
            
            long seqTimeRandom = runBenchmark(sequential, randomArray, runs);
            System.out.println("Sequential MergeSort: " + toMilliseconds(seqTimeRandom) + " ms");
            
            long parTimeRandom = runBenchmark(parallel, randomArray, runs);
            System.out.println("Parallel MergeSort: " + toMilliseconds(parTimeRandom) + " ms");
            
            long builtinTimeRandom = benchmarkBuiltinSort(randomArray, runs);
            System.out.println("Arrays.sort: " + toMilliseconds(builtinTimeRandom) + " ms");
            
            
            // Test with REVERSE SORTED array
            System.out.println("\n--- REVERSE SORTED ARRAY ---");
            int[] reverseArray = generateReverseSortedArray(size);
            
            long seqTimeReverse = runBenchmark(sequential, reverseArray, runs);
            System.out.println("Sequential MergeSort: " + toMilliseconds(seqTimeReverse) + " ms");
            
            long parTimeReverse = runBenchmark(parallel, reverseArray, runs);
            System.out.println("Parallel MergeSort: " + toMilliseconds(parTimeReverse) + " ms");
            
            long builtinTimeReverse = benchmarkBuiltinSort(reverseArray, runs);
            System.out.println("Arrays.sort: " + toMilliseconds(builtinTimeReverse) + " ms");
            
            
            // Test with SORTED array
            System.out.println("\n--- SORTED ARRAY ---");
            int[] sortedArray = generateSortedArray(size);
            
            long seqTimeSorted = runBenchmark(sequential, sortedArray, runs);
            System.out.println("Sequential MergeSort: " + toMilliseconds(seqTimeSorted) + " ms");
            
            long parTimeSorted = runBenchmark(parallel, sortedArray, runs);
            System.out.println("Parallel MergeSort: " + toMilliseconds(parTimeSorted) + " ms");
            
            long builtinTimeSorted = benchmarkBuiltinSort(sortedArray, runs);
            System.out.println("Arrays.sort: " + toMilliseconds(builtinTimeSorted) + " ms");
            
            System.out.println();
        }
        
        System.out.println("=================================================");
        System.out.println("BENCHMARK COMPLETE");
        System.out.println("=================================================");
    }
}
