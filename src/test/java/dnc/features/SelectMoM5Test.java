package dnc.features;

import dnc.features.SelectMoM5;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SelectMoM5Test {

    @Test
    void matchesSortedOrderStatistic() {
        Random rnd = new Random(3);
        for (int t = 0; t < 50; t++) {
            int n = rnd.nextInt(1000) + 1;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
            int k = rnd.nextInt(n);
            int[] b = a.clone();
            java.util.Arrays.sort(b);


            SelectMoM5.Metrics m = new SelectMoM5.Metrics();
            int got = SelectMoM5.select(a, k, m);

            assertEquals(b[k], got);
        }
    }
}
