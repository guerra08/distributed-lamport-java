package algorithm;

public class Lamport {

    private int counter;
    private String id;

    public Lamport(String id) {
        this.id = id;
        this.counter = 0;
    }

    public void updateLocalMessage() {
        this.counter++;
    }

    public void updateClock(int clock) {
        int newClock = Math.max(clock, counter) + 1;
        setCounter(newClock);
    }

    public void updateClock() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
