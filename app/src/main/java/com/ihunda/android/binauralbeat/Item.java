package com.ihunda.android.binauralbeat;

import com.multilevelview.models.RecyclerViewItem;

public class Item extends RecyclerViewItem {

    String text = "";

    String secondText = "";

    protected Item(int level) {
        super(level);
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
