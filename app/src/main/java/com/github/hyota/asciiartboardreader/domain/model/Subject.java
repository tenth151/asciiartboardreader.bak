package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

public class Subject {

    @NonNull
    private List<ThreadSubject> threadSubjectList;

    public Subject(@NonNull List<ThreadSubject> threadSubjectList) {
        this.threadSubjectList = Collections.unmodifiableList(threadSubjectList);
    }

    @NonNull
    public List<ThreadSubject> getThreadSubjectList() {
        return threadSubjectList;
    }

}
