/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parrallel.merge.sort;

/**
 *
 * @author Basil
 */
import java.util.Arrays;
import java.util.Random;

public class SortBenchmark {
    
    //ADDED FOR COMPARISON
       // Result class to hold both time and comparisons
    public static class BenchmarkResult {
        public long time;
        public long comparisons;
        
        public BenchmarkResult(long time, long comparisons) {
            this.time = time;
            this.comparisons = comparisons;
        }
    }
    
    // Generate array with random numbers
    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();// REMOVED THE SEED
        for (int i = 0; i < size; i++) {
            
            array[i] = random.nextInt(size * 10);
        }
        return array;
    }
    
    // Generate array that is already sorted
    public static int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }
    
    // Generate array that is reverse sorted
    public static int[] generateReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }
    
    //ADDED FOR COMPARISON
    // Measure time and comparisons for sorting
    public static BenchmarkResult measureTimeAndComparisons(sortAlgorithm sorter, int[] array) {
        int[] copy = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i];
        }
        
        // Reset comparisons before sorting
        if (sorter instanceof MergeSort) {
            MergeSort.resetComparisons();
        } else if (sorter instanceof ParallelMergeSort) {
            ParallelMergeSort.resetComparisons();
        }
        
        long start = System.nanoTime();
        sorter.sort(copy);
        long end = System.nanoTime();
        
        // Get comparisons after sorting
        long comparisons = 0;
        if (sorter instanceof MergeSort) {
            comparisons = MergeSort.getComparisons();
        } else if (sorter instanceof ParallelMergeSort) {
            comparisons = ParallelMergeSort.getComparisons();
        }
        
        return new BenchmarkResult(end - start, comparisons);
    }
    
    //ADDED FOR COMPARISON
    // Measure how long it takes to sort an array
    public static long measureTime(sortAlgorithm sorter, int[] array) {
        return measureTimeAndComparisons(sorter, array).time;
    }
    
    // Run benchmark multiple times and get average time
    public static long runBenchmark(sortAlgorithm sorter, int[] array, int runs) {
        long totalTime = 0;
        
        for (int i = 0; i < runs; i++) {
            long time = measureTime(sorter, array);
            totalTime = totalTime + time;
        }
        
        return totalTime / runs;  //return the average execution time
    }
    
    //ADDED FOR COMPARISON
    // Run benchmark with comparisons tracking
    public static BenchmarkResult runBenchmarkWithComparisons(sortAlgorithm sorter, int[] array, int runs) {
     long totalTime = 0;
     long totalComparisons = 0;

     for (int i = 0; i < runs; i++) {
         BenchmarkResult result = measureTimeAndComparisons(sorter, array);
         totalTime += result.time;
         totalComparisons += result.comparisons;
     }

     return new BenchmarkResult(totalTime / runs, totalComparisons / runs);
    }
    
    // Convert nanoseconds to milliseconds
    public static double toMilliseconds(long nanoseconds) {
        return nanoseconds / 1_000_000.0;
    }
    
    //ADDED FOR COMPARISON
    // Format comparisons with commas for readability
    public static String formatComparisons(long comparisons) {
        return String.format("%,d", comparisons);
    }

    // Benchmark Java's built-in Arrays.sort
    public static long benchmarkBuiltinSort(int[] array, int runs) {
        long totalTime = 0;
        
        for (int i = 0; i < runs; i++) {
            int[] copy = new int[array.length];
            for (int j = 0; j < array.length; j++) {
                copy[j] = array[j];
            }
            
            long start = System.nanoTime();
            Arrays.sort(copy);
            long end = System.nanoTime();
            
            totalTime = totalTime + (end - start); //to calculate the execution time
        }
        
        return totalTime / runs; //return the average execution time 
    }

    //💀✔💀
}

