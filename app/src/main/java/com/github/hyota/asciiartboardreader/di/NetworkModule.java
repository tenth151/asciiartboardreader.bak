package com.github.hyota.asciiartboardreader.di;

import com.github.hyota.asciiartboardreader.data.network.retrofit.ShitarabaService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class NetworkModule {

    @Named("shitaraba")
    @Provides
    @Singleton
    Retrofit provideShitarabaRetorofit() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://jbbs.shitaraba.net/")
                .build();
    }

    @Provides
    @Singleton
    ShitarabaService provideShitarabaService(@Named("shitaraba") Retrofit retrofit) {
        return retrofit.create(ShitarabaService.class);
    }

}
