/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package parrallel.merge.sort;

import java.util.concurrent.atomic.AtomicLong;


public class ComparisonCounter { // modified here - NEW FILE
    public static AtomicLong comparisons = new AtomicLong(0); // modified here - centralized counter
    private static ThreadLocal<Long> localComparisons = ThreadLocal.withInitial(() -> 0L);

    public static void resetComparisons() { // modified here
        comparisons.set(0);
        localComparisons.set(0L);
    }

    public static void increment() { // modified here
        localComparisons.set(localComparisons.get() + 1);
    }

    public static long getComparisons() { // modified here
        return comparisons.get();
    }
    
    public static void combineLocal() {
        long local = localComparisons.get();
        if (local > 0) {
            comparisons.addAndGet(local);
            localComparisons.set(0L);
        }
    }
}
