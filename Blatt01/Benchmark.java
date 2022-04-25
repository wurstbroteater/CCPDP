package Blatt01;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Benchmark runs a Benchmark on construction time and then allows
 * retrieving the results.
 */
public final class Benchmark {
    // might be useful later or remove
    private class Pair<T> {
        T left;
        T right;

        public Pair(T left, T right) {
            this.left = left;
            this.right = right;
        }

        public T getLeft() {
            return left;
        }

        public T getRight() {
            return right;
        }
    }

    public String description;
    public BenchCaseProducer producer;
    public StopCondition stop;
    private int runs = 0;
    private int forcedStopRuns = 0;
    private Optional<LocalDateTime> startTime;
    private List<Optional<Pair<LocalDateTime>>> benchmarkTimes;
    private List<Duration> benchmarkDurations;
    private Optional<LocalDateTime> endTime;
    private int runTimelimit; // in seconds
    private int benchmarkTimelimit = 10; // in seconds

    /**
     * Creates a new Benchmark and runs BenchCases until the StopCondition is
     * fulfilled.
     */
    public Benchmark(BenchCaseProducer producer, StopCondition stop, int runTimelimit, String description) {
        this.description = description;
        this.producer = producer;
        this.stop = stop;
        this.runTimelimit = runTimelimit;
        this.benchmarkTimes = new ArrayList<>();
        this.benchmarkDurations = new ArrayList<>();

        System.out.println("Initializing benchmark data...");
        this.startTime = Optional.of(LocalDateTime.now());
        BenchCase toTest = producer.create();
        performRuns(toTest);
        this.endTime = Optional.of(LocalDateTime.now());
    }

    private void performRuns(BenchCase toTest) {
        System.out.println("Starting runs...");

        // should be with lambda
        final ExecutorService service = Executors.newSingleThreadExecutor();
        while (!stop.shouldStop(this)) {
            LocalDateTime start = LocalDateTime.now();
            try {
                final Future<Object> f = service.submit(() -> {
                    toTest.run();
                    return true;
                });
                // check run timilimit: interrupt run(...) after runTimelimit secs
                f.get(runTimelimit, TimeUnit.SECONDS);
                // System.out.println(f.get(5, TimeUnit.SECONDS));
            } catch (final TimeoutException e) {
                System.err.println("Run " + runs + " took to long");
                forcedStopRuns++;
            } catch (final Exception e) {
                System.err.println("Run ended with error(s)!\n" + e);
                // throw new RuntimeException(e);
            } finally {
                // service.shutdown();
            }
            LocalDateTime end = LocalDateTime.now();
            this.benchmarkTimes.add(Optional.of(new Pair<>(start, end)));
            this.benchmarkDurations.add((Duration.between(start, end)));
            runs++;
            // check benchmark time limit
            if (Duration.between(startTime.get(), end).getSeconds() > benchmarkTimelimit) {
                System.err.println("Benchmark reached global time limit of " + benchmarkTimelimit + " sec");
                break;
            }
        }

        service.shutdown();

        if (runs != benchmarkTimes.size()) {
            System.err.println("#runs (" + runs + ") and size (" + benchmarkTimes.size() + ") differ!");
        }
        System.out.println("done!\n");
    }

    /**
     * The total number of runs.
     */
    public int totalRuns() {
        return runs;
    }

    /**
     * The total time spent on running the benchmarks, with nanosecond precision,
     * including time used for initialization and finalisation.
     */
    public Duration totalTime() {
        return Duration.between(startTime.get(), endTime.get());
    }

    /**
     * The total time spent on running the benchmarks, with nanosecond precision,
     * without the time used for initialization and finalization.
     */
    public Duration benchmarkTime() {
        return benchmarkDurations.stream().reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * The average time spent on running a single benchmark, with nanosecond
     * precision. The time used for initialization and finalization is not included.
     */
    public Optional<Duration> avgTime() {
        return Optional.of(benchmarkTime().dividedBy(runs));
    }

    /**
     * The minimum time spent on running a single benchmark, with nanosecond
     * precision. Return value is empty if no benchmarks have been run.
     */
    public Optional<Duration> minTime() {
        return benchmarkDurations.stream().min(Duration::compareTo);
    }

    /**
     * The minimum time spent on running a single benchmark, with nanosecond
     * precision. Return value is empty if no benchmarks have been run.
     */
    public Optional<Duration> maxTime() {
        return benchmarkDurations.stream().max(Duration::compareTo);
    }

    /**
     * The standart deviation of the time spent on running single Benchcases. Return
     * value is empty if less than two benchmarks have been run.
     */
    public Optional<Double> stdDev() {
        // formula: https://www.investopedia.com/terms/s/standarddeviation.asp
        if (benchmarkDurations.size() > 1) {
            int mean = avgTime().get().getNano();
            double numerator = benchmarkDurations.stream().map(d -> Math.pow(d.getNano() - mean, 2)).reduce(0.0,
                    Double::sum);
            return Optional.of(Math.sqrt(numerator / (benchmarkDurations.size() - 1)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Print the results of the Benchmark.
     */
    public void printResults() {
        System.out.println("Benchmark:          " + description);
        System.out.println("Total runs:         " + totalRuns());
        System.out.println("Reached time limit: " + forcedStopRuns);
        System.out.println("Total time:         " + totalTime());
        maxTime().ifPresent(
                (t) -> System.out.println("Maximum time(ns):   " + t.toNanos() + " time(s): " + t.toSeconds()));
        minTime().ifPresent((t) -> System.out.println("Minimum time(ns):   " + t.toNanos()));
        avgTime().ifPresent((t) -> System.out.println("Average time (ns):  " + t.toNanos()));
        stdDev().ifPresent((d) -> System.out.println("Standart Deviation: " + d));
    }

}