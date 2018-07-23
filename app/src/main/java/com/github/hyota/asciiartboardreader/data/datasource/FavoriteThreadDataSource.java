package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface FavoriteThreadDataSource {

    @NonNull
    Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo);

    @NonNull
    Maybe<ThreadInfo> find(@NonNull ThreadInfo threadInfo);

    @NonNull
    Single<ThreadInfo> save(@NonNull ThreadInfo threadInfo);

    @NonNull
    Completable delete(@NonNull ThreadInfo threadInfo);

}
