package Blatt01;
/**
 * Example Benchmark case that simply sleeps for one second.
 */
public class ExampleBenchCase implements BenchCase {

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
