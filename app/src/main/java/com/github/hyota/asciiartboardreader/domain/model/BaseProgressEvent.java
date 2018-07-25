package com.github.hyota.asciiartboardreader.domain.model;

public class BaseProgressEvent {

    private int max;
    private int progress;

    public BaseProgressEvent(int max, int progress) {
        this.max = max;
        this.progress = progress;
    }

    public int getMax() {
        return max;
    }

    public int getProgress() {
        return progress;
    }
}
