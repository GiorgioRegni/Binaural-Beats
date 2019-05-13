package com.ihunda.android.binauralbeat.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.multilevelview.models.RecyclerViewItem;

import java.io.Serializable;

/**
 * Created by kalpit on 9/04/19.
 */
public class VoiceModel extends RecyclerViewItem implements Serializable, Parcelable {
    @DatabaseField(canBeNull = true, foreign = true)
    private PeriodModel periodModel;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int freqStart;
    @DatabaseField
    private int freqEnd;
    @DatabaseField
    private int volume;
    @DatabaseField
    private int pitch;

    public int getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(int localPosition) {
        this.localPosition = localPosition;
    }

    private int localPosition;

    public VoiceModel(int level) {
        super(level);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreqStart() {
        return freqStart;
    }

    public void setFreqStart(int freqStart) {
        this.freqStart = freqStart;
    }

    public int getFreqEnd() {
        return freqEnd;
    }

    public void setFreqEnd(int freqEnd) {
        this.freqEnd = freqEnd;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    VoiceModel() {
        super(1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(freqStart);
        parcel.writeInt(freqEnd);
        parcel.writeInt(volume);
        parcel.writeInt(pitch);
    }

    protected VoiceModel(Parcel in) {
        super(1);
        id = in.readInt();
        freqStart = in.readInt();
        freqEnd = in.readInt();
        volume = in.readInt();
        pitch = in.readInt();
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VoiceModel> CREATOR = new Parcelable.Creator<VoiceModel>() {
        @Override
        public VoiceModel createFromParcel(Parcel in) {
            return new VoiceModel(in);
        }

        @Override
        public VoiceModel[] newArray(int size) {
            return new VoiceModel[size];
        }
    };
}
