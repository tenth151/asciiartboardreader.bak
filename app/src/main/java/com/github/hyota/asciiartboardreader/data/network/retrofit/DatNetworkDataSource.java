package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.data.datasource.DatRemoteDataSource;
import com.github.hyota.asciiartboardreader.domain.model.NetworkException;
import com.github.hyota.asciiartboardreader.domain.model.ProgressUpdateListener;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import okio.Source;

public class DatNetworkDataSource implements DatRemoteDataSource {

    @NonNull
    private ShitarabaService shitarabaService;
    @NonNull
    private ProgressInterceptor progressInterceptor;

    @Inject
    DatNetworkDataSource(@NonNull ShitarabaService shitarabaService, @NonNull ProgressInterceptor progressInterceptor) {
        this.shitarabaService = shitarabaService;
        this.progressInterceptor = progressInterceptor;
    }

    @NonNull
    @Override
    public Single<Source> load(@NonNull ThreadInfo threadInfo, @Nullable ProgressUpdateListener progressUpdateListener) {
        progressInterceptor.setProgressUpdateListener(progressUpdateListener);
        if (ShitarabaConstant.HOST.equals(threadInfo.getBbsInfo().getHost())) {
            return shitarabaService.dat(threadInfo.getBbsInfo().getCategory(), Objects.requireNonNull(threadInfo.getBbsInfo().getDirectory(), "shitaraba host must not null directory"), threadInfo.getUnixTime())
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
