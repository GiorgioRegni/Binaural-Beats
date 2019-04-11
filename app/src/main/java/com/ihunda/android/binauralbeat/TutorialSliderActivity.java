package com.ihunda.android.binauralbeat;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.codemybrainsout.onboarder.utils.ShadowTransformer;
import com.codemybrainsout.onboarder.views.CircleIndicatorView;
import com.ihunda.android.binauralbeat.viz.Image;

public class TutorialSliderActivity extends AppCompatActivity {
    ImageView ivPrev,ivNext;
    CustomViewPager viewPager;
    CircleIndicatorView circleIndicatorView;
    CardView cardViewStart;
    ShadowTransformer shadowTransformer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_slider);
        initialization();
    }

    public void initialization()
    {
        viewPager = (CustomViewPager) findViewById(R.id.pager_tutorial_slider);
        ivPrev = (ImageView)findViewById(R.id.image_tutorial_slider_prev);
        ivNext = (ImageView)findViewById(R.id.image_tutorial_slider_next);
        cardViewStart = (CardView)findViewById(R.id.card_start);
        ivPrev.setVisibility(View.INVISIBLE);
        circleIndicatorView = (CircleIndicatorView)findViewById(R.id.circle_indicator_pager);
        circleIndicatorView.setPageIndicators(6);
        circleIndicatorView.setActiveIndicatorColor(R.color.white);
        circleIndicatorView.setInactiveIndicatorColor(R.color.primary_dark);

        TutorialPagerAdapter tutorialPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tutorialPagerAdapter);
        //TutorialSliderAdapter tutorialSliderAdapter = new TutorialSliderAdapter(this);
       // viewPager.setAdapter(tutorialSliderAdapter);

        viewPager.setAnimationEnabled(true);
        viewPager.setFadeEnabled(true);
        viewPager.setFadeFactor(0.6f);

        circleIndicatorView.setCurrentPage(viewPager.getCurrentItem());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    ivPrev.setVisibility(View.INVISIBLE);
                    ivNext.setVisibility(View.VISIBLE);
                    fadeIn(ivNext);
                    fadeOut(ivPrev,true);
                    hideFinish();
                }else if(position==5){
                    ivNext.setVisibility(View.INVISIBLE);
                    ivPrev.setVisibility(View.VISIBLE);
                    fadeOut(ivNext,true);
                    fadeIn(ivPrev);
                    showFinish();
                }else {
                    ivPrev.setVisibility(View.VISIBLE);
                    ivPrev.setVisibility(View.VISIBLE);
                    fadeIn(ivPrev);
                    fadeIn(ivNext);
                    hideFinish();
                }
                circleIndicatorView.setCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem();
                viewPager.setCurrentItem(pos-1);
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem();
                viewPager.setCurrentItem(pos+1);
            }
        });

        cardViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void fadeOut(final View v, boolean delay) {

        long duration = 0;
        if (delay) {
            duration = 300;
        }

        if (v.getVisibility() != View.INVISIBLE) {
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
            fadeOut.setDuration(duration);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(fadeOut);
        }
    }

    private void fadeIn(final View v) {

        if (v.getVisibility() != View.VISIBLE) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
            fadeIn.setDuration(300);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    v.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(fadeIn);
        }
    }

    private void showFinish() {
        cardViewStart.setVisibility(View.VISIBLE);
        cardViewStart.animate().translationY(0 - dpToPixels(3, this)).setInterpolator(new DecelerateInterpolator()).setDuration(500).start();
    }

    private void hideFinish(boolean delay) {

        long duration = 0;
        if (delay) {
            duration = 250;
        }

        cardViewStart.animate().translationY(cardViewStart.getBottom() + dpToPixels(100, this)).setInterpolator(new AccelerateInterpolator()).setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                cardViewStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    private void hideFinish() {
        hideFinish(true);
    }
    public float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}
