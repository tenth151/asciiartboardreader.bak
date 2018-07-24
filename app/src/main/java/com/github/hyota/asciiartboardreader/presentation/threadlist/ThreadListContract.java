package com.github.hyota.asciiartboardreader.presentation.threadlist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;

import java.util.List;

public interface ThreadListContract {

    interface View {

        void setData(@NonNull List<ThreadInfo> threadInfoList);

        void showConfirmDeleteFavoriteThread(@NonNull ThreadInfo threadInfo);

        void notifyItemChanged(int position);

        void showAlertMessage(@NonNull String message);

    }

    interface Presenter {

        void onCreate(@NonNull BbsInfo bbsInfo);

        void onStart();

        void onStop();

        void onFavoriteStateChange(@NonNull ThreadInfo threadInfo);

        void onDeleteFavoriteThread(@NonNull ThreadInfo threadInfo);

    }

}
