package Blatt01;

import java.util.Random;

public class EvalBenchCase implements BenchCase {
    private int[] array;

    public EvalBenchCase(int size) {
        array = new int[size];
        Random rd = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rd.nextInt();
        }
    }

    public EvalBenchCase(int[] array) {
        this.array = array;
    }

    @Override
    public void run() {
        // insertion-sort: O(n*n)
        for (int i = 1; i < array.length; i++) {
            int current = array[i];
            // Move element to the left until it's at the right position
            int j = i;
            while (j > 0 && current < array[j - 1]) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = current;
        }
    }
}
