package algorithm;

public class Lamport {

    private int counter;
    private final String id;

    public Lamport(String id) {
        this.id = id;
        this.counter = 0;
    }

    public synchronized void updateClock(int clock, boolean local) {
        this.counter = (local) ? this.counter + clock : Math.max(clock, this.counter) + 1;
    }

    public synchronized int getCounter() { return counter; }

    public String getId() {
        return id;
    }
}
