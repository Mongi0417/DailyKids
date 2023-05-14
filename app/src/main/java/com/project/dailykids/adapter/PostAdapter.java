package com.project.dailykids.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.dailykids.R;
import com.project.dailykids.utils.TimestampConverter;
import com.project.dailykids.models.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> mData = new ArrayList<>();
    private OnItemClickListener mListener = null;

    public PostAdapter(ArrayList<Post> list) {
            mData = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post item = mData.get(position);

        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.date.setText(TimestampConverter.timestampToDate(item.getTimestamp()));
        holder.name.setText(item.getName());
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
        TextView title, content, date, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.post_tvTitle);
            content = itemView.findViewById(R.id.post_tvContent);
            date = itemView.findViewById(R.id.post_tvDate);
            name = itemView.findViewById(R.id.post_tvName);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (mListener != null) {
                        mListener.onItemClick(view, pos);
                    }
                }
            });
        }
    }
}
