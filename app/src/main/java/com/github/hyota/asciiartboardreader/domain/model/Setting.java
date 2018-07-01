package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.Nullable;

public class Setting {

    @Nullable
    private String title;

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }
}
