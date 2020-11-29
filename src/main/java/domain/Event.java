package domain;

import java.io.Serializable;

public class Event implements Serializable {

    private int clock;
    private String message;
    private int creator;

    public Event(int clock, String message, int creator) {
        this.clock = clock;
        this.message = message;
        this.creator = creator;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCreator() { return creator; }

    public void setCreator(int creator) { this.creator = creator; }
}
