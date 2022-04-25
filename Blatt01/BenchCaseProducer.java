package Blatt01;
/**
 * BenchCaseProducer creates new BenchCases during Benchmarks. It is recommended to implement this interface using lambas.
 */
interface BenchCaseProducer {
    BenchCase create();
}