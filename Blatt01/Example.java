package Blatt01;
import java.util.Random;
class Example {
    public static void main(String[] args) {
        /*
        BenchCaseProducer producer = () -> new ExampleBenchCase();
        StopCondition stop = (b) -> b.totalRuns() >= 10;
        var benchmark = new Benchmark(producer, stop, "Simply sleep for one second.");
        benchmark.printResults();
        */
        //performBenchmark(5,1000, 100000);
        //System.out.println("----------------------------------------------");
        //performBenchmark(5,1000, 200000);
        //System.out.println("----------------------------------------------");
        //performBenchmark(5,1000, 1000000);
        int size = 10000;
        int[] array = new int[size];
        Random rd = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rd.nextInt();
        }
        performBenchmark(5, 1000, array);

    }

    private static void performBenchmark(int timelimit, int maxRuns,int[] array) {
        BenchCaseProducer producer = () -> new AlgoBenchCase(array);
        StopCondition stop = (b) -> b.totalRuns() >= maxRuns;
        var benchmark = new Benchmark(producer, stop,timelimit, "Test: Sort Array of size " + array.length);
        benchmark.printResults();
        System.out.println("----------------------------------------------");
        System.out.println("Check Utils Method for comparision");

        producer = () -> new EvalBenchCase(array);
        benchmark = new Benchmark(producer, stop,timelimit, "Eval: Sort Array of size " + array.length);
        benchmark.printResults();
    }    

    private static void performBenchmark(int timelimit, int maxRuns,int arraySize) {
        //timelimit in seconds!
        BenchCaseProducer producer = () -> new AlgoBenchCase(arraySize);
        StopCondition stop = (b) -> b.totalRuns() >= maxRuns;
        var benchmark = new Benchmark(producer, stop,timelimit, "Sort Array of size " + arraySize);
        benchmark.printResults();
    }

}