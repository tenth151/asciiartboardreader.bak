package com.github.hyota.asciiartboardreader.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.data.network.retrofit.ShitarabaService;
import com.github.hyota.asciiartboardreader.domain.model.ProgressResponseBody;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class NetworkModule {

    @Named("shitaraba")
    @Provides
    @Singleton
    Retrofit provideShitarabaRetorofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://jbbs.shitaraba.net/")
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    ShitarabaService provideShitarabaService(@Named("shitaraba") Retrofit retrofit) {
        return retrofit.create(ShitarabaService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context context) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .body(new ProgressResponseBody(response.body()))
                            .build();
                })
                .addInterceptor(new ChuckInterceptor(context))
                .build();
    }

}
