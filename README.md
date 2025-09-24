# ⚡ Divide & Conquer Algorithms

This repository contains **classic Divide & Conquer algorithms** in Java.  
Each algorithm is implemented in a **single self-contained file**, with:
- 📜 CLI
- 🧪 JUnit 5 tests
- 📊 Metrics tracking
- ⚙️ GitHub Actions CI

---

## 1. Algorithms


**Merge Sort** works by splitting the array into two halves, sorting each half, and then merging them 
back into one sorted array. 
In our code, if the subarray is small (≤ 32 elements), we switch to Insertion Sort, because it is faster 
for small data. 
```
private static void sortRec(int[] a, int lo, int hi, int[] buf, Metrics m) {
        if (lo >= hi) return;
        if (hi - lo + 1 <= CUTOFF) { //Where CUTOFF = 32
            insertionSort(a, lo, hi, m); 
            return;
        }
```

**The merging part copies the two halves into a buffer and then compares elements one by one:**
```
while (i <= mid && j <= hi) {
    if (cmp(a[i], a[j], m) <= 0) buf[k++] = a[i++];
    else buf[k++] = a[j++];
}

```

###  🧩  MergeSort
- Self-contained: `Metrics`, `InsertionSort` helper, and `MergeSort` logic.
- Features:
    - Single reusable buffer for merging.
    - Insertion sort for small arrays (cutoff).
    - Always stable.
- Complexity: O(n log n) time, O(n) memory.

### ⚡ QuickSort

**Quick Sort** is faster in practice because it does not need extra memory like Merge Sort. 
The main idea is: 
- 1. Pick a pivot element. 
- 2. Put smaller elements to the left, bigger to the right. 
- 3. Sort left and right parts recursively. 

**In our implementation, we use a random pivot:**
```
int p = lo + rnd.nextInt(hi - lo + 1);
swap(a, p, hi, m);
int pivot = a[hi];
```

**Partitioning moves all numbers smaller than pivot to the left:**
```
for (int j = lo; j < hi; j++) {
    m.comparisons++;
    if (a[j] <= pivot) {
        swap(a, i, j, m);
        i++;
    }
}
```
**We also used <font color="green" >tail recursion</font> optimization: instead of always calling recursion twice, we call it for 
the smaller half and continue the loop for the bigger half. This reduces recursion depth.**

- Self-contained: `Metrics`, swap utility, partitioning logic, `QuickSort`.
- Features:
    - Randomized pivot.
    - Always recurse into the smaller partition to limit recursion depth.
    - Tracks comparisons, swaps, and recursion depth.
- Complexity: O(n log n) average, O(n^2) worst-case(but with random pivot it’s rare.).

### 🎯SelectMoM5 (Deterministic Select)
*This algorithm finds the k-th smallest element in an array.*

**The code splits the array into groups of 5, sorts them, and picks the median:**
```
int start = lo + g * 5;
int end = Math.min(start + 4, hi);
insertionSort(a, start, end, m);
int medianIdx = start + (end - start) / 2;
swap(a, lo + g, medianIdx, m);
```
**Then we recursively call medianOfMedians on the medians array**
```
int mid = lo + (groups - 1) / 2;
return selectRec(a, lo, lo + groups - 1, mid, m);
```
And what about **Partitioning step:**
```
for (int j = lo; j < hi; j++) {
    if (cmp(a[j], a[hi], m) <= 0) {
        swap(a, i, j, m);
        i++;
    }
}
```
- Self-contained: `Metrics`, insertion sort, partitionByValue, select logic.
- Features:
    - Groups of 5 elements, median-of-medians for pivot.
    - Worst-case linear time selection of k-th element.
    - More complicated than QuickSelect, but safer.
    - Finds the exact k-th element.
- Complexity: O(n) worst-case.

### 📐 ClosestPair2D
**This is a geometry problem: given many points, find two that are closest. 
The brute force solution checks all pairs and works in O(n²).**
**Code for recursive part:**
```
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
```
**And the strip check:**
```
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
```
- Self-contained: `Metrics`, `Point`, helpers for sorting and merging, recursive divide & conquer.
- Features:
    - Much faster than brute force.
    - Divide points, recursively solve left and right.
    - A good example of divide and conquer outside sorting. 
- Complexity: O(n log n).

---

## 🔹 2. Project Structure

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

## 7. Metrics / Why

- Compare algorithms beyond time:
    - `comparisons` / `swaps`: work count independent of CPU
    - `depth_max`: recursion behavior
    - `allocs`: memory pressure
- Helps generate plots: time vs n, depth vs n, etc.

---


## 9. License & Notes

- Educational project for learning divide & conquer and Java
- Each algorithm file is standalone for single-file submission
- Java 17+, Maven, JUnit 5

---

Everything is ready for compilation, testing, running, CSV generation, and CI.

