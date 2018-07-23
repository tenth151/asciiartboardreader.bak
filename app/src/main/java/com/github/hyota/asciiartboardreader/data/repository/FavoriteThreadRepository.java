package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.datasource.FavoriteThreadDataSource;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class FavoriteThreadRepository {

    @NonNull
    private FavoriteThreadDataSource dataSource;

    @Inject
    FavoriteThreadRepository(@NonNull FavoriteThreadDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    public Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo) {
        return dataSource.findByBbs(bbsInfo);
    }

    @NonNull
    public Maybe<ThreadInfo> find(@NonNull ThreadInfo threadInfo) {
        return dataSource.find(threadInfo);
    }

    @NonNull
    public Single<ThreadInfo> save(@NonNull ThreadInfo threadInfo) {
        return dataSource.save(threadInfo);
    }

    @NonNull
    public Completable delete(@NonNull ThreadInfo threadInfo) {
        return dataSource.delete(threadInfo);
    }

}
