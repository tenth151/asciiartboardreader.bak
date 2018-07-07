package com.github.hyota.asciiartboardreader.presentation.threadresponselist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.model.ResponseInfo;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.turingtechnologies.materialscrollbar.CustomIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class ThreadResponseListFragment extends Fragment
        implements ThreadResponseListContract.View {

    private static final String ARG_THREAD_INFO = "threadInfo";

    @Inject
    ThreadResponseListContract.Presenter presenter;
    @BindView(R.id.list)
    @Nullable
    RecyclerView recyclerView;
    @BindView(R.id.dragScrollBar)
    @Nullable
    DragScrollBar scrollBar;

    private Context context;
    private Unbinder unbinder;
    private ResponseRecyclerViewAdapter adapter;

    public static ThreadResponseListFragment newInstance(@NonNull ThreadInfo threadInfo) {
        ThreadResponseListFragment fragment = new ThreadResponseListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_THREAD_INFO, threadInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate((ThreadInfo) Objects.requireNonNull(Objects.requireNonNull(getArguments()).getSerializable(ARG_THREAD_INFO)));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread_response_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (recyclerView != null) {
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecoration);
        }
        if (scrollBar != null) {
            scrollBar.setIndicator(new CustomIndicator(context), true);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setData(@NonNull List<ResponseInfo> items) {
        if (recyclerView != null) {
            adapter = new ResponseRecyclerViewAdapter(items);
            recyclerView.setAdapter(adapter);
        }
    }
}
