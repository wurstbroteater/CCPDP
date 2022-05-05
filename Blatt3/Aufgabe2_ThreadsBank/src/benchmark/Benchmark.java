package benchmark;

import java.time.Duration;
import java.util.Optional;
import java.lang.System;

/**
 * Benchmark runs a Benchmark on construction time and then allows
 * retrieving the results.
 */
public final class Benchmark {
    public String description;
    private Duration totalTime;
    private Duration benchmarkTime;
    private Optional<Duration> minTime;
    private Optional<Duration> maxTime;
    private int totalRuns;
    private Optional<Double> stdDev;

    /**
     * Creates a new Benchmark and runs BenchCases until the StopCondition is
     * fulfilled.
     */
    public Benchmark(BenchCaseProducer producer, StopCondition stop, String description) {
        this.description = description;

        benchmarkTime = Duration.ZERO;
        totalTime = Duration.ZERO;
        minTime = Optional.empty();
        maxTime = Optional.empty();
        stdDev = Optional.empty();

        // Helps with calculating stdDev.
        double welfordHelper = 0;

        var benchmarkStart = System.nanoTime();

        double avgNanos = 0;

        while (!stop.shouldStop(this)) {
            totalRuns++;

            var benchCase = producer.create();

            var start = System.nanoTime();
            benchCase.run();
            var end = System.nanoTime();

            var timeNanos = end - start;

            if (maxTime.isEmpty() || timeNanos > maxTime.get().toNanos()) {
                maxTime = Optional.of(Duration.ofNanos(timeNanos));
            }

            if (minTime.isEmpty() || timeNanos < minTime.get().toNanos()) {
                minTime = Optional.of(Duration.ofNanos(timeNanos));
            }

            double previousAvg = avgNanos;

            benchmarkTime = Duration.ofNanos(benchmarkTime.toNanos() + timeNanos);

            avgNanos += timeNanos / totalRuns;

            // This helps with calculating the standard deviation.
            // see
            // https://stackoverflow.com/questions/895929/how-do-i-determine-the-standard-deviation-stddev-of-a-set-of-values
            welfordHelper += (timeNanos - avgNanos) * (timeNanos - previousAvg);

            var benchmarkEnd = System.nanoTime();
            totalTime = Duration.ofNanos(benchmarkEnd - benchmarkStart);
        }

        if (totalRuns > 1) {
            stdDev = Optional.of(Math.sqrt(welfordHelper / (totalRuns - 2)));
        }

    }

    /**
     * The total number of runs.
     */
    public int totalRuns() {
        return totalRuns;
    }

    /**
     * The total time spent on running the benchmarks, with nanosecond precision,
     * including time used for initialization and finalization.
     */
    public Duration totalTime() {
        return totalTime;
    }

    /**
     * The total time spent on running the benchmarks, with nanosecond precision,
     * without the time used for initialization and finalization.
     */
    public Duration benchmarkTime() {
        return benchmarkTime;
    }

    /**
     * The average time spent on running a single benchmark, with nanosecond
     * precision. The time used for initialization and finalization is not included.
     */
    public Optional<Duration> avgTime() {
        return switch (totalRuns) {
        case 0 -> Optional.empty();
        default -> Optional.of(Duration.ofNanos(benchmarkTime.toNanos() / totalRuns));
        };
    }

    /**
     * The minimum time spent on running a single benchmark, with nanosecond
     * precision. Return value is empty if no benchmarks have been run.
     */
    public Optional<Duration> minTime() {
        return minTime;
    }

    /**
     * The minimum time spent on running a single benchmark, with nanosecond
     * precision. Return value is empty if no benchmarks have been run.
     */
    public Optional<Duration> maxTime() {
        return maxTime;
    }

    /**
     * The standard deviation of the time spent on running single Benchcases. Return
     * value is empty if less than two benchmarks have been run.
     */
    public Optional<Double> stdDev() {
        return stdDev;
    }

    /**
     * Print the results of the Benchmark.
     */
    public void printResults() {
        System.out.println("Benchmark:          " + description);
        System.out.println("Total runs:         " + totalRuns());
        System.out.println("Total time:         " + totalTime());
        System.out.println("Benchmark time:     " + benchmarkTime());
        maxTime().ifPresent((t) -> System.out.println("Maximum time(ns):   " + t.toNanos()));
        minTime().ifPresent((t) -> System.out.println("Minimum time(ns):   " + t.toNanos()));
        avgTime().ifPresent((t) -> System.out.println("Average time (ns):  " + t.toNanos()));
        stdDev().ifPresent((d) -> System.out.println("Standard Deviation: " + d));
    }

}