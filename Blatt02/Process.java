package Blatt02;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Process<T> extends Thread {
    /**
     * A2: Amdahl’s law (AL)
     *          s_t
     * ------------------
     * (1-tp) + t0 + (tp/N) (= time to execte prog on N parallel processors)
     * s_t = time to execute prog on single processor
     * N = # CPUs
     * tp = portion of programm that can be executed in parallel
     * t0 = approx. communication overhead
     * 
     * for N = 4 and tp = 0,1s and t0 = 0 (no Overhead)
     *  -> AL = 
     * */
    int[] n;
    int[] k;
    boolean[] flag;
    private final int customId;
    private final int maxInc = 0;
    private static final String example = "B32"; // "B20", "B27", "B32"

    public Process(int[] n, int[] k, boolean[] flag, int id) {
        this.n = n;
        this.k = k;
        this.flag = flag;
        this.customId = id;
    }

    // B-20: should we use Thread.State?
    public void runB20() {
        if (customId % 2 == 0) {
            int k1 = 1;
            n[0] = k1;
        } else {
            int k2 = 2;
            n[0] = k2;
        }
    }

    // Fairness and Starvation
    public void runB27() {
        if (customId % 2 != 0) {
            // id 1 case
            while (!flag[0]) {
                n[0] = n[0] - 1;
            }
        } else {
            flag[0] = true;
        }
    }

    // Data Dependency
    public void runB32() {
        if (customId % 2 != 0) {
            // id 1 case
            int temp;
            temp = k[0];
            n[0] = temp + 1;

        } else {
            int temp;
            temp = n[0];
            k[0] = temp + 1;
        }
    }

    // Compiler Optimizations
    public void runB35() {
        if (customId % 2 != 0) {
            // id 1 case
            int local1;
            int local2;
            n[0] = n[0] + 2;
            local1 = (n[0] +5) * 7;
            local2 = n[0] + 5;
            n[0] = local1 * local2;
        } else {
            int local;
            local = n[0]+ 6;
        }
    }

    @Override
    public void run() {
        switch (example) {
            case "B20":
                runB20();
                break;
            case "B27":
                runB27();
                break;
            case "B32":
                runB32();
                break;
            case "B35":
                runB35();
                break;
            default:
                System.err.println("Unkown example! " + example);
                break;
        }
    }

    @Override
    public String toString() {
        return "Thread " + customId;
    }

    public int getCustomId() {
        return customId;
    }

    public static void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            System.err.println("got interrupted!");
        }
    }

    public static void main(String[] args) {
        ArrayList<Pair<Integer>> occurs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int[] n = { 0 };
            int[] k = { 1 };
            boolean[] flag = { false };
            Process<Integer> p1 = new Process<>(n, k, flag, 1);
            Process<Integer> p2 = new Process<>(n, k, flag, 2);
            p1.start();
            // Only for B-27 give: while loop some time to do sth
            if (example.equals("B27")) {
                sleepFor(100);
            }
            p2.start();
            occurs.add(new Pair<Integer>(n[0], k[0]));
            // System.out.println(n[0]);
        }
        System.out.println(occurs.stream().collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting())));
        // B-20: Many runs necessary to produce eg: {0=7, 1=992, 2=1}
        // B-35: {(2, 1)=74, (2, 3)=1, (0, 1)=25}
    }

}
