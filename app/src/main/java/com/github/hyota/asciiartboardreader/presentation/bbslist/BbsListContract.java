package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

public interface BbsListContract {

    interface View {

        void setData(@NonNull List<BbsInfo> bbsInfoList);

    }

    interface Presenter {

        void onCreateView();

    }

}
