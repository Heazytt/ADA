package dnc.features;

public class MergeSort {


    public static class Metrics {
        public long comparisons = 0;
        public long swaps = 0;
        public long allocations = 0;
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


    private static final int CUTOFF = 32;

    public static void sort(int[] a, Metrics m) {
        int[] buf = new int[a.length];
        m.allocations += a.length;
        sortRec(a, 0, a.length - 1, buf, m);
    }

    private static void sortRec(int[] a, int lo, int hi, int[] buf, Metrics m) {
        if (lo >= hi) return;
        if (hi - lo + 1 <= CUTOFF) {
            insertionSort(a, lo, hi, m);
            return;
        }
        m.incDepth();
        int mid = (lo + hi) >>> 1;
        sortRec(a, lo, mid, buf, m);
        sortRec(a, mid + 1, hi, buf, m);
        merge(a, lo, mid, hi, buf, m);
        m.decDepth();
    }

    private static void merge(int[] a, int lo, int mid, int hi, int[] buf, Metrics m) {
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (cmp(a[i], a[j], m) <= 0) buf[k++] = a[i++];
            else buf[k++] = a[j++];
        }
        while (i <= mid) buf[k++] = a[i++];
        while (j <= hi) buf[k++] = a[j++];
        for (int t = lo; t <= hi; t++) a[t] = buf[t];
    }

    private static int cmp(int x, int y, Metrics m) {
        m.comparisons++;
        return Integer.compare(x, y);
    }
}
