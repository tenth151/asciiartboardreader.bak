package com.github.hyota.asciiartboardreader.presentation.threadresponselist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.ResponseInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

public interface ThreadResponseListContract {

    interface View {

        void setData(@NonNull List<ResponseInfo> items);

    }

    interface Presenter {

        void onCreate(@NonNull ThreadInfo threadInfo);

        void onStart();

        void onStop();

    }

}
