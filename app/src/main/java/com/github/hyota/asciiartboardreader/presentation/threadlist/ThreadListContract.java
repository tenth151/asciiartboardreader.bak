package com.github.hyota.asciiartboardreader.presentation.threadlist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

public interface ThreadListContract {

    interface View {

        void setData(@NonNull List<ThreadInfo> threadInfoList);

    }

    interface Presenter {

        void onCreate(@NonNull BbsInfo bbsInfo);

    }

}
