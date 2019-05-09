package com.ihunda.android.binauralbeat.db;

import com.j256.ormlite.field.DatabaseField;
import com.multilevelview.models.RecyclerViewItem;

/**
 * Created by kalpit on 9/04/19.
 */
public class VoiceModel extends RecyclerViewItem {
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
}
