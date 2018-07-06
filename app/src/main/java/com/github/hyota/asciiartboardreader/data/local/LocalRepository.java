package com.github.hyota.asciiartboardreader.data.local;

import android.os.Environment;

import com.github.hyota.asciiartboardreader.BuildConfig;

import java.io.File;

public interface LocalRepository {

    default File getLocalDirectory() {
        return new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID);
    }

}
