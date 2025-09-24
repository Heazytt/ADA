package dnc.features;

public class SelectMoM5 {

    
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

    
    private static void insertionSort(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= lo && cmp(a[j], x, m) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = x;
        }
    }

    
    private static int partitionByValue(int[] a, int lo, int hi, int pivotValue, Metrics m) {
        int pivotIndex = -1;
        for (int k = lo; k <= hi; k++) {
            if (a[k] == pivotValue) { pivotIndex = k; break; }
        }
        if (pivotIndex == -1) pivotIndex = hi;
        swap(a, pivotIndex, hi, m);
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (cmp(a[j], a[hi], m) <= 0) {
                swap(a, i, j, m);
                i++;
            }
        }
        swap(a, i, hi, m);
        return i;
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
        m.swaps++;
    }

    
    public static int select(int[] a, int k, Metrics m) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return selectRec(a, 0, a.length - 1, k, m);
    }

    private static int selectRec(int[] a, int lo, int hi, int k, Metrics m) {
        while (true) {
            if (lo == hi) return a[lo];
            if (hi - lo + 1 <= 32) {
                insertionSort(a, lo, hi, m);
                return a[k];
            }
            m.incDepth();
            int pivot = medianOfMedians(a, lo, hi, m);
            int p = partitionByValue(a, lo, hi, pivot, m);
            if (k == p) {
                m.decDepth();
                return a[p];
            } else if (k < p) {
                hi = p - 1;
            } else {
                lo = p + 1;
            }
            m.decDepth();
        }
    }

    private static int medianOfMedians(int[] a, int lo, int hi, Metrics m) {
        int n = hi - lo + 1;
        int groups = (n + 4) / 5;
        for (int g = 0; g < groups; g++) {
            int start = lo + g * 5;
            int end = Math.min(start + 4, hi);
            insertionSort(a, start, end, m);
            int medianIdx = start + (end - start) / 2;
            swap(a, lo + g, medianIdx, m);
        }
        int mid = lo + (groups - 1) / 2;
        return selectRec(a, lo, lo + groups - 1, mid, m);
    }

    private static int cmp(int x, int y, Metrics m) {
        m.comparisons++;
        return Integer.compare(x, y);
    }
}
