package com.github.hyota.asciiartboardreader.di;

import android.app.Application;

import com.github.hyota.asciiartboardreader.MyApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        DataBaseModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        ActivityModule.class,
})
public interface AppComponent extends AndroidInjector<MyApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApplication> {
        @BindsInstance
        abstract public Builder application(Application application);
    }

}