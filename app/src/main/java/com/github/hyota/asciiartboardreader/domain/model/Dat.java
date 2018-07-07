package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;

import java.util.List;

public class Dat {

    @NonNull
    List<ThreadResponse> threadResponseList;

    public Dat(@NonNull List<ThreadResponse> threadResponseList) {
        this.threadResponseList = threadResponseList;
    }

    @NonNull
    public List<ThreadResponse> getThreadResponseList() {
        return threadResponseList;
    }

}
