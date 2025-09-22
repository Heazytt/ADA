# Divide & Conquer Algorithms

This repository contains implementations of classic divide & conquer algorithms in Java, all in **single files** with self-contained helpers. It includes a CLI, JUnit 5 tests, metrics tracking, and GitHub Actions CI.

---

## 1. Algorithms

### MergeSort
- Self-contained: `Metrics`, `InsertionSort` helper, and `MergeSort` logic.
- Features:
    - Single reusable buffer for merging.
    - Insertion sort for small arrays (cutoff).
    - Tracks comparisons, swaps, allocations, and recursion depth.
- Complexity: O(n log n) time, O(n) memory.

### QuickSort
- Self-contained: `Metrics`, swap utility, partitioning logic, `QuickSort`.
- Features:
    - Randomized pivot.
    - Always recurse into the smaller partition to limit recursion depth.
    - Tracks comparisons, swaps, and recursion depth.
- Complexity: O(n log n) average, O(n^2) worst-case.

### SelectMoM5 (Deterministic Select)
- Self-contained: `Metrics`, insertion sort, partitionByValue, select logic.
- Features:
    - Groups of 5 elements, median-of-medians for pivot.
    - Worst-case linear time selection of k-th element.
- Complexity: O(n) worst-case.

### ClosestPair2D
- Self-contained: `Metrics`, `Point`, helpers for sorting and merging, recursive divide & conquer.
- Features:
    - Divide points, recursively solve left and right.
    - Merge by Y-coordinate and check the middle strip.
- Complexity: O(n log n).

---

## 2. Project Structure

```
algos-dnc/
├─ pom.xml
├─ README.md
├─ .gitignore
├─ .github/workflows/ci.yml       # CI: build + test
├─ results/                       # CSV output folder
└─ src/
   ├─ main/java/dnc/features/
   │  ├─ MergeSort.java
   │  ├─ QuickSort.java
   │  ├─ SelectMoM5.java
   │  └─ ClosestPair2D.java
   ├─ main/java/dnc/cli/Main.java # CLI: parse args, run algo, write CSV
   │
   │
   ├─test/java/dnc/features/
   │  ├─ MergeSortTest.java
   │  ├─ QuickSortTest.java
   │  ├─ SelectMoM5Test.java
   │  └─ ClosestPair2DTest.java
   │
   └─test/java/benchmarks/SelectBench
```

---

## 3. Building

### Compile and run tests
```bash
mvn clean test
```

### Package JAR (skip tests)
```bash
mvn -q -DskipTests package
```
Output: `target/algos-dnc-1.0.0.jar`

---

## 4. CLI Usage

General form:
```bash
java -cp target/algos-dnc-1.0.0.jar dnc.cli.Main \
  --algo <mergesort|quicksort|select|closest> \
  --n <size> \
  --trials <runs> \
  --seed <number> \
  [--k <index>] \
  --out results/<file>.csv
```

### Examples
```bash
# QuickSort on 100k items, 2 trials
java -cp target/algos-dnc-1.0.0.jar dnc.cli.Main --algo quicksort --n 100000 --trials 2 --seed 42 --out results/qs.csv

# MergeSort on 100k items
java -cp target/algos-dnc-1.0.0.jar dnc.cli.Main --algo mergesort --n 100000 --trials 2 --seed 42 --out results/ms.csv

# Deterministic Select, find 50000th element
java -cp target/algos-dnc-1.0.0.jar dnc.cli.Main --algo select --n 100000 --k 50000 --trials 1 --seed 42 --out results/select.csv

# Closest Pair, 2D points
java -cp target/algos-dnc-1.0.0.jar dnc.cli.Main --algo closest --n 10000 --trials 1 --seed 42 --out results/closest.csv
```

### CSV Columns
`algo,n,time_ns,depth_max,comparisons,swaps,allocs,seed,notes`
- `time_ns`: runtime in nanoseconds
- `depth_max`: max recursion depth
- `comparisons` / `swaps`: operation counts
- `allocs`: memory allocations

---

## 5. Tests

JUnit 5 tests are in `src/test/java/dnc/features/`:
- `MergeSortTest`, `QuickSortTest`, `SelectMoM5Test`, `ClosestPair2DTest`
- Validate correctness against Java `Arrays.sort` or brute force.

Run all tests:
```bash
mvn test
```

---

## 6. CI / GitHub Actions

`.github/workflows/ci.yml`:
- Runs on push / PR to `main`
- Steps:
    1. Checkout repository
    2. Setup JDK 21 (Temurin)
    3. `mvn package`
    4. `mvn test`

Ensures project always builds and tests pass.

---


---

## 8. Metrics / Why

- Compare algorithms beyond time:
    - `comparisons` / `swaps`: work count independent of CPU
    - `depth_max`: recursion behavior
    - `allocs`: memory pressure
- Helps generate plots: time vs n, depth vs n, etc.

---


## 10. License & Notes

- Educational project for learning divide & conquer and Java
- Each algorithm file is standalone for single-file submission
- Java 17+, Maven, JUnit 5

---

Everything is ready for compilation, testing, running, CSV generation, and CI.

