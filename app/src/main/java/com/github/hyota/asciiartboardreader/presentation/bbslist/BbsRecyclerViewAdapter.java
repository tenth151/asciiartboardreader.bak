package com.github.hyota.asciiartboardreader.presentation.bbslist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.model.BbsInfo;
import com.github.hyota.asciiartboardreader.presentation.bbslist.BbsListFragment.OnBbsSelectListener;
import com.github.hyota.asciiartboardreader.presentation.bbslist.dummy.DummyContent.DummyItem;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BbsRecyclerViewAdapter extends RecyclerView.Adapter<BbsRecyclerViewAdapter.ViewHolder> {

    private final List<BbsInfo> items;
    private final OnBbsSelectListener listener;

    public BbsRecyclerViewAdapter(List<BbsInfo> items, BbsListFragment.OnBbsSelectListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bbs_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        BbsInfo item = items.get(position);
        holder.item = item;
        holder.title.setText(item.getTitle());
        holder.url.setText(Stream.of(item.getScheme(), item.getServer(), item.getCategory(), item.getDirectory())
                .filter(str -> str != null)
                .collect(Collectors.joining("/"))
        );

        holder.view.setOnClickListener(view -> {
            if (null != listener) {
                listener.onBbsSelect(holder.item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView url;
        public BbsInfo item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.text_title);
            url = view.findViewById(R.id.text_url);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + url.getText() + "'";
        }
    }
}
