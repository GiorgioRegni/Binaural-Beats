package com.ihunda.android.binauralbeat.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalpit on 9/04/19.
 */
public class PeriodModel extends RecyclerViewItem implements Parcelable {
    private int duration;
    private String background;
    private String Visualizer;
    private String backgroundVolume;
    private String voiceModelArray;
    private ArrayList<VoiceModel> voiceModelArrayList = new ArrayList<VoiceModel>();
    private int localPosition;

    public String getVoiceModelArray() {
        return voiceModelArray;
    }

    public void setVoiceModelArray(String voiceModelArray) {
        this.voiceModelArray = voiceModelArray;
    }

    public int getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(int localPosition) {
        this.localPosition = localPosition;
    }


    public PeriodModel(int level) {
        super(level);
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBackgroundVolume() {
        return backgroundVolume;
    }

    public void setBackgroundVolume(String backgroundVolume) {
        this.backgroundVolume = backgroundVolume;
    }

    public ArrayList<VoiceModel> getVoiceModelArrayList() {
        return voiceModelArrayList;
    }

    public void setVoiceModelArrayList(ArrayList<VoiceModel> voiceModelArrayList) {
        this.voiceModelArrayList = voiceModelArrayList;
    }

    public String getVisualizer() {
        return Visualizer;
    }

    public void setVisualizer(String visualizer) {
        Visualizer = visualizer;
    }

    PeriodModel() {
        super(1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(duration);
        parcel.writeString(background);
        parcel.writeString(Visualizer);
        parcel.writeString(backgroundVolume);
        parcel.writeString(voiceModelArray);
        parcel.writeList((List) voiceModelArrayList);
    }

    protected PeriodModel(Parcel in) {
        super(1);
        duration = in.readInt();
        background = in.readString();
        Visualizer = in.readString();
        backgroundVolume = in.readString();
        voiceModelArray = in.readString();
        voiceModelArrayList = new ArrayList<>();
        in.readList((List) voiceModelArrayList, VoiceModel.class.getClassLoader());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PeriodModel> CREATOR = new Parcelable.Creator<PeriodModel>() {
        @Override
        public PeriodModel createFromParcel(Parcel in) {
            return new PeriodModel(in);
        }

        @Override
        public PeriodModel[] newArray(int size) {
            return new PeriodModel[size];
        }
    };
}