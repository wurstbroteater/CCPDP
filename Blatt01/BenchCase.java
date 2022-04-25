package Blatt01;

/**
 * BenchCase represents the a single execution of a benchmarked function.
 *
 * <p>
 * <ul>
 * Notes for child class implementations:
 * <li>Preparations that shouldn't be time measured should be put into the
 * constructor.
 * <li>Cleanup actions that shouldn't be time measured should be put into a
 * finalize() function.
 * </ul>
 */

public interface BenchCase {
    /**
     * The method for which the execution time is measured.
     */
    abstract void run();
}