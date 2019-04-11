package com.ihunda.android.binauralbeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.Toast;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgio on 26/11/16.
 */

public class TutorialActivity extends AhoyOnboarderActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard(getString(R.string.welcome), getString(R.string.tutorial_1_text), R.drawable.tutorial_1);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard(
                getString(R.string.tutorial_2_title),
                getString(R.string.tutorial_2_text),
                R.drawable.tutorial_2);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard(
                getString(R.string.tutorial_3_title),
                getString(R.string.tutorial_3_text),
                R.drawable.tutorial_2);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard(
                getString(R.string.tutorial_4_title),
                getString(R.string.tutorial_4_text),
                R.drawable.tutorial_2);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard(
                getString(R.string.tutorial_5_title),
                getString(R.string.tutorial_5_text),
                R.drawable.tutorial_2);
        AhoyOnboarderCard ahoyOnboarderCard6 = new AhoyOnboarderCard(
                getString(R.string.tutorial_6_title),
                getString(R.string.tutorial_6_text),
                R.drawable.tutorial_1);

        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);
        pages.add(ahoyOnboarderCard5);
        pages.add(ahoyOnboarderCard6);

        for (AhoyOnboarderCard p: pages) {
            p.setBackgroundColor(R.color.black_transparent);
            p.setTitleColor(R.color.white);
            p.setDescriptionColor(R.color.grey_200);
            p.setTitleTextSize(dpToPixels((int) getResources().getDimension(R.dimen.dp_6), this));
            p.setDescriptionTextSize(dpToPixels((int) getResources().getDimension(R.dimen.dp_5), this));
        }

        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.primary_dark);
        colorList.add(R.color.secondary_text);
        colorList.add(R.color.white);

        setColorBackground(colorList);

        setFinishButtonTitle(getString(R.string.tutorial_start));

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        this.finish();
    }
}
