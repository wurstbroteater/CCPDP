package Blatt02;

public class Process<T> extends Thread{
    T k;
    private final int customId;
    private final int maxInc = 20;
    
    public Process(T val, int id) {
        this.k = val;
        this.customId = id;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        for(int i = 0; i < maxInc; i++) {
            System.out.println(this + " performs run " + i);
        }
    }

    @Override
    public String toString() {
        return "Thread " + customId;
    }
{}
    public int getCustomId() {
        return customId;
    }

    public static void main(String[] args) {
        Process<Integer> p1 = new Process<>(20,0);
        Process<Integer> p2 = new Process<>(20,1);
        p1.start();
        p2.start();
        
    }

}
