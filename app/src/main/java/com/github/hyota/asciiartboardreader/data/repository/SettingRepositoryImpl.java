package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.Setting;

import java.io.File;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.Source;

public class SettingRepositoryImpl implements SettingRepository {

    @NonNull
    private SettingRepository settingLocalRepository;
    @NonNull
    private SettingRepository settingNetworkRepository;

    public SettingRepositoryImpl(@NonNull SettingRepository settingLocalRepository, @NonNull SettingRepository settingNetworkRepository) {
        this.settingLocalRepository = settingLocalRepository;
        this.settingNetworkRepository = settingNetworkRepository;
    }

    @Override
    public Maybe<Setting> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        return Maybe.create(e -> settingLocalRepository.findByUrl(scheme, host, category, directory)
                .subscribe(e::onSuccess,
                        e::onError,
                        () -> settingNetworkRepository.findByUrl(scheme, host, category, directory)
                                .subscribe(e::onSuccess)));
    }

    @Override
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source) {
        return settingLocalRepository.save(host, category, directory, source);
    }
}
