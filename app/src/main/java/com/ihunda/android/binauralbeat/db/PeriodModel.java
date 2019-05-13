package com.ihunda.android.binauralbeat.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.multilevelview.models.RecyclerViewItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by kalpit on 9/04/19.
 */
public class PeriodModel extends RecyclerViewItem implements Serializable, Parcelable {
    @DatabaseField(canBeNull = true, foreign = true)
    private PresetModel presetModel;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int duration;
    @DatabaseField
    private String background;
    @DatabaseField
    private String Visualizer;
    @DatabaseField
    private String backgroundVolume;
    @ForeignCollectionField
    private Collection<VoiceModel> voiceModelArrayList = new ArrayList<VoiceModel>();

    private int localPosition;

    public int getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(int localPosition) {
        this.localPosition = localPosition;
    }


    public PeriodModel(int level) {
        super(level);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return (ArrayList<VoiceModel>) voiceModelArrayList;
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
        parcel.writeInt(id);
        parcel.writeInt(duration);
        parcel.writeString(background);
        parcel.writeString(Visualizer);
        parcel.writeString(backgroundVolume);
        parcel.writeList((List) voiceModelArrayList);
    }

    protected PeriodModel(Parcel in) {
        super(1);
        id = in.readInt();
        duration = in.readInt();
        background = in.readString();
        Visualizer = in.readString();
        backgroundVolume = in.readString();
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