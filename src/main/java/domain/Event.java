package domain;

import java.io.Serializable;

public class Event implements Serializable {

    private int clock;
    private String message;

    public Event(int clock, String message) {
        this.clock = clock;
        this.message = message;
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
}
