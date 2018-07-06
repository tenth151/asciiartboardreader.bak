package com.github.hyota.asciiartboardreader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.Setting;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.Source;

public interface SettingRepository {

    Maybe<Setting> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory);

    Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source);

    @NonNull
    default Setting parse(@NonNull File file, @NonNull String host) throws IOException {
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
