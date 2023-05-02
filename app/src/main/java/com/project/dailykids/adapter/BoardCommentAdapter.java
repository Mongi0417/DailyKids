package com.project.dailykids.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.dailykids.R;
import com.project.dailykids.models.Board;

import java.util.ArrayList;

public class BoardCommentAdapter extends RecyclerView.Adapter<BoardCommentAdapter.ViewHolder> {
    private ArrayList<Board> mData;

    public BoardCommentAdapter(ArrayList<Board> list) {
        mData = list;
    }

    @NonNull
    @Override
    public BoardCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardCommentAdapter.ViewHolder holder, int position) {
        Board item = mData.get(position);

        holder.comment.setText(item.getComment());
        holder.date.setText(item.getDate() + " " + item.getTime());
        holder.name.setText(item.getName());
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

            name = itemView.findViewById(R.id.board_comment_tvName);
            comment = itemView.findViewById(R.id.board_comment_tvComment);
            date = itemView.findViewById(R.id.board_comment_tvDate);
        }
    }
}
