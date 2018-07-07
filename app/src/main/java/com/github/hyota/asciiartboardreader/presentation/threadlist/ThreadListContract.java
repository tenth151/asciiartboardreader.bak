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

    }

    interface Presenter {

        void onCreate(@NonNull BbsInfo bbsInfo);

        void onFavoriteStateChange(@NonNull ThreadInfo threadInfo, boolean favorite);

        void onDeleteFavoriteThread(@NonNull ThreadInfo threadInfo);

    }

}
