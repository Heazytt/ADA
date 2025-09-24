package dnc.features;

import java.util.Random;

public class QuickSort {

    
    public static class Metrics {
        public long comparisons = 0;
        public long swaps = 0;
        private int depthCurrent = 0;
        public int depthMax = 0;

        public void incDepth() {
            depthCurrent++;
            if (depthCurrent > depthMax) depthMax = depthCurrent;
        }
        public void decDepth() { depthCurrent--; }
    }

    
    private static void swap(int[] a, int i, int j, Metrics m) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
        m.swaps++;
    }

    
    private static int partitionRandom(int[] a, int lo, int hi, Random rnd, Metrics m) {
        int p = lo + rnd.nextInt(hi - lo + 1);
        swap(a, p, hi, m);
        int pivot = a[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            m.comparisons++;
            if (a[j] <= pivot) {
                swap(a, i, j, m);
                i++;
            }
        }
        swap(a, i, hi, m);
        return i;
    }

    
    public static void sort(int[] a, long seed, Metrics m) {
        Random rnd = new Random(seed);
        sortRange(a, 0, a.length - 1, rnd, m);
    }

    private static void sortRange(int[] a, int lo, int hi, Random rnd, Metrics m) {
        while (lo < hi) {
            m.incDepth();
            int p = partitionRandom(a, lo, hi, rnd, m);
            int left = p - lo;
            int right = hi - p;
            if (left < right) {
                sortRange(a, lo, p - 1, rnd, m);
                lo = p + 1;
            } else {
                sortRange(a, p + 1, hi, rnd, m);
                hi = p - 1;
            }
            m.decDepth();
        }
    }
}
