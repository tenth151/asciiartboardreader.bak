package com.github.hyota.asciiartboardreader.presentation.threadlist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.github.hyota.asciiartboardreader.domain.value.AlertDialogRequestCode;
import com.github.hyota.asciiartboardreader.presentation.common.dialog.AlertDialogFragment;
import com.turingtechnologies.materialscrollbar.CustomIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class ThreadListFragment extends Fragment
        implements ThreadListContract.View, AlertDialogFragment.Callback {

    private static final String ARG_BBS_INFO = "bbsInfo";

    private static final String PARAM_THREAD_INFO = "threadInfo";

    @Inject
    ThreadListContract.Presenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.drag_scroll_bar)
    DragScrollBar scrollBar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Context context;
    private Unbinder unbinder;
    private OnThreadSelectListener listener;
    private ThreadListRecyclerViewAdapter adapter;

    public interface OnThreadSelectListener {
        void onThreadSelect(ThreadInfo threadInfo);
    }

    public static ThreadListFragment newInstance(@NonNull BbsInfo bbsInfo) {
        ThreadListFragment fragment = new ThreadListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BBS_INFO, bbsInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof OnThreadSelectListener) {
            listener = (OnThreadSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate((BbsInfo) Objects.requireNonNull(Objects.requireNonNull(getArguments()).getSerializable(ARG_BBS_INFO)));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        scrollBar.setIndicator(new CustomIndicator(context), true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void setData(@NonNull List<ThreadInfo> threadInfoList) {
        adapter = new ThreadListRecyclerViewAdapter(threadInfoList, threadInfo -> listener.onThreadSelect(threadInfo), presenter::onFavoriteStateChange);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showConfirmDeleteFavoriteThread(@NonNull ThreadInfo threadInfo) {
        Bundle params = new Bundle();
        params.putSerializable(PARAM_THREAD_INFO, threadInfo);
        new AlertDialogFragment.Builder(this)
                .title("お気に入り削除確認")
                .message("お気に入りを削除すると関連付けたタグ情報も削除されます。nよろしいですか？")
                .positive(android.R.string.ok)
                .negative(android.R.string.cancel)
                .cancelable(true)
                .requestCode(AlertDialogRequestCode.DELETE_FAVORITE_THREAD_CONFIRM)
                .params(params)
                .show();
    }

    @Override
    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void showAlertMessage(@NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateProgress(int max, int progress) {
        progressBar.setMax(max);
        progressBar.setProgress(progress);
    }

    @Override
    public void onAlertDialogSuccess(AlertDialogRequestCode requestCode, int resultCode, Bundle params) {
        switch (requestCode) {
            case DELETE_FAVORITE_THREAD_CONFIRM:
                if (resultCode == DialogInterface.BUTTON_POSITIVE) {
                    ThreadInfo threadInfo = (ThreadInfo) Objects.requireNonNull(Objects.requireNonNull(params).getSerializable(PARAM_THREAD_INFO));
                    presenter.onDeleteFavoriteThread(threadInfo);
                }
                break;
        }
    }

    @Override
    public void onAlertDialogCancel(AlertDialogRequestCode requestCode, Bundle params) {
        // NOOP
    }
}
