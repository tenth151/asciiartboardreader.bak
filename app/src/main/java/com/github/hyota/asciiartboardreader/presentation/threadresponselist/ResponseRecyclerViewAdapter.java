package com.github.hyota.asciiartboardreader.presentation.threadresponselist;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.domain.model.ResponseInfo;
import com.turingtechnologies.materialscrollbar.ICustomAdapter;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResponseRecyclerViewAdapter extends RecyclerView.Adapter<ResponseRecyclerViewAdapter.ViewHolder>
        implements ICustomAdapter {

    private static final DateTimeFormatter RESPONSE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd(E) HH:mm:ss");

    @NonNull
    private List<ResponseInfo> items;

    ResponseRecyclerViewAdapter(@NonNull List<ResponseInfo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_response, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ResponseInfo item = items.get(position);
        holder.item = item;
        holder.no.setText(String.valueOf(item.getNo()));
        holder.name.setText(fromHtml(item.getName()));
        holder.email.setText(item.getEmail());
        holder.dateTime.setText(item.getDateTime().format(RESPONSE_DATE_TIME_FORMATTER));
        holder.id.setText(item.getId());
        Spanned content = fromHtml(item.getContent());
        holder.message.setText(content);
        holder.asciiArt.setText(content);
        if (item.isAsciiArt()) {
            holder.message.setVisibility(View.GONE);
            holder.asciiArt.setVisibility(View.VISIBLE);
        } else {
            holder.message.setVisibility(View.VISIBLE);
            holder.asciiArt.setVisibility(View.GONE);
        }
        if (item.isAbone()) {
            holder.view.setVisibility(View.GONE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public String getCustomStringForElement(int element) {
        return String.valueOf(items.get(element).getNo());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.no)
        TextView no;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.dateTime)
        TextView dateTime;
        @BindView(R.id.id)
        TextView id;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.ascii_art)
        TextView asciiArt;
        public ResponseInfo item;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + no.getText() + "'";
        }
    }

    private Spanned fromHtml(@NonNull String str) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(str);
        }
    }
}
