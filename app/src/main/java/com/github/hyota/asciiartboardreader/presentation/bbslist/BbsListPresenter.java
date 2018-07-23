package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.BuildConfig;
import com.github.hyota.asciiartboardreader.data.repository.BbsInfoRepository;
import com.github.hyota.asciiartboardreader.di.FragmentScope;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@FragmentScope
public class BbsListPresenter implements BbsListContract.Presenter {

    @NonNull
    private BbsListContract.View view;
    @NonNull
    private BbsInfoRepository bbsInfoRepository;

    private List<BbsInfo> items;

    @Inject
    BbsListPresenter(@NonNull BbsListContract.View view, @NonNull BbsInfoRepository bbsInfoRepository) {
        this.view = view;
        this.bbsInfoRepository = bbsInfoRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void load() {
        view.setData(Collections.emptyList());

        if (BuildConfig.DEBUG) {
            bbsInfoRepository.findAll()
                    .subscribe(bbsInfoList -> {
                        if (bbsInfoList.isEmpty()) {
                            bbsInfoRepository.save(new BbsInfo("やる夫スレヒロイン板（新）", "http", "jbbs.shitaraba.net", "otaku", "15956"))
                                    .subscribe();
                            bbsInfoRepository.save(new BbsInfo("やる夫系雑談・避難・投下板（緊急避難用）", "http", "jbbs.shitaraba.net", "internet", "3408"))
                                    .subscribe();
                        }
                    });
        }
        bbsInfoRepository.findAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bbsInfoList -> {
                    items = new ArrayList<>(bbsInfoList);
                    view.setData(items);
                });
    }

    @Override
    public void onAddButtonClick() {
        view.showAddBbsDialog();
    }

    @Override
    public void onBbsLongClick(@NonNull BbsInfo bbsInfo) {
        Uri.Builder builder = new Uri.Builder()
                .scheme(bbsInfo.getScheme())
                .authority(bbsInfo.getHost())
                .appendPath(bbsInfo.getCategory());
        if (bbsInfo.getDirectory() != null) {
            builder.appendPath(bbsInfo.getDirectory());
        }
        view.showEditBbsDialog(bbsInfo.getId(), bbsInfo.getSort(), bbsInfo.getTitle(), builder.build().toString());
    }

    @Override
    public void onCreateBbs(@NonNull BbsInfo bbsInfo) {
        items.add(bbsInfo);
        view.notifyItemInserted(items.size() - 1);
    }

    @Override
    public void onEditBbs(@NonNull BbsInfo bbsInfo) {
        int position = Stream.of(items).map(BbsInfo::getId).collect(Collectors.toList()).indexOf(bbsInfo.getId());
        if (position > -1) {
            items.set(position, bbsInfo);
            view.notifyItemChanged(position);
        } else {
            Timber.w("not found id = %s", bbsInfo.getId());
        }
    }

    @Override
    public void onDeleteBbs(long id) {
        int position = Stream.of(items).map(BbsInfo::getId).collect(Collectors.toList()).indexOf(id);
        if (position > -1) {
            items.remove(position);
            view.notifyItemChanged(position);
        } else {
            Timber.w("not found id = %s", position);
        }
    }

}
