package dnc.cli;

import dnc.features.MergeSort;
import dnc.features.QuickSort;
import dnc.features.SelectMoM5;
import dnc.features.ClosestPair2D;
import dnc.features.ClosestPair2D.Point;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        
        String algo = null;
        int n = 0, trials = 1, k = -1;
        long seed = System.nanoTime();
        String outFile = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--algo" -> algo = args[++i];
                case "--n" -> n = Integer.parseInt(args[++i]);
                case "--trials" -> trials = Integer.parseInt(args[++i]);
                case "--k" -> k = Integer.parseInt(args[++i]);
                case "--seed" -> seed = Long.parseLong(args[++i]);
                case "--out" -> outFile = args[++i];
                default -> {
                    System.err.println("Unknown arg: " + args[i]);
                    return;
                }
            }
        }

        if (algo == null || n <= 0) {
            System.err.println("Usage: --algo [quicksort|mergesort|select|closest] --n N [--trials T] [--k K] [--seed S] [--out file.csv]");
            return;
        }

        
        PrintWriter out = (outFile == null) ? new PrintWriter(System.out, true) : new PrintWriter(new FileWriter(outFile, true));
        out.println("algo,n,time_ns,depth_max,comparisons,swaps,allocs,seed,notes");

        Random rnd = new Random(seed);

        
        for (int t = 0; t < trials; t++) {
            long trialSeed = seed + t;
            int[] arr = rnd.ints(n, 0, 1_000_000).toArray();

            switch (algo) {
                case "mergesort" -> {
                    MergeSort.Metrics m = new MergeSort.Metrics();
                    long start = System.nanoTime();
                    MergeSort.sort(arr, m);
                    long end = System.nanoTime();
                    out.printf("mergesort,%d,%d,%d,%d,%d,%d,%d,%n",
                            n, (end - start), m.depthMax, m.comparisons, m.swaps, m.allocations, trialSeed);
                }

                case "quicksort" -> {
                    QuickSort.Metrics m = new QuickSort.Metrics();
                    long start = System.nanoTime();
                    QuickSort.sort(arr, trialSeed, m);
                    long end = System.nanoTime();
                    out.printf("quicksort,%d,%d,%d,%d,%d,0,%d,%n",
                            n, (end - start), m.depthMax, m.comparisons, m.swaps, trialSeed);
                }

                case "select" -> {
                    if (k < 0) k = n / 2;
                    SelectMoM5.Metrics m = new SelectMoM5.Metrics();
                    long start = System.nanoTime();
                    int kth = SelectMoM5.select(arr, k, m);
                    long end = System.nanoTime();
                    out.printf("select,%d,%d,%d,%d,%d,0,%d,k=%d,value=%d%n",
                            n, (end - start), m.depthMax, m.comparisons, m.swaps, trialSeed, k, kth);
                }

                case "closest" -> {
                    ClosestPair2D.Metrics m = new ClosestPair2D.Metrics();
                    Point[] pts = new Point[n];
                    for (int i = 0; i < n; i++) pts[i] = new Point(rnd.nextDouble(), rnd.nextDouble());
                    long start = System.nanoTime();
                    double d = ClosestPair2D.closest(pts, m);
                    long end = System.nanoTime();
                    out.printf("closest,%d,%d,0,%d,0,%d,%d,d=%.6f%n",
                            n, (end - start), m.comparisons, m.allocations, trialSeed, d);
                }

                default -> {
                    System.err.println("Unknown algorithm: " + algo);
                    return;
                }
            }
        }
        out.flush();
        if (outFile != null) out.close();
    }
}
