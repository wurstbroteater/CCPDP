public class CounterFun extends Thread {

    public int i = 0;
    public int j = 0;
    private boolean incI = true;

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            toggle();
            if (incI)
                incI();
            else
                incJ();
        }
    }

    public synchronized void toggle() {
        incI = !incI;
    }

    public synchronized void incI() {
        i += 1;
    }

    public synchronized void incJ() {
        j += 1;
    }

    public static void main(String[] args) {
        CounterFun t = new CounterFun();
        t.start();

        for (int i = 0; i < 10000; i++) {
            t.toggle();
            if (t.incI)
                t.incI();
            else
                t.incJ();
        }

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("i = " + t.i + "   j = " + t.j);
    }
}