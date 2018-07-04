package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

/**
 * 板一覧
 */
public class BbsListFragment extends Fragment implements BbsListContract.View, EditBbsDialogFragment.OnEditBbsListener {

    @Inject
    BbsListContract.Presenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton addBbsButton;

    private Context context;
    @Nullable
    private Unbinder unbinder;
    private BbsRecyclerViewAdapter adapter;
    private OnBbsSelectListener listener;

    public interface OnBbsSelectListener {
        void onBbsSelect(BbsInfo item);
    }

    public static BbsListFragment newInstance() {
        return new BbsListFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof OnBbsSelectListener) {
            listener = (OnBbsSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBbsSelectListener");
        }
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        addBbsButton.setOnClickListener(v -> presenter.onAddButtonClick());

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        presenter.load();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void setData(@NonNull List<BbsInfo> bbsInfoList) {
        Timber.d("setData");
        adapter = new BbsRecyclerViewAdapter(bbsInfoList, listener::onBbsSelect, presenter::onBbsLongClick);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showAddBbsDialog() {
        EditBbsDialogFragment.show(getChildFragmentManager());
    }

    @Override
    public void showEditBbsDialog(long id, long sort, @NonNull String title, @NonNull String url) {
        EditBbsDialogFragment.show(getChildFragmentManager(), id, sort, title, url);
    }

    @Override
    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void notifyItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onCreateBbs(@NonNull BbsInfo bbsInfo) {
        presenter.onCreateBbs(bbsInfo);
    }

    @Override
    public void onEditBbs(@NonNull BbsInfo bbsInfo) {
        presenter.onEditBbs(bbsInfo);
    }

    @Override
    public void onDeleteBbs(long id) {
        presenter.onDeleteBbs(id);
    }
}
