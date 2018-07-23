package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.datasource.DatRemoteDataSource;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.value.ShitarabaConstant;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import okio.Source;

public class DatNetworkDataSource implements DatRemoteDataSource {

    @NonNull
    private ShitarabaService shitarabaService;

    @Inject
    DatNetworkDataSource(@NonNull ShitarabaService shitarabaService) {
        this.shitarabaService = shitarabaService;
    }

    @NonNull
    @Override
    public Single<Source> load(@NonNull ThreadInfo threadInfo) {
        if (ShitarabaConstant.HOST.equals(threadInfo.getBbsInfo().getHost())) {
            return shitarabaService.dat(threadInfo.getBbsInfo().getCategory(), Objects.requireNonNull(threadInfo.getBbsInfo().getDirectory(), "shitaraba host must not null directory"), threadInfo.getUnixTime())
                    .map(response -> Objects.requireNonNull(response.body()).source());
        } else {
            throw new IllegalStateException("not implemented");
        }
    }

}
