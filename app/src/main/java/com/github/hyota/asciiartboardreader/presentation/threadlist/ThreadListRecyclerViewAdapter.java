package com.github.hyota.asciiartboardreader.presentation.threadlist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.model.ThreadInfo;
import com.turingtechnologies.materialscrollbar.ICustomAdapter;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThreadListRecyclerViewAdapter extends RecyclerView.Adapter<ThreadListRecyclerViewAdapter.ViewHolder>
        implements ICustomAdapter {

    private static final DateTimeFormatter THREAD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.getDefault());

    @NonNull
    private List<ThreadInfo> items;
    @Nullable
    private OnThreadClickListener onThreadClickListener;
    @Nullable
    private OnFavoriteStateChangeListener onFavoriteStateChangeListener;

    public interface OnThreadClickListener {
        void onClick(@NonNull ThreadInfo threadInfo);
    }

    public interface OnFavoriteStateChangeListener {
        void onFavoriteStateChange(@NonNull ThreadInfo threadInfo, boolean favorite);
    }

    ThreadListRecyclerViewAdapter(@NonNull List<ThreadInfo> items, @Nullable OnThreadClickListener onThreadClickListener, @NonNull OnFavoriteStateChangeListener onFavoriteStateChangeListener) {
        this.items = items;
        this.onThreadClickListener = onThreadClickListener;
        this.onFavoriteStateChangeListener = onFavoriteStateChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thread_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ThreadInfo item = holder.item = items.get(position);
        switch (item.getState()) {
            case UPDATE:
                holder.state.setImageResource(R.drawable.ic_arrow_upward);
                break;
            case NO_UPDATE:
                holder.state.setImageResource(R.drawable.ic_check);
                break;
            case LOG:
                holder.state.setImageResource(R.drawable.ic_arrow_downward);
                break;
            default:
                holder.state.setImageBitmap(null);
        }
        holder.no.setText(item.getNo() != null ? item.getNo().toString() : "");
        holder.title.setText(item.getTitle());
        holder.bbs.setText(item.getBbsInfo().getTitle());
        holder.count.setText(String.valueOf(item.getCount()));
        holder.readCount.setText(item.getReadCount() != null ? item.getReadCount().toString() : "");
        holder.newCount.setText(item.getNewCount() != null ? item.getNewCount().toString() : "");
        holder.push.setText(String.format(Locale.getDefault(), "%1$.1f", item.getPush()));
        holder.since.setText(item.getSince().format(THREAD_DATE_FORMATTER));
        holder.lastUpdate.setText(item.getLastUpdate() != null ? item.getLastUpdate().format(THREAD_DATE_FORMATTER) : "");
        holder.lastWrite.setText(item.getLastWrite() != null ? item.getLastWrite().format(THREAD_DATE_FORMATTER) : "");
        holder.favorite.setOnCheckedChangeListener(null);
        holder.favorite.setChecked(item.getFavoriteId() != null);

        holder.view.setOnClickListener(view -> {
            if (onThreadClickListener != null) {
                onThreadClickListener.onClick(holder.item);
            }
        });

        holder.favorite.setOnClickListener(view -> {
            if (onFavoriteStateChangeListener != null) {
                boolean checked = holder.favorite.isChecked();
                if (!checked) {
                    holder.favorite.setChecked(true);
                }
                onFavoriteStateChangeListener.onFavoriteStateChange(holder.item, checked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public String getCustomStringForElement(int element) {
        Integer no = items.get(element).getNo();
        return no != null ? no.toString() : "";
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.state)
        ImageView state;
        @BindView(R.id.no)
        TextView no;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.bbs)
        TextView bbs;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.readCount)
        TextView readCount;
        @BindView(R.id.newCount)
        TextView newCount;
        @BindView(R.id.push)
        TextView push;
        @BindView(R.id.since)
        TextView since;
        @BindView(R.id.last_update)
        TextView lastUpdate;
        @BindView(R.id.last_write)
        TextView lastWrite;
        @BindView(R.id.favorite)
        ToggleButton favorite;
        public ThreadInfo item;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}
