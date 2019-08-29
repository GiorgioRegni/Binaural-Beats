package com.ihunda.android.binauralbeat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihunda.android.binauralbeat.db.PresetModel;

import java.util.ArrayList;

public class PresetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PresetModel> arrayList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private PresetListActivity presetListActivity;

    public PresetListAdapter(ArrayList arrayList, PresetListActivity presetListActivity) {
        this.arrayList = arrayList;
        this.presetListActivity = presetListActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_preset, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvPreset.setText(presetListActivity.getString(R.string.preset) + " " + (position + 1));
            ((ViewHolder) holder).tvName.setText(arrayList.get(position).getName());
            ((ViewHolder) holder).tvAuthor.setText(arrayList.get(position).getAuthor());
            ((ViewHolder) holder).tvDesc.setText(arrayList.get(position).getDescription());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPreset, tvName, tvAuthor, tvDesc;
        ImageView ivEdit;
        ImageView ivDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvPreset = (TextView) itemView.findViewById(R.id.tvPreset);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            this.tvDesc = (TextView) itemView.findViewById(R.id.tvDescription);
            ivEdit = (ImageView) itemView.findViewById(R.id.ivEdit);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presetListActivity.edit(getAdapterPosition());
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presetListActivity.delete(getAdapterPosition());
                }
            });
        }
    }
}