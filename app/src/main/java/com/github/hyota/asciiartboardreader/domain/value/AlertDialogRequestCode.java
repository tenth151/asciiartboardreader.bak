package com.github.hyota.asciiartboardreader.domain.value;

import com.annimon.stream.Stream;

public enum AlertDialogRequestCode {

    DELETE_FAVORITE_THREAD_CONFIRM,
    NONE,;

    public static AlertDialogRequestCode ordinalOf(int ordinal) {
        return Stream.of(values()).filter(it -> it.ordinal() == ordinal).findFirst().orElse(NONE);
    }

}
