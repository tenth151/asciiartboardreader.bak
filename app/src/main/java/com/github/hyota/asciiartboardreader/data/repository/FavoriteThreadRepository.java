package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface FavoriteThreadRepository {

    @NonNull
    Single<List<ThreadInfo>> findByBbs(@NonNull BbsInfo bbsInfo);

    @NonNull
    Maybe<ThreadInfo> findByThread(@NonNull ThreadInfo threadInfo);

    @NonNull
    Completable save(@NonNull ThreadInfo threadInfo);

    @NonNull
    Completable delete(@NonNull ThreadInfo threadInfo);

}
