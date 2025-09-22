package dnc.features;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair2D {

    // ==== Метрики ====
    public static class Metrics {
        public long comparisons = 0;
        public long allocations = 0;
    }

    public static class Point {
        public final double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    public static double closest(Point[] pts, Metrics m) {
        if (pts.length < 2) return Double.POSITIVE_INFINITY;
        Point[] byX = Arrays.copyOf(pts, pts.length);
        Arrays.sort(byX, Comparator.comparingDouble(p -> p.x));
        m.allocations += pts.length;
        Point[] aux = new Point[pts.length];
        m.allocations += pts.length;
        return rec(byX, aux, 0, pts.length - 1, m);
    }

    private static double rec(Point[] byX, Point[] aux, int lo, int hi, Metrics m) {
        int n = hi - lo + 1;
        if (n <= 3) {
            double d = brute(byX, lo, hi, m);
            Arrays.sort(byX, lo, hi + 1, Comparator.comparingDouble(p -> p.y));
            return d;
        }
        int mid = (lo + hi) >>> 1;
        double midX = byX[mid].x;
        double dL = rec(byX, aux, lo, mid, m);
        double dR = rec(byX, aux, mid + 1, hi, m);
        double d = Math.min(dL, dR);

        mergeByY(byX, aux, lo, mid, hi);
        System.arraycopy(aux, lo, byX, lo, hi - lo + 1);

        int sz = 0;
        for (int i = lo; i <= hi; i++) {
            if (Math.abs(byX[i].x - midX) < d) {
                aux[lo + (sz++)] = byX[i];
            }
        }

        for (int i = lo; i < lo + sz; i++) {
            for (int j = i + 1; j < Math.min(i + 8, lo + sz); j++) {
                m.comparisons++;
                double dist = dist(aux[i], aux[j]);
                if (dist < d) d = dist;
            }
        }
        return d;
    }

    private static double brute(Point[] a, int lo, int hi, Metrics m) {
        double d = Double.POSITIVE_INFINITY;
        for (int i = lo; i <= hi; i++)
            for (int j = i + 1; j <= hi; j++) {
                m.comparisons++;
                d = Math.min(d, dist(a[i], a[j]));
            }
        return d;
    }

    private static double dist(Point p, Point q) {
        double dx = p.x - q.x, dy = p.y - q.y;
        return Math.hypot(dx, dy);
    }

    private static void mergeByY(Point[] a, Point[] aux, int lo, int mid, int hi) {
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (a[i].y <= a[j].y) aux[k++] = a[i++];
            else aux[k++] = a[j++];
        }
        while (i <= mid) aux[k++] = a[i++];
        while (j <= hi) aux[k++] = a[j++];
    }
}
