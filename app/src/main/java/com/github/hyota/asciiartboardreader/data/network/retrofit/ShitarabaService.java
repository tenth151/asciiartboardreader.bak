package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ShitarabaService {

    @GET("/{category}/{directory}/subject.txt")
    @Streaming
    Single<Response<ResponseBody>> subject(@NonNull @Path("category") String category,
                                           @NonNull @Path("directory") String directory);

    @Streaming
    @GET("/bbs/api/setting.cgi/{category}/{directory}/")
    Single<Response<ResponseBody>> setting(@NonNull @Path("category") String category,
                                           @NonNull @Path("directory") String directory);

    @Streaming
    @GET("/bbs/rawmode.cgi/{category}/{directory}/{unixTime}/")
    Single<Response<ResponseBody>> dat(@NonNull @Path("category") String category,
                                       @NonNull @Path("directory") String directory,
                                       @Path("unixTime") long unixTime);

}
