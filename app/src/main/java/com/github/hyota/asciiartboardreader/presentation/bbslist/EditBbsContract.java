package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface EditBbsContract {

    interface View {

        void showAlertMessage(@NonNull String message);

        void setTitle(@Nullable String title);

        void succeed();

        void deleted();

    }

    interface Presenter {

        void onOk(long id, long sort, @NonNull String title, @NonNull String url);

        void onDelete(long id);

        void onGetTitle(@NonNull String url);

    }

}
