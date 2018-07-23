package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface HistoryDataSource {

    @NonNull
    Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo);

    @NonNull
    Maybe<ThreadInfo> find(@NonNull ThreadInfo threadInfo);

    @NonNull
    Single<ThreadInfo> saveLastUpdate(@NonNull ThreadInfo threadInfo, @NonNull ZonedDateTime dateTime);

    @NonNull
    Single<ThreadInfo> updateReadCount(@NonNull ThreadInfo threadInfo, long readCount);

    @NonNull
    Single<ThreadInfo> updateLastWrite(@NonNull ThreadInfo threadInfo, @NonNull ZonedDateTime dateTime);

    @NonNull
    Completable delete(@NonNull ThreadInfo threadInfo);

}
