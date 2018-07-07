package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.Dat;

import java.io.File;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.Source;

public class DatRepositoryImpl implements DatRepository {

    @NonNull
    private DatRepository datLocalRepository;
    @NonNull
    private DatRepository datNetworkRepository;

    public DatRepositoryImpl(@NonNull DatRepository datLocalRepository, @NonNull DatRepository datNetworkRepository) {
        this.datLocalRepository = datLocalRepository;
        this.datNetworkRepository = datNetworkRepository;
    }

    @Override
    public Maybe<Dat> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, long unixTime) {
        return Maybe.create(e -> datLocalRepository.findByUrl(scheme, host, category, directory, unixTime)
                .subscribe(e::onSuccess,
                        e::onError,
                        () -> datNetworkRepository.findByUrl(scheme, host, category, directory, unixTime)
                                .subscribe(e::onSuccess)));
    }

    @Override
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, long unixTime, @NonNull Source source) {
        return datLocalRepository.save(host, category, directory, unixTime, source);
    }

}
