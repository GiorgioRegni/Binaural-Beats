package com.ihunda.android.binauralbeat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihunda.android.binauralbeat.db.PeriodModel;
import com.ihunda.android.binauralbeat.db.VoiceModel;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.Locale;


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
            return new VoiceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
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

            periodHolder.tvPeriod.setText(mContext.getString(R.string.period) + " " + position);
            periodHolder.tvDuration.setText(formatTime(periodModel.getDuration()));
            periodHolder.tvBackground.setText("" + periodModel.getBackground());
            periodHolder.tvVolume.setText("" + periodModel.getBackgroundVolume());

            if (periodModel.getVoiceModelArrayList() != null && periodModel.getVoiceModelArrayList().size() > 0) {
                setExpandButton(periodHolder.ivExpand, periodModel.isExpanded());
                periodHolder.ivExpand.setVisibility(View.VISIBLE);
            } else {
                periodHolder.ivExpand.setVisibility(View.GONE);
            }
            Log.e("MuditLog", periodModel.getLevel() + " " + periodModel.getPosition() + " " + periodModel.isExpanded() + "");
//            float density = mContext.getResources().getDisplayMetrics().density;
//            ((ViewGroup.MarginLayoutParams) periodHolder.llParent.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
        }

        if (holder instanceof VoiceHolder) {
            voiceHolder = (VoiceHolder) holder;
            voiceModel = (VoiceModel) arrayList.get(position);
            voiceHolder.mTitle.setText("" + voiceModel.getFreqEnd());
            voiceHolder.mSubtitle.setText("" + voiceModel.getFreqStart());

//            if (periodModel.getVoiceModelArrayList() != null && periodModel.getVoiceModelArrayList().size() > 0) {
//                setExpandButton(periodHolder.mExpandIcon, periodModel.isExpanded());
//                periodHolder.mExpandButton.setVisibility(View.VISIBLE);
//            } else {
            voiceHolder.mExpandButton.setVisibility(View.GONE);
//            }
            Log.e("MuditLog", voiceModel.getLevel() + " " + voiceModel.getPosition() + " " + voiceModel.isExpanded() + "");
            float density = mContext.getResources().getDisplayMetrics().density;
            ((ViewGroup.MarginLayoutParams) voiceHolder.mTextBox.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
        }

    }

    private class PeriodHolder extends RecyclerView.ViewHolder {

        TextView tvPeriod;
        TextView tvDuration;
        TextView tvVolume;
        TextView tvBackground;
        ImageView ivExpand;
        ImageView ivEdit;
        LinearLayout llParent;

        PeriodHolder(View itemView) {
            super(itemView);
            tvPeriod = (TextView) itemView.findViewById(R.id.tvPeriod);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvVolume = (TextView) itemView.findViewById(R.id.tvVolume);
            tvBackground = (TextView) itemView.findViewById(R.id.tvBackground);
            ivExpand = (ImageView) itemView.findViewById(R.id.ivArrow);
            ivEdit = (ImageView) itemView.findViewById(R.id.ivEdit);
            llParent = itemView.findViewById(R.id.llParent);
            // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
            // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set click event on item here
//                    Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

            //set click listener on LinearLayout because the click area is bigger than the ImageView
            ivExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set click event on expand button here
                    mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
                    // rotate the icon based on the current state
                    // but only here because otherwise we'd see the animation on expanded items too while scrolling
                    ivExpand.animate().rotation(arrayList.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();

//                    Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s", getAdapterPosition(), periodModel.isExpanded()), Toast.LENGTH_SHORT).show();
                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPresetActivity.showAddEditPeriodDialog(getAdapterPosition());
                }
            });
        }
    }

    private class VoiceHolder extends RecyclerView.ViewHolder {

        TextView mTitle, mSubtitle;
        ImageView mExpandIcon;
        LinearLayout mTextBox, mExpandButton;

        VoiceHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mSubtitle = (TextView) itemView.findViewById(R.id.subtitle);
            mExpandIcon = (ImageView) itemView.findViewById(R.id.image_view);
            mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
            mExpandButton = (LinearLayout) itemView.findViewById(R.id.expand_field);

            // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
            // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set click event on item here
                    Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

//            //set click listener on LinearLayout because the click area is bigger than the ImageView
//            mExpandButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // set click event on expand button here
//                    mMultiLevelRecyclerView.toggleItemsGroup(getAdapterPosition());
//                    // rotate the icon based on the current state
//                    // but only here because otherwise we'd see the animation on expanded items too while scrolling
//                    mExpandIcon.animate().rotation(arrayList.get(getAdapterPosition()).isExpanded() ? -180 : 0).start();
//
//                    Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s", getAdapterPosition(), periodModel.isExpanded()), Toast.LENGTH_SHORT).show();
//                }
//            });
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
