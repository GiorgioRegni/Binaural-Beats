package com.ihunda.android.binauralbeat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihunda.android.binauralbeat.db.HistoryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<HistoryModel> historyArrayList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;

    // RecyclerView recyclerView;
    public HistoryAdapter(ArrayList arrayList, Context context) {
        this.historyArrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        if (viewType == TYPE_HEADER) {
//            View listItem = layoutInflater.inflate(R.layout.row_history_header, parent, false);
//            HeaderHolder headerHolder = new HeaderHolder(listItem);
//            return headerHolder;
//        } else {
        View listItem = layoutInflater.inflate(R.layout.row_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvDate.setText(context.getText(R.string.date_history) + " " + getDate(historyArrayList.get(position).getDateMillis(), "dd/MM/yy HH:mm"));
            ((ViewHolder) holder).tvTotal.setText(context.getText(R.string.total_time) + " " + formatTime((int) (historyArrayList.get(position).getCompletedTime() / 1000)));

            if (TextUtils.isEmpty(historyArrayList.get(position).getProgramName())) {
                ((ViewHolder) holder).tvPresetName.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).tvPresetName.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).tvPresetName.setText(context.getText(R.string.program_name) + " " + historyArrayList.get(position).getProgramName());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return TYPE_HEADER;
//        } else {
        return TYPE_ITEM;
//        }
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvTotal, tvPresetName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            this.tvTotal = (TextView) itemView.findViewById(R.id.tvTotalTime);
            this.tvPresetName = (TextView) itemView.findViewById(R.id.tvProgramName);
        }
    }

//    class HeaderHolder extends RecyclerView.ViewHolder {
//        public HeaderHolder(View itemView) {
//            super(itemView);
//        }
//    }

    private String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(new Date(milliSeconds));
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

    private String formatTimeNumberwithLeadingZero_3(int t) {
        if (t > 99)
            return String.format("%3d", t);
        if (t > 9)
            return String.format("0%2d", t);
        else
            return String.format("00%1d", t);
    }
}