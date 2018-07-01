package com.github.hyota.asciiartboardreader.data.db.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "bbs_info",
        indices = {
                @Index(value = {
                        "scheme",
                        "server",
                        "category",
                        "directory"
                }, unique = true),
        })
public class BbsInfoEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String title;
    @NonNull
    private String scheme;
    @NonNull
    private String server;
    @NonNull
    private String category;
    @Nullable
    private String directory;
    private long sort;

    public BbsInfoEntity(@NonNull String title, @NonNull String scheme, @NonNull String server, @NonNull String category, @Nullable String directory, long sort) {
        this.title = title;
        this.scheme = scheme;
        this.server = server;
        this.category = category;
        this.directory = directory;
        this.sort = sort;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getScheme() {
        return scheme;
    }

    public void setScheme(@NonNull String scheme) {
        this.scheme = scheme;
    }

    @NonNull
    public String getServer() {
        return server;
    }

    public void setServer(@NonNull String server) {
        this.server = server;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    @Nullable
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(@Nullable String directory) {
        this.directory = directory;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }
}
