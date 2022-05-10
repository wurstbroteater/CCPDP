package benchmark;

/**
 * BenchCaseProducer creates new BenchCases during Benchmarks. It is recommended to implement this interface using lambas.
 */
public interface BenchCaseProducer {
    BenchCase create();
}