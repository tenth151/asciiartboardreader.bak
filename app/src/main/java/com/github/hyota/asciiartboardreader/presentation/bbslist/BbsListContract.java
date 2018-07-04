package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.support.annotation.NonNull;

import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

public interface BbsListContract {

    interface View {

        void setData(@NonNull List<BbsInfo> bbsInfoList);

        void showAddBbsDialog();

        void showEditBbsDialog(long id, long sort, @NonNull String title, @NonNull String url);

        void notifyItemInserted(int position);

        void notifyItemChanged(int position);

        void notifyItemRemoved(int position);

    }

    interface Presenter {

        void load();

        void onAddButtonClick();

        void onBbsLongClick(@NonNull BbsInfo bbsInfo);

        void onCreateBbs(@NonNull BbsInfo bbsInfo);

        void onEditBbs(@NonNull BbsInfo bbsInfo);

        void onDeleteBbs(long id);

    }

}
