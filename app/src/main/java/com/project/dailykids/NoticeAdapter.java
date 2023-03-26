package com.project.dailykids;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private ArrayList<NoticeDTO> mData;

    public NoticeAdapter(ArrayList<NoticeDTO> list) {
        this.mData = list;
    }

    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.ViewHolder holder, int position) {
        NoticeDTO item = mData.get(position);

        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.date.setText(item.postedDate());
        if (item.getNotice() == 1)
            holder.notice.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, date, notice;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notice_tvTitle);
            content = itemView.findViewById(R.id.notice_tvContent);
            date = itemView.findViewById(R.id.notice_tvDate);
            notice = itemView.findViewById(R.id.notice_tvNotice);
        }
    }
}
