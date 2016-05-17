package com.example.gustaf.customviewapp;

import java.util.ArrayList;

/**
 * Created by Gustaf on 2016-05-17.
 */
public class GraphEvent {

    private long start;
    private long stop;
    private String title;
    private boolean stamp;

    public GraphEvent(long start, long stop, boolean stamp) {
        this.start = start;
        this.stop = stop;
        this.stamp = stamp;
    }

    public GraphEvent(long start, long stop, boolean stamp, String title) {
        this(start, stop, stamp);
        this.title = title;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getStop() {
        return stop;
    }

    public void setStop(long stop) {
        this.stop = stop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStamp() {
        return stamp;
    }

    public void setStamp(boolean stamp) {
        this.stamp = stamp;
    }
}
