package com.ihunda.android.binauralbeat.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalpit on 9/04/19.
 */
public class PresetModel implements Parcelable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField
    private String author;
    @DatabaseField
    private String periodModelArray;
    private ArrayList<PeriodModel> periodModelArrayList = new ArrayList<PeriodModel>();

    public String getPeriodModelArray() {
        return periodModelArray;
    }

    public void setPeriodModelArray(String periodModelArray) {
        this.periodModelArray = periodModelArray;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<PeriodModel> getPeriodModelArrayList() {
        return periodModelArrayList;
    }

    public void setPeriodModelArrayList(ArrayList<PeriodModel> periodModelArrayList) {
        this.periodModelArrayList = periodModelArrayList;
    }

    public PresetModel() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(author);
        parcel.writeString(periodModelArray);
        parcel.writeList((List) periodModelArrayList);
    }

    protected PresetModel(Parcel in) {
        super();
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        author = in.readString();
        periodModelArray = in.readString();
        periodModelArrayList = new ArrayList<>();
        in.readList((List) periodModelArrayList, PeriodModel.class.getClassLoader());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PresetModel> CREATOR = new Parcelable.Creator<PresetModel>() {
        @Override
        public PresetModel createFromParcel(Parcel in) {
            return new PresetModel(in);
        }

        @Override
        public PresetModel[] newArray(int size) {
            return new PresetModel[size];
        }
    };

}