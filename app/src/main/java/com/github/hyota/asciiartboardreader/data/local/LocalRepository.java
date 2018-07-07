package com.github.hyota.asciiartboardreader.data.local;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.BuildConfig;

import java.io.File;

public interface LocalRepository {

    default File getLocalDirectory(@NonNull String appName) {
        return new File(Environment.getExternalStorageDirectory(), appName);
    }

}
