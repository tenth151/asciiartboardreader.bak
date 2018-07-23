package com.github.hyota.asciiartboardreader.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * 板情報データソース
 */
public interface BbsInfoDataSource {

    @NonNull
    Single<List<BbsInfo>> findAll();

    @NonNull
    Maybe<BbsInfo> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory);

    @NonNull
    Maybe<BbsInfo> findByTitle(@NonNull String title);

    @NonNull
    Single<BbsInfo> save(@NonNull BbsInfo bbsInfo);

    @NonNull
    Completable delete(long id);

}
