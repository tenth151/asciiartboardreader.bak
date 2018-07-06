package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.data.repository.SettingRepository;
import com.github.hyota.asciiartboardreader.domain.model.Setting;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.io.File;
import java.util.Objects;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import okio.Source;

public class SettingNetworkRepository implements SettingRepository {

    private ShitarabaService shitarabaService;
    private SettingRepository settingLocalRepository;

    public SettingNetworkRepository(ShitarabaService shitarabaService, SettingRepository settingLocalRepository) {
        this.shitarabaService = shitarabaService;
        this.settingLocalRepository = settingLocalRepository;
    }

    @Override
    public Maybe<Setting> findByUrl(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory) {
        if (ShitarabaConstant.HOST.equals(host)) {
            if (directory == null) {
                throw new IllegalStateException("shitaraba host must not null directory");
            }
            return shitarabaService.setting(category, directory)
                    .flatMap(response -> {
                        if (response.isSuccessful()) {
                            ResponseBody body = Objects.requireNonNull(response.body());
                            return settingLocalRepository.save(host, category, directory, body.source());
                        } else {
                            throw new IllegalStateException("httpCode = " + response.code() + ", message = " + response.message());
                        }
                    })
                    .toMaybe()
                    .map(file -> parse(file, host));
        } else {
            throw new IllegalStateException("not implemented");
        }
    }

    @Override
    public Single<File> save(@NonNull String host, @NonNull String category, @Nullable String directory, @NonNull Source source) {
        throw new IllegalStateException("network repository is not implemented save");
    }
}
