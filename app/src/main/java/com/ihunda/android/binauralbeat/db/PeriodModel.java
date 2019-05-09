package com.ihunda.android.binauralbeat.db;

import com.j256.ormlite.field.DatabaseField;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;

/**
 * Created by kalpit on 9/04/19.
 */
public class PeriodModel extends RecyclerViewItem {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int duration;
    @DatabaseField
    private String background;
    @DatabaseField
    private String viz;
    @DatabaseField
    private String backgroundVolume;
    @DatabaseField
    private ArrayList<VoiceModel> voiceModelArrayList;

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

    public String getViz() {
        return viz;
    }

    public void setViz(String viz) {
        this.viz = viz;
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


}