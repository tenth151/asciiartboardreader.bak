package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.datasource.HistoryDataSource;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class HistoryRepository {

    @NonNull
    private HistoryDataSource dataSource;

    @Inject
    HistoryRepository(@NonNull HistoryDataSource dataSource) {
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
    public Single<ThreadInfo> saveLastUpdate(@NonNull ThreadInfo threadInfo, @NonNull ZonedDateTime dateTime) {
        return dataSource.saveLastUpdate(threadInfo, dateTime);
    }

    @NonNull
    public Single<ThreadInfo> updateReadCount(@NonNull ThreadInfo threadInfo, long readCount) {
        return dataSource.updateReadCount(threadInfo, readCount);
    }

    @NonNull
    public Single<ThreadInfo> updateLastWrite(@NonNull ThreadInfo threadInfo, @NonNull ZonedDateTime dateTime) {
        return dataSource.updateLastWrite(threadInfo, dateTime);
    }

    @NonNull
    public Completable delete(@NonNull ThreadInfo threadInfo) {
        return dataSource.delete(threadInfo);
    }

}
