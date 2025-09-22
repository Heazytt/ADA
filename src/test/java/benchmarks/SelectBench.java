import dnc.features.SelectMoM5;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class SelectBench {

    @Param({"1000", "10000"})
    public int n;

    int[] data;
    int k;

    @Setup(Level.Trial)
    public void setup() {
        Random rnd = new Random(42);
        data = new int[n];
        for (int i = 0; i < n; i++) data[i] = rnd.nextInt();
        k = n / 2;
    }

    @Benchmark
    public int medianOfMedians() {
        int[] a = data.clone();
        SelectMoM5.Metrics m = new SelectMoM5.Metrics();
        return SelectMoM5.select(a, k, m);
    }

    @Benchmark
    public int sortThenPick() {
        int[] a = data.clone();
        java.util.Arrays.sort(a);
        return a[k];
    }
}
