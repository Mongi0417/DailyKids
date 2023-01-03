package com.project.dailykids;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<ChatDTO> mChatDTO;
    private String myUid;
    private View view;

    public ChatAdapter(ArrayList<ChatDTO> list, String myUid) {
        mChatDTO = list;
        this.myUid = myUid;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_msg_box, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_msg_box, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        ChatDTO item = mChatDTO.get(position);

        holder.name.setText(item.getName());
        holder.message.setText(item.getMessage());
        holder.timestamp.setText(item.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mChatDTO.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatDTO.get(position).getUid().equals(myUid)) {
            return 1;
        } else {
            return 2;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            message = itemView.findViewById(R.id.tvMessage);
            timestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}
