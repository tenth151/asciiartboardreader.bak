package com.github.hyota.asciiartboardreader;

import com.akaita.java.rxjava2debug.RxJava2Debug;
import com.facebook.stetho.Stetho;
import com.github.hyota.asciiartboardreader.di.DaggerAppComponent;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setFontAttrId(R.attr.fontPath).build());
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
            RxJava2Debug.enableRxJava2AssemblyTracking(new String[]{"com.github.hyota.asciiartboardreader"});
        }
        LeakCanary.install(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).create(this);
    }

}
