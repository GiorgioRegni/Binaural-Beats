package com.ihunda.android.binauralbeat;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihunda.android.binauralbeat.db.PeriodModel;
import com.ihunda.android.binauralbeat.db.VoiceModel;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;


public class AddPresetAdapter extends MultiLevelAdapter {

    private PeriodHolder periodHolder;
    private VoiceHolder voiceHolder;
    private Context mContext;
    private ArrayList<RecyclerViewItem> arrayList = new ArrayList<>();
    private PeriodModel periodModel;
    private VoiceModel voiceModel;
    private MultiLevelRecyclerView mMultiLevelRecyclerView;
    private AddPresetActivity addPresetActivity;

    AddPresetAdapter(Context mContext, ArrayList<RecyclerViewItem> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView, AddPresetActivity addPresetActivity) {
        super(mListItems);
        this.arrayList = mListItems;
        this.mContext = mContext;
        this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
        this.addPresetActivity = addPresetActivity;
    }

    private void setExpandButton(ImageView expandButton, boolean isExpanded) {
        // set the icon based on the current state
        expandButton.setImageResource(isExpanded ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new PeriodHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_period, parent, false));
        } else {
            return new VoiceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_voice, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof PeriodModel) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PeriodHolder) {
            periodHolder = (PeriodHolder) holder;
            periodModel = (PeriodModel) arrayList.get(position);
            periodHolder.tvPeriod.setText(mContext.getString(R.string.period) + " " + periodModel.getLocalPosition());
            periodHolder.tvDuration.setText(formatTime(periodModel.getDuration()));
            periodHolder.tvBackground.setText("" + periodModel.getBackground());
            periodHolder.tvVisualizer.setText("" + periodModel.getVisualizer());
            periodHolder.tvVolume.setText("" + periodModel.getBackgroundVolume());

            if (periodModel.getVoiceModelArrayList() != null && periodModel.getVoiceModelArrayList().size() > 0) {
                setExpandButton(periodHolder.ivExpand, periodModel.isExpanded());
                periodHolder.ivExpand.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2.0f
                );
                periodHolder.llVoice.setLayoutParams(param);
            } else {
                periodHolder.ivExpand.setVisibility(View.GONE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.5f
                );
                periodHolder.llVoice.setLayoutParams(param);
            }
            Log.e("MuditLog", periodModel.getLevel() + " " + periodModel.getPosition() + " " + periodModel.isExpanded() + "");
//            float density = mContext.getResources().getDisplayMetrics().density;
//            ((ViewGroup.MarginLayoutParams) periodHolder.llParent.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
        }

        if (holder instanceof VoiceHolder) {
            voiceHolder = (VoiceHolder) holder;
            voiceModel = (VoiceModel) arrayList.get(position);
            if (voiceModel.getLocalPosition() == 1) {
                voiceHolder.tvVoiceHeading.setVisibility(View.VISIBLE);
            } else {
                voiceHolder.tvVoiceHeading.setVisibility(View.GONE);
            }
            voiceHolder.tvVoice.setText(mContext.getString(R.string.voice) + " " + voiceModel.getLocalPosition());
            voiceHolder.tvFreqEnd.setText("" + voiceModel.getFreqEnd());
            voiceHolder.tvFreqStart.setText("" + voiceModel.getFreqStart());
            voiceHolder.tvVolume.setText("" + voiceModel.getVolume());
            voiceHolder.tvPitch.setText("" + voiceModel.getNote());

            float density = mContext.getResources().getDisplayMetrics().density;
            ((ViewGroup.MarginLayoutParams) voiceHolder.llParent.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
        }
    }

    private class PeriodHolder extends RecyclerView.ViewHolder {

        TextView tvPeriod;
        TextView tvDuration;
        TextView tvVolume;
        TextView tvBackground;
        TextView tvVisualizer;
        ImageView ivExpand;
        ImageView ivEdit;
        TextView tvAdd;
        ImageView ivDelete;
        LinearLayout llParent;
        LinearLayout llVoice;

        PeriodHolder(View itemView) {
            super(itemView);
            tvPeriod = (TextView) itemView.findViewById(R.id.tvPeriod);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvVolume = (TextView) itemView.findViewById(R.id.tvVolume);
            tvBackground = (TextView) itemView.findViewById(R.id.tvBackground);
            tvVisualizer = (TextView) itemView.findViewById(R.id.tvVisualizer);
            ivExpand = (ImageView) itemView.findViewById(R.id.ivArrow);
            ivEdit = (ImageView) itemView.findViewById(R.id.ivEdit);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            tvAdd = (TextView) itemView.findViewById(R.id.tvAdd);
            llParent = itemView.findViewById(R.id.llParent);
            llVoice = itemView.findViewById(R.id.llVoice);
            // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
            // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set click event on item here
//                    Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

            ivExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
                    ivExpand.animate().rotation(arrayList.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();
                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPresetActivity.showAddEditPeriodDialog(getAdapterPosition());
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPresetActivity.delete(getAdapterPosition());
                }
            });

            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPresetActivity.showAddToPeriodDialog(getAdapterPosition());
                }
            });
        }
    }

    private class VoiceHolder extends RecyclerView.ViewHolder {

        TextView tvVoice;
        TextView tvVoiceHeading;
        TextView tvFreqStart;
        TextView tvFreqEnd;
        TextView tvVolume;
        TextView tvPitch;
        ImageView ivEdit;
        ImageView ivDelete;
        CardView llParent;

        VoiceHolder(View itemView) {
            super(itemView);
            tvVoice = (TextView) itemView.findViewById(R.id.tvVoice);
            tvVoiceHeading = (TextView) itemView.findViewById(R.id.tvVoiceHeading);
            tvFreqStart = (TextView) itemView.findViewById(R.id.tvFreqStart);
            tvFreqEnd = (TextView) itemView.findViewById(R.id.tvFreqEnd);
            tvVolume = (TextView) itemView.findViewById(R.id.tvVolume);
            tvPitch = (TextView) itemView.findViewById(R.id.tvPitch);
            ivEdit = (ImageView) itemView.findViewById(R.id.ivEdit);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            llParent = itemView.findViewById(R.id.llParent);
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPresetActivity.showAddToPeriodDialog(getAdapterPosition());
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPresetActivity.delete(getAdapterPosition());
                }
            });
        }
    }

    private String formatTime(int timeInSecs) {
        int minutes = timeInSecs / 60;
        return String.format("%s:%s",
                formatTimeNumberwithLeadingZero(minutes),
                formatTimeNumberwithLeadingZero(timeInSecs - minutes * 60));
    }

    private String formatTimeNumberwithLeadingZero(int t) {
        if (t > 9)
            return String.format("%2d", t);
        else
            return String.format("0%1d", t);
    }
}
