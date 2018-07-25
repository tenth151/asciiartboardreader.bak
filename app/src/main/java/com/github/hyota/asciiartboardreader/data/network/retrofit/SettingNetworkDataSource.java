package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.function.BiFunction;
import com.github.hyota.asciiartboardreader.data.datasource.SettingRemoteDataSource;
import com.github.hyota.asciiartboardreader.domain.model.NetworkException;
import com.github.hyota.asciiartboardreader.domain.model.BaseProgressEvent;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import okio.Source;

public class SettingNetworkDataSource implements SettingRemoteDataSource {

    @NonNull
    private ShitarabaService shitarabaService;
    @NonNull
    private ProgressInterceptor progressInterceptor;

    @Inject
    SettingNetworkDataSource(@NonNull ShitarabaService shitarabaService, @NonNull ProgressInterceptor progressInterceptor) {
        this.shitarabaService = shitarabaService;
        this.progressInterceptor = progressInterceptor;
    }

    @NonNull
    @Override
    public Single<Source> load(@NonNull String scheme, @NonNull String host, @NonNull String category, @Nullable String directory, @Nullable BiFunction<Integer, Integer, ? extends BaseProgressEvent> progressEvent) {
        progressInterceptor.setProgressEvent(progressEvent);
        if (ShitarabaConstant.HOST.equals(host)) {
            return shitarabaService.setting(category, Objects.requireNonNull(directory, "shitaraba host must not null directory"))
                    .map(response -> {
                        if (response.isSuccessful()) {
                            return Objects.requireNonNull(response.body()).source();
                        } else {
                            ResponseBody body = response.errorBody();
                            String message = "";
                            if (body != null) {
                                message = body.string();
                            }
                            throw new NetworkException(message, response.code());
                        }
                    });
        } else {
            throw new IllegalStateException("not implemented");
        }
    }

}
