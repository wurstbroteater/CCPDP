package Blatt02;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Process<T> extends Thread{
    int[] n;
    int[] flag;
    private final int customId;
    private final int maxInc = 0;
    
    public Process(int[] n,int[] flag, int id) {
        this.n = n;
        this.flag = flag;
        this.customId = id;
    }

    //B-20: should we use Thread.State?
/*
    @Override
    public void run() {
        if(customId % 2 == 0) {
            int k1 = 1;
            n[0] = k1; 
        } else {
            int k2 = 2;
            n[0] = k2;
        }
    }
*/

    public void run() {
        if(customId % 2 == 0) {
           while(flag[0] == 0) {
               n[0] = n[0] - 1;
               if(n[0] <= -100000) {
                   break;
               }
           }
        } else {
            flag[0] = 1;
            System.out.println("set it");
        }
    }

    @Override
    public String toString() {
        return "Thread " + customId;
    }

    public int getCustomId() {
        return customId;
    }

    public static void sleepFor(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex){
            System.err.println("got interrupted!");
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> occurs = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            int[] n = {0};
            int[] flag = {0};
            //Process<Integer> p1 = new Process<>(n,flag,1);
            Process<Integer> p2 = new Process<>(n,flag,2);
            //p1.start();
            p2.start();
            sleepFor(1000);
            occurs.add(n[0]);
            System.out.println(flag[0]);
        }
        System.out.println(occurs.stream().collect(Collectors.groupingBy(e -> e.toString(),Collectors.counting())));
        //B-20: Many runs necessary to produce eg: {0=7, 1=992, 2=1}
    }


}
