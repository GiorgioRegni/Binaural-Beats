package com.ihunda.android.binauralbeat.db;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by kalpit on 9/04/19.
 */
public class HistoryModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(long completedTime) {
        this.completedTime = completedTime;
    }

    public Long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(Long dateMillis) {
        this.dateMillis = dateMillis;
    }

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String programName;
    @DatabaseField
    private long completedTime;
    @DatabaseField
    private Long dateMillis;


}
