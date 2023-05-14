package com.project.dailykids.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.dailykids.R;
import com.project.dailykids.models.Comment;
import com.project.dailykids.utils.TimestampConverter;

import java.util.ArrayList;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {
    private ArrayList<Comment> mData;

    public PostCommentAdapter(ArrayList<Comment> list) {
        mData = list;
    }

    @NonNull
    @Override
    public PostCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentAdapter.ViewHolder holder, int position) {
        Comment item = mData.get(position);

        holder.comment.setText(item.getComment());
        holder.date.setText(TimestampConverter.timestampToDateAndTime(item.getTimestamp()));
        holder.name.setText(item.getNickname());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comment;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.post_comment_tvName);
            comment = itemView.findViewById(R.id.post_comment_tvComment);
            date = itemView.findViewById(R.id.post_comment_tvDate);
        }
    }
}
