package Blatt01;

/**
 * StopCondition determines whether a Benchmark should stop or run another
 * BenchCase. It is recommended to implement this using lambdas.
 */
public interface StopCondition {
    boolean shouldStop(Benchmark b);
}