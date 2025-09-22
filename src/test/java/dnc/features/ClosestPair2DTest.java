package dnc.features;

import dnc.features.ClosestPair2D;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ClosestPair2DTest {


    private static double brute(ClosestPair2D.Point[] pts) {
        double d = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x - pts[j].x;
                double dy = pts[i].y - pts[j].y;
                double dist = Math.hypot(dx, dy);
                d = Math.min(d, dist);
            }
        }
        return d;
    }

    @Test
    void matchesBruteForceOnSmallN() {
        Random rnd = new Random(4);
        for (int t = 0; t < 20; t++) {
            int n = rnd.nextInt(400) + 2;
            ClosestPair2D.Point[] pts = new ClosestPair2D.Point[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new ClosestPair2D.Point(rnd.nextDouble(), rnd.nextDouble());
            }


            ClosestPair2D.Metrics m = new ClosestPair2D.Metrics();
            double fast = ClosestPair2D.closest(pts, m);
            double slow = brute(pts);

            assertEquals(slow, fast, 1e-9);
        }
    }
}
