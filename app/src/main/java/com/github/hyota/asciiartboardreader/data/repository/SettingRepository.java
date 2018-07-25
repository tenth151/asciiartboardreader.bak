package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.function.BiFunction;
import com.github.hyota.asciiartboardreader.data.datasource.SettingLocalDataSource;
import com.github.hyota.asciiartboardreader.data.datasource.SettingRemoteDataSource;
import com.github.hyota.asciiartboardreader.domain.model.BaseProgressEvent;
import com.github.hyota.asciiartboardreader.domain.model.Setting;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.inject.Inject;

import io.reactivex.Single;

public class SettingRepository {

    @NonNull
    private SettingLocalDataSource localDataSource;
    @NonNull
    private SettingRemoteDataSource remoteDataSource;

    @Inject
    SettingRepository(@NonNull SettingLocalDataSource localDataSource, @NonNull SettingRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @NonNull
    public Single<Setting> load(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, @Nullable BiFunction<Integer, Integer, ? extends BaseProgressEvent> progressEvent) {
        return localDataSource.load(scheme, host, category, directory)
                .onErrorResumeNext(remoteDataSource.load(scheme, host, category, directory, progressEvent)
                        .flatMap(sink -> localDataSource.save(scheme, host, category, directory, sink)))
                .map(file -> parse(file, host));
    }

    @NonNull
    public Single<Setting> loadFromRemote(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, @Nullable BiFunction<Integer, Integer, ? extends BaseProgressEvent> progressEvent) {
        return remoteDataSource.load(scheme, host, category, directory, progressEvent)
                .flatMap(sink -> localDataSource.save(scheme, host, category, directory, sink))
                .map(file -> parse(file, host));
    }

    private Setting parse(@NonNull File file, @NonNull String host) throws IOException {
        Charset charset;
        if (ShitarabaConstant.HOST.equals(host)) {
            charset = ShitarabaConstant.ENCODE;
        } else {
            throw new IllegalStateException("not implements"); // TODO
        }
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, charset);
             BufferedReader br = new BufferedReader(isr)) {
            Setting setting = new Setting();
            String line;
            while ((line = br.readLine()) != null) {
                String[] params = line.split("=");
                if (params.length != 2) {
                    continue;
                }
                if ("BBS_TITLE".equals(params[0])) {
                    setting.setTitle(params[1]);
                }
            }
            return setting;
        }
    }

}
