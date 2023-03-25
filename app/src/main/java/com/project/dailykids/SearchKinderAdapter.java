package com.project.dailykids;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchKinderAdapter extends RecyclerView.Adapter<SearchKinderAdapter.ViewHolder> {
    private ArrayList<SearchKinderDTO> mData;
    private Context context;
    private GetJsonObject mCallback;
    private String uid = "", who = "", nickname = "";

    @Override
    public SearchKinderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchKinderAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setItems(ArrayList<SearchKinderDTO> list) {
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
