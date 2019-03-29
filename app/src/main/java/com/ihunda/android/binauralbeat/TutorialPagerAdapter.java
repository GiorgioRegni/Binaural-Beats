package com.ihunda.android.binauralbeat;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;

import com.codemybrainsout.onboarder.utils.ShadowTransformer;

public class TutorialPagerAdapter extends FragmentStatePagerAdapter{

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TutorialFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
