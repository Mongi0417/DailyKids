package com.project.dailykids.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.dailykids.R;
import com.project.dailykids.activities.KinderInformationActivity;
import com.project.dailykids.models.KinderInfo;
import com.project.dailykids.utils.GetJsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchKinderAdapter extends RecyclerView.Adapter<SearchKinderAdapter.ViewHolder> {
    private ArrayList<KinderInfo> mData;
    private Context context;
    private GetJsonObject mCallback;
    private String who = "", nickname = "";

    public SearchKinderAdapter(ArrayList<KinderInfo> mData, Context context, GetJsonObject mCallback, String who, String nickname) {
        this.mData = mData;
        this.context = context;
        this.mCallback = mCallback;
        this.who = who;
        this.nickname = nickname;
    }

    @Override
    public SearchKinderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_kinder, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchKinderAdapter.ViewHolder holder, int position) {
        KinderInfo item = mData.get(position);

        holder.tvKinderName.setText(item.getKinderName());
        holder.tvKinderAddress.setText(item.getKinderAddress());
        holder.tvKinderTel.setText(item.getKinderTel());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setItems(ArrayList<KinderInfo> list) {
        mData = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKinderName;
        TextView tvKinderAddress;
        TextView tvKinderTel;

        public ViewHolder(View itemView) {
            super(itemView);

            tvKinderName = itemView.findViewById(R.id.search_tvKinderName);
            tvKinderAddress = itemView.findViewById(R.id.search_tvKinderAddress);
            tvKinderTel = itemView.findViewById(R.id.search_tvKinderTel);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                context = view.getContext();
                if (pos != RecyclerView.NO_POSITION) {
                    //mListener.onItemClick(view, pos);
                    Intent intent = new Intent(context, KinderInformationActivity.class);
                    JSONObject jsonObject = mCallback.getJsonObject();
                    intent.putExtra("kinderInfo", jsonObject.toString());
                    intent.putExtra("kinderName", mData.get(pos).getKinderName());
                    intent.putExtra("who", who);
                    intent.putExtra("name", nickname);
                    context.startActivity(intent);
                }
            });
        }
    }
}
