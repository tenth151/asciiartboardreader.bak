package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * 板情報リポジトリ
 */
public interface BbsInfoRepository {

    @NonNull
    Single<List<BbsInfo>> findAll();

    @NonNull
    Completable save(@NonNull BbsInfo bbsInfo);

    @NonNull
    Completable delete(@NonNull BbsInfo bbsInfo);

}
