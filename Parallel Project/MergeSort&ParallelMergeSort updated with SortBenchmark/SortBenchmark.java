import java.util.Arrays;
import java.util.Random;

public class SortBenchmark {
    
    // Generate array with random numbers
    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random(123);
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
    
    // Measure how long it takes to sort an array
    public static long measureTime(sortAlgorithm sorter, int[] array) {
        int[] copy = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i];
        }
        
        long start = System.nanoTime();
        sorter.sort(copy);
        long end = System.nanoTime();
        
        return end - start; //return the total execution time for any of sorting algorithms
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
    
    
    // Convert nanoseconds to milliseconds
    public static double toMilliseconds(long nanoseconds) {
        return nanoseconds / 1_000_000.0;
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
