package com.example.markproject;

public class Habit {
    private String title;
    private boolean done;
    private String key;
    public Habit(String title, boolean done)
    {
        this.title = title;
        this.done = done;
    }

    public Habit()
    {

    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}