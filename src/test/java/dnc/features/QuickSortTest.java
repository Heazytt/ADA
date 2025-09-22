package dnc.features;

import dnc.features.QuickSort;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class QuickSortTest {

    @Test
    void sortsVariousPatterns() {
        Random rnd = new Random(2);
        for (int t = 0; t < 30; t++) {
            int n = rnd.nextInt(1000) + 100;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
            int[] b = a.clone();
            java.util.Arrays.sort(b);


            QuickSort.Metrics m = new QuickSort.Metrics();
            QuickSort.sort(a, 123, m);

            assertArrayEquals(b, a);
            int bound = (int) (2 * (Math.log(n) / Math.log(2))) + 100;
            assertTrue(m.depthMax <= bound, "depth " + m.depthMax + " vs bound " + bound);
        }
    }
}

