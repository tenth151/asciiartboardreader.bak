package com.github.hyota.asciiartboardreader.domain.model;

@FunctionalInterface
public interface ProgressUpdateListener {

    void onProgressUpdate(int max, int progress);

}
