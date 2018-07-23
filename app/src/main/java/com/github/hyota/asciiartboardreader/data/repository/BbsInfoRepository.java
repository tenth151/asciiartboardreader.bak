package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.data.datasource.BbsInfoDataSource;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * 板情報リポジトリ
 */
public class BbsInfoRepository {

    @NonNull
    private BbsInfoDataSource dataSource;

    @Inject
    BbsInfoRepository(@NonNull BbsInfoDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    public Single<List<BbsInfo>> findAll() {
        return dataSource.findAll();
    }

    @NonNull
    public Maybe<BbsInfo> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        return dataSource.findByUrl(scheme, host, category, directory);
    }

    @NonNull
    public Maybe<BbsInfo> findByTitle(@NonNull String title) {
        return dataSource.findByTitle(title);
    }

    @NonNull
    public Single<BbsInfo> save(@NonNull BbsInfo bbsInfo) {
        return dataSource.save(bbsInfo);
    }

    @NonNull
    public Completable delete(long id) {
        return dataSource.delete(id);
    }

}
