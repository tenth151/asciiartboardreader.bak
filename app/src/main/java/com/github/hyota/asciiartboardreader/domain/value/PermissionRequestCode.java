package com.github.hyota.asciiartboardreader.domain.value;

import android.support.annotation.Nullable;

import com.annimon.stream.Stream;

public enum PermissionRequestCode {

    ADD_BBS,
    GET_BBS_TITLE,;

    @Nullable
    public static PermissionRequestCode valueOf(int ordinal) {
        return Stream.of(values())
                .filter(it -> it.ordinal() == ordinal)
                .findFirst()
                .orElse(null);
    }

}
