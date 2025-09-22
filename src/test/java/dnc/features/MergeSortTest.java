package dnc.features;

import dnc.features.MergeSort;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    @Test
    void sortsRandomArrays() {
        Random rnd = new Random(1);
        for (int t = 0; t < 50; t++) {
            int n = rnd.nextInt(500) + 1;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
            int[] b = a.clone();
            java.util.Arrays.sort(b);

            // ✅ теперь используем вложенный Metrics
            MergeSort.Metrics m = new MergeSort.Metrics();
            MergeSort.sort(a, m);

            assertArrayEquals(b, a);
        }
    }
}
