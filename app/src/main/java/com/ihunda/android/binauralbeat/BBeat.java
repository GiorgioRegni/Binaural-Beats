package com.ihunda.android.binauralbeat;

/*
 * @author Giorgio Regni
 * @contact @GiorgioRegni on Twitter
 * http://twitter.com/GiorgioRegni
 *
 * This file is part of Binaural Beats Therapy or BBT.
 *
 *   BBT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   BBT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with BBT.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   BBT project home is at https://github.com/GiorgioRegni/Binaural-Beats
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.plus.PlusShare;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ihunda.android.binauralbeat.db.HistoryModel;
import com.ihunda.android.binauralbeat.db.PeriodModel;
import com.ihunda.android.binauralbeat.db.PresetModel;
import com.ihunda.android.binauralbeat.db.VoiceModel;
import com.ihunda.android.binauralbeat.viz.Aurora;
import com.ihunda.android.binauralbeat.viz.Black;
import com.ihunda.android.binauralbeat.viz.Flash;
import com.ihunda.android.binauralbeat.viz.GLBlack;
import com.ihunda.android.binauralbeat.viz.Hiit;
import com.ihunda.android.binauralbeat.viz.HypnoFlash;
import com.ihunda.android.binauralbeat.viz.HypnoticSpiral;
import com.ihunda.android.binauralbeat.viz.LSD;
import com.ihunda.android.binauralbeat.viz.Leds;
import com.ihunda.android.binauralbeat.viz.Mandelbrot;
import com.ihunda.android.binauralbeat.viz.Morphine;
import com.ihunda.android.binauralbeat.viz.None;
import com.ihunda.android.binauralbeat.viz.Plasma;
import com.ihunda.android.binauralbeat.viz.SpiralDots;
import com.ihunda.android.binauralbeat.viz.Starfield;
import com.ihunda.android.binauralbeat.viz.Starfield3D;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import io.fabric.sdk.android.Fabric;

public class BBeat extends AppCompatActivity implements PurchasesUpdatedListener {

    enum eState {
        START,
        RUNNING,
        END
    }

    enum appState {
        NONE,
        SETUP,
        INPROGRAM
    }

    private static final int MAX_STREAMS = 5;

    public static final float W_DELTA_FREQ = 2.00f;
    public static final float W_THETA_FREQ = 6.00f;
    public static final float W_ALPHA_FREQ = 10.00f;
    public static final float W_BETA_FREQ = 20.00f;
    public static final float W_GAMMA_FREQ = 60.00f;
    public static final float W_MAX_BEAT = 80.00f;

    private static final long ANIM_TIME_MS = 600;

    private LinearLayout mPresetView;
    private LinearLayout mInProgram;
    private View mVizV;
    private FrameLayout mVizHolder;
    private TextView mStatus;
    private ExpandableListView mPresetList;

    private appState state;

    private int soundWhiteNoise;
    private int soundUnity;
    private SoundPool mSoundPool;

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_STARTED = 1;

    private PowerManager mPm;
    private PowerManager.WakeLock mWl;

    private Handler mHandler = new Handler();
    private RunProgram programFSM;
    private long pause_time = -1;

    private Vector<StreamVoice> playingStreams;
    private int playingBackground = -1;

    private SeekBar soundBeatV;
    private float mSoundBeatVolume;
    private SeekBar soundBGV;
    private float mSoundBGVolume;

    private static final String SOURCE_CODE_URL = "http://bit.ly/BBeats";
    private static final String BLOG_URL = "http://bit.ly/BBeatsBlog";
    private static final String HELP_URL = "http://bit.ly/BBeatsHelp";
    private static final String FORUM_URL = "https://plus.google.com/u/1/communities/113832254482827107359";
    private static final String FACEBOOK_URL = "http://www.facebook.com/pages/Binaural-Beat-Therapy/121737064536801";
    private static final String CONTACT_EMAIL = "binaural-beats@ihunda.com";
    private static final String FACEBOOK_INSTALL_URL = "http://bit.ly/BBTFBSHARE";
    private static final String FACEBOOK_SHARE_IMG = "http://i.imgur.com/bG9coHF.png";
    private static final String LOGBBEAT = "BBT-MAIN";
    private static final int NUM_START_BEFORE_DONATE = 2;

    /* All dialogs declaration go here */
    private static final int DIALOG_WELCOME = 1;
    private static final int DIALOG_CONFIRM_RESET = 2;
    private static final int DIALOG_GETTING_INVOLVED = 3;
    private static final int DIALOG_JOIN_COMMUNITY = 4;
    private static final int DIALOG_PROGRAM_PREVIEW = 5;
    private static final int DIALOG_DONATE = 6;

    private static final float DEFAULT_VOLUME = 0.6f;

    private static final float BG_VOLUME_RATIO = 0.4f;

    private static final float FADE_INOUT_PERIOD = 5f;
    private static final float FADE_MIN = 0.6f;

    private static final String PREFS_NAME = "BBT";
    private static final String PREFS_VIZ = "VIZ";
    private static final String PREFS_TUTORIAL = "TUT";
    private static final String PREFS_NUM_STARTS = "NUM_STARTS";

    private VoicesPlayer vp;

    boolean glMode = false;
    boolean vizEnabled = true;
    boolean seenTutorial = false;

    private LinearLayout mGraphVoicesLayout;

    Map<String, ProgramMeta> programs;
    ArrayList<CategoryGroup> groups;

    private long numStarts;

    private Toolbar mToolbar;

    // Stats tracking

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers, storing them all in Application object
     * helps ensure that they are created only once per application instance.
     */
    public enum TrackerName {
        APP_TRACKER,
        // Tracker used only in this app.
        GLOBAL_TRACKER,
        // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    public final String PROPERTY_ID = "UA-76238-16";

    /*
     * Not sure this is the best way to do it but it seems to work
     * Some of the vizualisation need to get pointer to resources to load iamges, sounds, etc...
     *  */
    private static BBeat instance;

    /*
     * Keeps a reference to a selected program only for the purpose of the
     * preview dialog, should always be null when the preview dialog is not
     * displayed
     */
    private static Program _tmp_program_holder;

    /**
     * Called when the activity is first created.
     */

    /**
     * In app purchase objects declaration
     */
    BillingClient mBillingClient;
    boolean mIsBillingServiceConnected = false;
    int mBillingClientResponseCode = 0;

    List<SkuDetails> mProductSkuList;
    SharedPref mSharedPref = SharedPref.getInstance();
    String mDonationLevel = null;
    private int currentHistoryId = -1;
    private long historyTotalTimeElapsed = 0;
    private String historyProgramName = "";
    private DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Facebook */
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.main);

        /* Initialize Fabric and Crashlytics */
        Fabric.with(this, new Crashlytics());

        /* Init sounds */
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mSharedPref.initialize(this);
        //in app purchase billing client initialization
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mIsBillingServiceConnected = false;

        /*
         * Sets up power management, device should not go to sleep during a program
         */
        mPm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWl = mPm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BBTherapy:");

        /* Setup all buttons */
        Button b;

        b = (Button) findViewById((R.id.bmUserGuide));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoHelp();
            }
        });

        b = (Button) findViewById((R.id.donateButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_DONATE);
            }
        });

        b = (Button) findViewById((R.id.historyButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(BBeat.this, HistoryActivity.class));
            }
        });

        b = (Button) findViewById((R.id.joinCommunityButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoForum();
            }
        });

        mGraphVoicesLayout = (LinearLayout) findViewById(R.id.graphVoices);

        pause_time = -1;

        /* Set up volume bar */
        soundBeatV = (SeekBar) findViewById((R.id.soundVolumeBar));
        soundBeatV.setMax(100);
        mSoundBeatVolume = DEFAULT_VOLUME;
        soundBeatV.setProgress((int) (mSoundBeatVolume * 100));
        soundBeatV.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mSoundBeatVolume = ((float) progress) / 100.f;
                resetAllVolumes();
            }
        });
        /* Set up background volume bar */
        soundBGV = (SeekBar) findViewById((R.id.soundBGVolumeBar));
        soundBGV.setMax(100);
        mSoundBGVolume = mSoundBeatVolume * BG_VOLUME_RATIO;
        soundBGV.setProgress((int) (mSoundBGVolume * 100));
        soundBGV.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,

                                          boolean fromUser) {

                mSoundBGVolume = ((float) progress) / 100.f;

                resetAllVolumes();
            }
        });

        mInProgram = (LinearLayout) findViewById(R.id.inProgramLayout);
        mPresetView = (LinearLayout) findViewById(R.id.presetLayout);

        mVizHolder = (FrameLayout) findViewById(R.id.VisualizationView);
        mStatus = (TextView) findViewById(R.id.Status);

        // Set a static pointer to this instance so that vizualisation can access it
        setInstance(this);

        /* Init Tracker */
        Tracker tra = getTracker(TrackerName.APP_TRACKER);
        // Enable Advertising Features.
        tra.enableAdvertisingIdCollection(true);

        mPresetList = (ExpandableListView) findViewById(R.id.presetListView);
        //final List<String> programs = new ArrayList<String>(DefaultProgramsBuilder.getProgramMethods(this).keySet());

        programs = DefaultProgramsBuilder.getProgramMethods(this);
        groups = new ArrayList<CategoryGroup>();

        new LoadAdapter().execute();

        mPresetList.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                /* Do nothing for now */
                return false;
            }
        });

        mPresetList.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (groups.get(groupPosition).getObjets().get(childPosition).getMethod() != null) {
                    selectProgram(groups.get(groupPosition).getObjets().get(childPosition));
                } else {
                    selectCreateProgram(groups.get(groupPosition).getProgram().get(childPosition));
                }
                return true;
            }
        });

        LayoutInflater inflater = getLayoutInflater();

        mPresetList.setGroupIndicator(getResources().getDrawable(R.drawable.empty));


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(drawerLayout, mToolbar);

        // Wire Navigation Drawer Buttons

        ImageView imb = (ImageView) findViewById((R.id.NDLogo));
        imb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                _show_tutorial();
            }
        });

        b = (Button) findViewById((R.id.NDUserGuide));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoHelp();
            }
        });

        b = (Button) findViewById((R.id.NDCommunityButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoForum();
            }
        });

        b = (Button) findViewById((R.id.NDFacebookLikeButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoFacebook();
            }
        });

        b = (Button) findViewById((R.id.NDDonateButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_DONATE);
            }
        });

        b = (Button) findViewById((R.id.NDPresetBuilderButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoPresetBuilder();
                    }
                }, 300);
            }
        });

        b = (Button) findViewById((R.id.NDRateButton));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoMarket();
            }
        });

        b = (Button) findViewById((R.id.NDGettingInvolved));
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_GETTING_INVOLVED);
            }
        });

        _load_config();

        if (!seenTutorial)
            _show_tutorial();

        initSounds();

        _updateDonationLevel();

        state = appState.NONE;
        goToState(appState.SETUP);

    }

    private void _show_tutorial() {
        seenTutorial = true;
        _save_config();

        Intent intent = new Intent(this, TutorialSliderActivity.class);
        startActivity(intent);
    }

    /*
     * Takes the current setup and saves it to preference for next start
     */
    private void _save_config() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREFS_VIZ, vizEnabled);
        editor.putLong(PREFS_NUM_STARTS, numStarts);
        editor.putBoolean(PREFS_TUTORIAL, seenTutorial);
        editor.commit();
    }

    private void _load_config() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        vizEnabled = settings.getBoolean(PREFS_VIZ, true);
        numStarts = settings.getLong(PREFS_NUM_STARTS, 0);
        seenTutorial = settings.getBoolean(PREFS_TUTORIAL, false);
    }

    void initSounds() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }

        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundWhiteNoise = mSoundPool.load(this, R.raw.whitenoise, 1);
        soundUnity = mSoundPool.load(this, R.raw.unity, 1);

        playingStreams = new Vector<StreamVoice>(MAX_STREAMS);
        playingBackground = -1;
    }

    void startVoicePlayer() {
        if (vp == null) {
            vp = new VoicesPlayer();
            vp.start();
        }
    }

    void stopVoicePlayer() {
        try {
            vp.shutdown();
        } catch (Exception e) {
            // ignore
        }

        vp = null;
    }

    @Override
    protected void onStart() {
        Log.v(LOGBBEAT, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v(LOGBBEAT, "onStop");

        if (!isInProgram()) {
            stopVoicePlayer();
        }

        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // ask for confirmation during workout
            if (state == appState.INPROGRAM) {
                showDialog(DIALOG_CONFIRM_RESET);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (state == appState.INPROGRAM) {
            inflater.inflate(R.menu.inprogram, menu);
        } else {
            inflater.inflate(R.menu.insetup, menu);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop: {
                showDialog(DIALOG_CONFIRM_RESET);
                return true;
            }
            case R.id.togglegraphics:
                if (!vizEnabled)
                    item.setIcon(R.drawable.ic_visibility_white);
                else
                    item.setIcon(R.drawable.ic_visibility_off_white);

                setGraphicsEnabled(!vizEnabled);
                break;
            // In program
            case R.id.pause:
                pauseOrResume();
                if (pause_time == -1)
                    item.setIcon(android.R.drawable.ic_media_pause);
                else
                    item.setIcon(android.R.drawable.ic_media_play);
                break;
        }
        return false;
    }

    private void goToState(appState newState) {
        switch (state) {
            case NONE:
                mInProgram.setVisibility(View.GONE);
                mPresetView.setVisibility(View.GONE);
                break;
            case INPROGRAM:

                if (mWl.isHeld()) {
                    mWl.release();
                }

                mVizHolder.removeAllViews();
                mVizV = null;

                runGoneAnimationOnView(mInProgram);
                _cancel_all_notifications();

                stopVoicePlayer();

                /* Reinit all sounds */
                initSounds();

                /* Check if its time to show a donate dialog */
                if (!_isDonated())
                    if (numStarts % NUM_START_BEFORE_DONATE == NUM_START_BEFORE_DONATE - 1) {
                        showDialog(DIALOG_DONATE);
                    }

                break;
            case SETUP:
                runGoneAnimationOnView(mPresetView);
                break;
            default:
                break;
        }

        state = newState;
        switch (state) {
            case SETUP:
                _track_screen("SETUP");
                runComeBackAnimationOnView(mPresetView);
                mPresetList.invalidate();
                mVizHolder.setVisibility(View.GONE);
                try {
                    if (currentHistoryId != -1) {
                        ArrayList<HistoryModel> arrayList = (ArrayList<HistoryModel>) ((BBeatApp) getApplicationContext()).getDbHelper().get(HistoryModel.class, "" + currentHistoryId);
                        HistoryModel historyModel = arrayList.get(0);
                        historyModel.setCompletedTime(historyModel.getCompletedTime() + new Date().getTime() - historyTotalTimeElapsed);
                        ((BBeatApp) getApplicationContext()).getDbHelper().fillObject(HistoryModel.class, historyModel);
                    }
                    historyTotalTimeElapsed = 0;
                    currentHistoryId = -1;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case INPROGRAM:
                _track_screen("INPROGRAM");
                // Track number of usage
                numStarts++;
                _save_config();

                // Start voice player thread
                startVoicePlayer();

                // Acquire power management lock
                if (vizEnabled) {
                    mWl.acquire();
                }

                _start_notification(programFSM.getProgram().getName());
                runComeBackAnimationOnView(mInProgram);
                mVizHolder.setVisibility(View.VISIBLE);

                glMode = programFSM.pR.doesUseGL();
                if (glMode) {
                    mVizV = new GLVizualizationView(getBaseContext());
                } else {
                    mVizV = new CanvasVizualizationView(getBaseContext());
                }
                mVizHolder.addView(mVizV);

                // JENLA managed state of pause button mPlayPause.setChecked(true);
                pause_time = -1;

                try {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setProgramName(historyProgramName);
                    historyModel.setCompletedTime(0);
                    historyModel.setDateMillis(new Date().getTime());
                    currentHistoryId = ((BBeatApp) getApplicationContext()).getDbHelper().insertObject(HistoryModel.class, historyModel);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                historyTotalTimeElapsed = new Date().getTime();
                break;
        }
        invalidateOptionsMenu(); // Force re-evaluation of option menu
    }

    public boolean isInProgram() {
        if (state == appState.INPROGRAM) {
            return true;
        }

        return false;
    }

    public boolean isPaused() {
        if (pause_time > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void pauseOrResume() {
        if (state == appState.INPROGRAM) {
            if (pause_time > 0) {
                long delta = _getClock() - pause_time;
                programFSM.catchUpAfterPause(delta);
                pause_time = -1;
                historyTotalTimeElapsed = new Date().getTime();
                unmuteAll();
            } else {
                /* This is a pause time */
                pause_time = _getClock();
                try {
                    if (currentHistoryId != -1) {
                        ArrayList<HistoryModel> arrayList = (ArrayList<HistoryModel>) ((BBeatApp) getApplicationContext()).getDbHelper().get(HistoryModel.class, "" + currentHistoryId);
                        HistoryModel historyModel = arrayList.get(0);
                        historyModel.setCompletedTime(historyModel.getCompletedTime() + new Date().getTime() - historyTotalTimeElapsed);
                        ((BBeatApp) getApplicationContext()).getDbHelper().fillObject(HistoryModel.class, historyModel);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                muteAll();
            }
        }
    }

    private void setGraphicsEnabled(boolean on) {
        if (state == appState.INPROGRAM) {
            if (vizEnabled && on == false) {
                // Disable Viz
                Period p = programFSM.getCurrentPeriod();
                Visualization v;

                if (((Object) mVizV).getClass() == GLVizualizationView.class) {
                    v = new GLBlack();
                } else {
                    v = new Black();
                }

                ((VizualisationView) mVizV).stopVisualization();
                ((VizualisationView) mVizV).startVisualization(v, p.getLength());
                ((VizualisationView) mVizV).setFrequency(p.getVoices().get(0).freqStart);
                vizEnabled = false;

                if (mWl.isHeld()) {
                    mWl.release();
                }

                ToastText(R.string.graphics_off);
            } else if (!vizEnabled && on == true) {
                // Enable viz
                Period p = programFSM.getCurrentPeriod();
                ((VizualisationView) mVizV).stopVisualization();
                ((VizualisationView) mVizV).startVisualization(p.getV(), p.getLength());
                ((VizualisationView) mVizV).setFrequency(p.getVoices().get(0).freqStart);
                vizEnabled = true;

                if (mWl.isHeld() == false) {
                    mWl.acquire();
                }

                ToastText(R.string.graphics_on);
            }
        }
        _save_config();
    }

    @Override
    protected void onDestroy() {
        panic();
        _cancel_all_notifications();
        try {
            if (currentHistoryId != -1) {
                ArrayList<HistoryModel> arrayList = (ArrayList<HistoryModel>) ((BBeatApp) getApplicationContext()).getDbHelper().get(HistoryModel.class, "" + currentHistoryId);
                HistoryModel historyModel = arrayList.get(0);
                historyModel.setCompletedTime(historyModel.getCompletedTime() + new Date().getTime() - historyTotalTimeElapsed);
                ((BBeatApp) getApplicationContext()).getDbHelper().fillObject(HistoryModel.class, historyModel);
            }
            historyTotalTimeElapsed = 0;
            currentHistoryId = -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.v(LOGBBEAT, "onPause");
        super.onPause();

        // Facebook
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        Log.v(LOGBBEAT, "onResume");
        super.onResume();

        // Facebook
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DIALOG_WELCOME: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.welcome_text)
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                return alert;
            }

            case DIALOG_CONFIRM_RESET: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_reset)
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                BBeat.this.stopProgram();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                return alert;
            }

            case DIALOG_GETTING_INVOLVED:
            case DIALOG_JOIN_COMMUNITY: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.getting_involved_dialog)
                        .setCancelable(true)
                        .setPositiveButton(R.string.contact, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                emailAuthor(getString(R.string.app_name), getString(R.string.share_text));
                            }
                        }).setNeutralButton(R.string.donate, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        showDialog(DIALOG_DONATE);
                    }
                });
                AlertDialog alert = builder.create();
                return alert;
            }

            case DIALOG_PROGRAM_PREVIEW:
                final Program p = _tmp_program_holder;
                if (p == null) {
                    return null;
                }

                int length = p.getLength();

                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.program_preview_dialog, null);

                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(view);

                Button close = (Button) view.findViewById(R.id.p_back);
                close.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        removeDialog(DIALOG_PROGRAM_PREVIEW);
                    }
                });
                Button start = (Button) view.findViewById(R.id.p_start);
                start.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        historyProgramName = p.getName();
                        StartPreviouslySelectedProgram();
                        removeDialog(DIALOG_PROGRAM_PREVIEW);
                    }
                });

                ((TextView) view.findViewById(R.id.p_name)).setText(p.getName());
                ((TextView) view.findViewById(R.id.p_descr)).setText(p.getDescription());
                ((TextView) view.findViewById(R.id.p_author)).setText(p.getAuthor());
                ((TextView) view.findViewById(R.id.p_totaltime)).setText(String.format(" %sh%smin.",
                        formatTimeNumberwithLeadingZero(length / 60 / 60),
                        formatTimeNumberwithLeadingZero((length / 60) % 60)));

                return dialog;

            case DIALOG_DONATE: {
                _track_ui_click("DONATE", "DIALOG");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.donate_text)
                        .setCancelable(true)
                        .setPositiveButton(R.string.donate, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getAllProductsList();

                            }
                        }).setNeutralButton(R.string.share_facebook, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeDialog(DIALOG_DONATE);
                        displayFacebookShare();
                    }
                });
                /*.setNegativeButton(R.string.like, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeDialog(DIALOG_DONATE);
                        gotoFacebook();
                    }
                });;*/

                AlertDialog alert = builder.create();
                return alert;

            }
        }

        return null;
    }

    ;

    private void panic() {
        // Stop all sounds
        for (StreamVoice v : playingStreams) {
            mSoundPool.stop(v.streamID);
        }
        playingStreams.clear();
        if (vp != null) {
            vp.stopVoices();
        }
    }

    private void _start_notification(String programName) {
        //Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        // Show controls on lock screen even when user hides sensitive content.
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setContentTitle(getString(R.string.notif_started))
                        .setContentText(getString(R.string.notif_descr, programName))
                        .setOngoing(true);

        Intent notificationIntent = this.getIntent(); //new Intent(this, hiit.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        mBuilder.setContentIntent(contentIntent);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_STARTED, mBuilder.build());
    }

//    private void _start_notification(String programName) {
//        Notification notification = new Notification(R.drawable.icon, getString(R.string.notif_started), 0); // JENLA
//
//        Context context = getApplicationContext();
//        CharSequence contentTitle = getString(R.string.notif_started);
//        CharSequence contentText = getString(R.string.notif_descr, programName);
//        Intent notificationIntent = this.getIntent(); //new Intent(this, hiit.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
//        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
//
//        mNotificationManager.notify(NOTIFICATION_STARTED, notification);
//    }

    private void _cancel_all_notifications() {
        mNotificationManager.cancelAll();
    }

    private void selectProgram(ProgramMeta pm) {
        if (programFSM != null) {
            programFSM.stopProgram();
        }

        Program p = DefaultProgramsBuilder.getProgram(pm);
        _tmp_program_holder = p;

        _track_ui_click(p.getName(), "select");

        showDialog(DIALOG_PROGRAM_PREVIEW);
    }

    private void selectCreateProgram(Program program) {
        if (programFSM != null) {
            programFSM.stopProgram();
        }

        Program p = program;
        _tmp_program_holder = p;

        _track_ui_click(p.getName(), "select");

        showDialog(DIALOG_PROGRAM_PREVIEW);
    }

    private void StartPreviouslySelectedProgram() {
        Program p = _tmp_program_holder;
        _tmp_program_holder = null;

        _track_ui_click(p.getName(), "start");

        mToolbar.setTitle(p.getName());

        programFSM = new RunProgram(p, mHandler);
        goToState(appState.INPROGRAM);
    }

    private void stopProgram() {
        if (programFSM != null) {
            programFSM.stopProgram();
            programFSM = null;
        }
        panic();

        mToolbar.setTitle(getString(R.string.app_name));

        goToState(appState.SETUP);

    }

    int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) {
        int id = mSoundPool.play(soundID, leftVolume * mSoundBeatVolume, rightVolume * mSoundBeatVolume,
                priority, loop, rate);

        /*
         * Record all playing stream ids to be able to stop sound on pause/panic
         */
        playingStreams.add(new StreamVoice(id, leftVolume, rightVolume, loop, rate));
        if (playingStreams.size() > MAX_STREAMS) {
            StreamVoice v = playingStreams.remove(0);
            mSoundPool.stop(v.streamID);
        }

        return id;
    }

    void stop(int soundID) {
        mSoundPool.stop(soundID);
        playingStreams.removeElement(new Integer(soundID));
    }

    /**
     * Loop through all playing voices and set regular volume back
     */
    void resetAllVolumes() {
        if (playingStreams != null &&
                mSoundPool != null) {
            for (StreamVoice v : playingStreams) {
                if (v.streamID == playingBackground) {
                    mSoundPool.setVolume(v.streamID, v.leftVol * mSoundBGVolume, v.rightVol * mSoundBGVolume);
                } else {
                    mSoundPool.setVolume(v.streamID, v.leftVol * mSoundBeatVolume, v.rightVol * mSoundBeatVolume);
                }
            }
        }
        if (vp != null) {
            vp.setVolume(mSoundBeatVolume);
        }
    }

    /**
     * Loop through all playing voices and lower volume to 0 but do not stop
     */
    void muteAll() {
        if (playingStreams != null &&
                mSoundPool != null) {
            for (StreamVoice v : playingStreams) {
                mSoundPool.setVolume(v.streamID, 0, 0);
            }
        }
        vp.setVolume(0);
    }

    /**
     * Loop through all playing voices and set volume back
     */
    void unmuteAll() {
        resetAllVolumes();
    }

    private void playBackgroundSample(SoundLoop background, float vol) {

        switch (background) {
            case WHITE_NOISE:
                playingBackground = play(soundWhiteNoise, vol, vol, 2, -1, 1.0f);
                break;
            case UNITY:
                playingBackground = play(soundUnity, vol, vol, 2, -1, 1.0f);
                break;
            case NONE:
                playingBackground = -1;
                break;
            default:
                playingBackground = -1;
                break;
        }

        if (playingBackground != -1) {
            mSoundPool.setVolume(playingBackground, vol * mSoundBGVolume, vol * mSoundBGVolume);
        }
    }

    private void stopBackgroundSample() {
        if (playingBackground != -1) {
            stop(playingBackground);
        }
        playingBackground = -1;
    }

    /**
     * Go through a list of voices and start playing them with their start frequency
     *
     * @param voices list of voices to play
     */
    protected void playVoices(ArrayList<BinauralBeatVoice> voices) {
        vp.playVoices(voices);
        vp.setVolume(mSoundBeatVolume);
    }

    /**
     * @return beat frequency of first voice
     */
    protected float skewVoices(ArrayList<BinauralBeatVoice> voices, float pos, float length, boolean doskew) {
        int i = 0;
        float res = -1;

        float freqs[] = new float[voices.size()];
        for (BinauralBeatVoice v : voices) {
            float ratio = (v.freqEnd - v.freqStart) / length;

            if (res == -1) {
                res = ratio * pos + v.freqStart; // Only set res for the first voice
            }

            freqs[i] = res;

            i++;
        }
        if (doskew) {

            float fade_period = Math.min(FADE_INOUT_PERIOD / 2, length / 2);

            if (length < FADE_INOUT_PERIOD) {
                vp.setFade(1f);
            } else if (pos < fade_period) {
                vp.setFade(FADE_MIN + pos / fade_period * (1f - FADE_MIN));
            } else if (length - pos < fade_period) {
                float fade = FADE_MIN + (length - pos) / fade_period * (1f - FADE_MIN);
                vp.setFade(fade);
            } else {
                vp.setFade(1f);
            }

            vp.setFreqs(freqs);
        }

        return res;
    }

    /**
     * Go through all currently running beat voices and stop them
     */
    protected void stopAllVoices() {
        vp.stopVoices();
    }

    class RunProgram implements Runnable {

        private static final long TIMER_FSM_DELAY = 1000 / 20;

        private static final int GRAPH_VOICE_VIEW_PAST = 60;
        private static final int GRAPH_VOICE_SPAN = 600;
        private static final int GRAPH_VOICE_UPDATE = 5;

        private Program pR;
        private Iterator<Period> periodsIterator;
        private Period currentPeriod;
        private long cT; // current Period start time
        private long startTime;
        private long programLength;
        private String sProgramLength;
        private String formatString;
        private String format_INFO_TIMING_MIN_SEC;
        private long oldDelta; // Utilized to reduce the amount of redraw for the program legend
        private eState s;
        private Handler h;
        LineGraphView graphView;

        private long _last_graph_update;

        public RunProgram(Program pR, Handler h) {
            this.pR = pR;
            this.h = h;

            programLength = pR.getLength();
            sProgramLength = getString(R.string.time_format,
                    formatTimeNumberwithLeadingZero((int) programLength / 60),
                    formatTimeNumberwithLeadingZero((int) programLength % 60));
            formatString = getString(R.string.info_timing);
            format_INFO_TIMING_MIN_SEC = getString(R.string.time_format_min_sec);
            startTime = _getClock();
            oldDelta = -1;
            _last_graph_update = 0;

            s = eState.START;

            h.postDelayed(this, TIMER_FSM_DELAY);
        }

        public Period getCurrentPeriod() {
            return currentPeriod;
        }

        public void stopProgram() {
            stopAllVoices();
            endPeriod();
            h.removeCallbacks(this);
        }

        private void startPeriod(Period p) {
            if (vizEnabled) {
                ((VizualisationView) mVizV).startVisualization(p.getV(), p.getLength());
            } else {
                Visualization v;
                if (((Object) mVizV).getClass() == GLVizualizationView.class) {
                    v = new GLBlack();
                } else {
                    v = new Black();
                }
                ((VizualisationView) mVizV).startVisualization(v, p.getLength());
            }

            ((VizualisationView) mVizV).setFrequency(p.getVoices().get(0).freqStart);
            playVoices(p.voices);
            vp.setFade(FADE_MIN);
            playBackgroundSample(p.background, p.getBackgroundvol());

            Log.v(LOGBBEAT, String.format("New Period - duration %d", p.length));
        }

        private void inPeriod(long now, Period p, float pos) {
            long delta = (now - startTime) / 50; // Do not refresh too often

            float freq = skewVoices(p.voices, pos, p.length, oldDelta != delta);

            ((VizualisationView) mVizV).setFrequency(freq);
            ((VizualisationView) mVizV).setProgress(pos);

            if (oldDelta != delta) {
                oldDelta = delta;
                delta = delta / 20; // Down to seconds
                mStatus.setText(String.format(formatString,
                        freq,
                        formatTimeNumberwithLeadingZero((int) delta / 60),
                        formatTimeNumberwithLeadingZero((int) delta % 60)
                        )
                                +
                                sProgramLength
                );

                updatePeriodGraph((now - startTime) / 1000);
            }
        }

        private void endPeriod() {
            //stopAllVoices();
            stopBackgroundSample();
            ((VizualisationView) mVizV).stopVisualization();
        }

        public void catchUpAfterPause(long delta) {
            cT += delta;
            startTime += delta;
        }

        public void run() {
            long now = _getClock();

            switch (s) {
                case START:
                    s = eState.RUNNING;
                    periodsIterator = pR.getPeriodsIterator();
                    cT = now;
                    drawPeriodGraph();
                    nextPeriod();
                    break;

                case RUNNING:
                    if (isPaused()) {
                        break;
                    }

                    float pos = (now - cT) / 1000f;

                    if (pos > currentPeriod.length) {
                        endPeriod();

                        // Current period is over
                        if (!periodsIterator.hasNext()) {
                            // Finished
                            s = eState.END;
                        } else {
                            // this is a new period
                            cT = now;
                            nextPeriod();
                        }
                    } else {
                        /**
                         * In the middle of current period, adjust each beat voice
                         */
                        inPeriod(now, currentPeriod, pos);
                    }
                    break;

                case END:
                    BBeat.this.stopProgram();
                    return;
            }

            h.postDelayed(this, TIMER_FSM_DELAY);
        }

        private void nextPeriod() {
            currentPeriod = periodsIterator.next();
            startPeriod(currentPeriod);
        }

        public Program getProgram() {
            return pR;
        }

        private void updatePeriodGraph(long now) {
            // update viewport

            if (now >= _last_graph_update + GRAPH_VOICE_UPDATE) {
                int viewstart = 0;
                _last_graph_update = now;

                if (GRAPH_VOICE_SPAN < programLength) {
                    viewstart = (int) Math.max(0, now - GRAPH_VOICE_VIEW_PAST);
                }
                int viewsize = GRAPH_VOICE_SPAN;

                if (graphView != null) {
                    graphView.setDrawBackground(true);
                    graphView.setDrawBackgroundLimit(now);
                    graphView.setViewPort(viewstart, viewsize);
                }
            }
        }

        private void drawPeriodGraph() {

            Iterator<Period> iP = pR.getPeriodsIterator();
            int numPeriods = pR.getNumPeriods();
            GraphViewData data[] = new GraphViewData[numPeriods * 2];

            int i = 0;
            int cursor = 0;
            double maxFreq = 0;

            while (iP.hasNext()) {
                Period cP = iP.next();

                data[i++] = new GraphViewData(cursor + 0.01, cP.getMainBeatStart());
                cursor += cP.getLength();
                data[i++] = new GraphViewData(cursor, cP.getMainBeatEnd());

                maxFreq = Math.max(maxFreq, cP.getMainBeatStart());
                maxFreq = Math.max(maxFreq, cP.getMainBeatEnd());
            }

            GraphViewSeries voiceSeries = new GraphViewSeries(data);

            graphView = new LineGraphView(
                    BBeat.this // context
                    , "Beat frequency" // heading
            ) {
                @Override
                protected String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        return String.format(format_INFO_TIMING_MIN_SEC,
                                formatTimeNumberwithLeadingZero((int) value / 60),
                                formatTimeNumberwithLeadingZero((int) value % 60));
                    } else {
                        return String.format("%.1f", value);
                    }
                }
            };
            graphView.addSeries(voiceSeries); // data

            int viewstart = 0;
            int viewsize = (int) Math.min(programLength, GRAPH_VOICE_SPAN);

            graphView.setManualYAxisBounds(((int) Math.ceil(maxFreq)), 0);

            graphView.setViewPort(viewstart, viewsize);
            graphView.setScrollable(true);
            // optional - activate scaling / zooming
            //graphView.setScalable(true);

            graphView.setDrawBackground(false);

            mGraphVoicesLayout.removeAllViews();
            mGraphVoicesLayout.addView(graphView);
        }
    }

    public Animation runGoneAnimationOnView(View target) {
        Animation animation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        animation.setDuration(ANIM_TIME_MS);
        final View mTarget = target;
        animation.setAnimationListener(new AnimationListener() {

            public void onAnimationEnd(Animation animation) {
                mTarget.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        target.startAnimation(animation);

        return animation;
    }

    public Animation runComeBackAnimationOnView(View target) {
        Animation animation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        animation.setDuration(ANIM_TIME_MS);
        final View mTarget = target;

        mTarget.setVisibility(View.VISIBLE); // to be compatible with Android 1.5

        animation.setAnimationListener(new AnimationListener() {

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        target.startAnimation(animation);

        return animation;
    }

    private String formatTimeNumberwithLeadingZero(int t) {
        if (t > 9) {
            return String.format("%2d", t);
        } else {
            return String.format("0%1d", t);
        }
    }

    private void gotoBlog() {
        gotoURL(BLOG_URL);
    }

    @SuppressWarnings("unused")
    private void gotoSourceCode() {
        gotoURL(SOURCE_CODE_URL);
    }

    private void gotoForum() {
        gotoURL(FORUM_URL);
    }

    private void gotoFacebook() {
        gotoURL(FACEBOOK_URL);
    }

    private void gotoHelp() {
    	/*Intent i = new Intent(this, Comments.class);
    	i.putExtra("ID", "yoyoma");
    	startActivity(i);*/
        gotoURL(HELP_URL);
    }

    private void gotoMarket() {
        gotoURL("market://details?id=com.ihunda.android.binauralbeat");
    }

    private void gotoPresetBuilder() {
        Intent i = new Intent(this, PresetListActivity.class);
        startActivity(i);
    }

    private void gotoURL(String URL) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(URL));
            startActivity(i);
        } catch (Exception e) {

        }
    }

    public void emailAuthor(String subject, String text) {
        String aEmailList[] = {CONTACT_EMAIL};

        composeEmail(aEmailList, subject, text);
    }

    private void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private static void setInstance(BBeat instance) {
        BBeat.instance = instance;
    }

    public static BBeat getInstance() {
        return instance;
    }

    public String readRawTextFile(int resId) {
        InputStream inputStream = this.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    private void ToastText(int id) {
        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show();
    }

    public class DonationsConfiguration {

        public static final String TAG = "Donations";

        public static final boolean DEBUG = false;

    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : null;
			/*: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
							: analytics.newTracker(R.xml.ecommerce_tracker);*/

            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    private void _track_ui_click(String what, String cat) {
        Tracker t = getTracker(TrackerName.APP_TRACKER);
        if (t == null) {
            return;
        }

        t.send(new HitBuilders.EventBuilder()
                .setCategory(cat)
                .setAction(what)
                .setLabel("click")
                .setValue(1)
                .build());
    }

    private void _track_ui_click(String what) {
        _track_ui_click(what, "UI");
    }

    private void _track_time(String what, long timems) {
        Tracker t = getTracker(TrackerName.APP_TRACKER);
        if (t == null) {
            return;
        }

        // Build and send timing.
        t.send(new HitBuilders.TimingBuilder()
                .setCategory("Time")
                .setValue(timems)
                .setVariable(what)
                .setLabel("ms")
                .build());
    }

    private void _track_screen(String screenName) {
        Tracker t = getTracker(TrackerName.APP_TRACKER);
        if (t == null) {
            return;
        }

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void ToastText(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private boolean displayFacebookShare() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareDialog shareDialog = new ShareDialog(this);
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Binaural Beats Therapy App")
                    .setContentDescription("Already approved by hundred thousands of people, a very powerful self-improvement, brain enhancement and stress-relief app.")
                    .setContentUrl(Uri.parse(FACEBOOK_INSTALL_URL))
                    .setImageUrl(Uri.parse(FACEBOOK_SHARE_IMG))
                    .build();

            shareDialog.show(linkContent);

            ToastText("Opening facebook dialog...");
            _track_ui_click("FACEBOOK_SHARE_OK");

            return true;
        }
        // If the Facebook app is installed and we can present the share dialog
        else {
            ToastText("Couldn't open facebook dialog...");
            _track_ui_click("FACEBOOK_SHARE_NOK");
            return false;
        }
    }

    private boolean displayGooglePlusShare() {
        PlusShare.Builder builder = new PlusShare.Builder(this);

        // Set call-to-action metadata.
        builder.addCallToAction(
                "CREATE_ITEM", /** call-to-action button label */
                Uri.parse("http://plus.google.com/pages/create"), /** call-to-action url (for desktop use) */
                "/pages/create" /** call to action deep-link ID (for mobile use), 512 characters or fewer */);

        // Set the content url (for desktop use).
        builder.setContentUrl(Uri.parse(FORUM_URL));

        // Set the target deep-link ID (for mobile use).
        //builder.setContentDeepLinkId("/pages/",
        //        null, null, null);

        // Set the share text.
        builder.setText("Create your Google+ Page too!");

        startActivityForResult(builder.getIntent(), 0);

        return true;
    }

    private long _getClock() {
        return SystemClock.elapsedRealtime();
    }


    private boolean _isDonated() {
        return (mDonationLevel != null);
    }

    private void _updateDonationLevel() {
        _updateDonationLevel(false);
    }

    private void _updateDonationLevel(boolean noNetwork) {
        mDonationLevel = null;

        String sku = mSharedPref.getString(AppConstants.DONATIONPURCHASESKU);

        if (noNetwork == false && (sku == "" || sku == null)) {

            executeBillingServiceRequest(new Runnable() {
                @Override
                public void run() {
                    // try to sync up purchase history of that user
                    Purchase.PurchasesResult purchasesRes = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    if (purchasesRes.getResponseCode() == BillingClient.BillingResponse.OK) {
                        List<Purchase> purchasesList = purchasesRes.getPurchasesList();
                        boolean hasAPurchase = false;
                        for (Purchase purchase : purchasesList) {
                            //String purchaseToken = purchase.getPurchaseToken();
                            String purchaseSku = purchase.getSku();
                            //long purchaseTime = purchase.getPurchaseTime();
                            mSharedPref.putData(AppConstants.DONATIONPURCHASESKU, purchaseSku);
                            hasAPurchase = true;
                        }
                        // This recovers a purchase when an user switch phone
                        // or reinstall the app
                        if (hasAPurchase)
                            _updateDonationLevel(true);
                    }
                }
            }, false);

        }

        switch (sku) {
            case "don_10":
                mDonationLevel = getString(R.string.don_10_level);
                break;
            case "don_50":
                mDonationLevel = getString(R.string.don_50_level);
                break;
            case "don_100":
                mDonationLevel = getString(R.string.don_100_level);
                break;
        }

        if (mDonationLevel != null) {
            Button b;
            b = (Button) findViewById((R.id.donateButton));
            b.setText(mDonationLevel);
            b = (Button) findViewById((R.id.NDDonateButton));
            b.setText(mDonationLevel);
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        //after payment success
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                //String purchaseToken = purchase.getPurchaseToken();
                String purchaseSku = purchase.getSku();
                //long purchaseTime = purchase.getPurchaseTime();
                mSharedPref.putData(AppConstants.DONATIONPURCHASESKU, purchaseSku);
            }
            _updateDonationLevel();
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            ToastText(R.string.DONATION_USER_CANCELED);
        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            // Handle an error caused by a user cancelling the purchase flow.
            ToastText(R.string.DONATION_ALREADY_OWNED);
        } else {
            // Handle any other error codes.
            ToastText(getString(R.string.DONATION_OTHER_ERROR) + " " + new Integer(responseCode).toString());
        }
    }

    private void startBillingServiceConnection(final Runnable executeOnSuccess, final boolean toastOnError) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int billingResponseCode) {
                Log.d(LOGBBEAT, "Billing Setup finished. Response code: " + billingResponseCode);

                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    mIsBillingServiceConnected = true;
                    if (executeOnSuccess != null) {
                        synchronized (BBeat.this) {
                            executeOnSuccess.run();
                        }
                    }
                } else {
                    if (toastOnError == true)
                        ToastText(R.string.DONATION_CONNECTION_ERROR);
                }
                mBillingClientResponseCode = billingResponseCode;
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsBillingServiceConnected = false;
            }
        });
    }

    private void executeBillingServiceRequest(Runnable runnable) {
        executeBillingServiceRequest(runnable, true);
    }

    private void executeBillingServiceRequest(Runnable runnable, boolean toastOnError) {
        if (mIsBillingServiceConnected) {
            synchronized (BBeat.this) {
                runnable.run();
            }
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startBillingServiceConnection(runnable, toastOnError);
        }
    }

    public void getAllProductsList() {
        executeBillingServiceRequest(new Runnable() {
            @Override
            public void run() {

                List<String> skuList = new ArrayList<>();
                skuList.add("don_10");
                skuList.add("don_50");
                skuList.add("don_100");
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                            mProductSkuList = new ArrayList<>();
                            for (SkuDetails skuDetails : skuDetailsList) {
                                mProductSkuList.add(skuDetails);
                            }

                            Collections.sort(mProductSkuList, new Comparator<SkuDetails>() {
                                @Override
                                public int compare(SkuDetails lhs, SkuDetails rhs) {
                                    return (int) (lhs.getPriceAmountMicros() - rhs.getPriceAmountMicros());
                                }
                            });

                            showDialogWithAllProducts(mProductSkuList);
                        }
                    }
                });

            }
        });
    }

    public void showDialogWithAllProducts(final List<SkuDetails> list) {

        // for testing
        /*
        final String titles[] = {"t", "t", "t"};
        final String descriptions[] = {"d1", "d2", "d3"};
        final String prices[] = {"p1", "p2", "p3"};
        */
        _track_ui_click("DONATE", "DIALOG_SKU");

        final String titles[] = {list.get(0).getTitle(), list.get(1).getTitle(), list.get(2).getTitle()};
        final String descriptions[] = {list.get(0).getDescription(), list.get(1).getDescription(), list.get(2).getDescription()};
        final String prices[] = {list.get(0).getPrice(), list.get(1).getPrice(), list.get(2).getPrice()};


        final Dialog dialog = new Dialog(BBeat.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_sku_products_list);

        ListView lv = (ListView) dialog.findViewById(R.id.dialog_show_sku_listview);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ProductSkuListAdapter mAdapter;

        mAdapter = new ProductSkuListAdapter(BBeat.this, titles, descriptions, prices);
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                dialog.dismiss();
                _track_ui_click("DONATE", String.format("SKUCLICK-%d", position));
                startBillingFlow(list.get(position));
            }
        });

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.image_dialog_inapp_close);
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void startBillingFlow(SkuDetails skuDetails) {
        final BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build();

        executeBillingServiceRequest(new Runnable() {
            @Override
            public void run() {
                mBillingClient.launchBillingFlow(BBeat.this, billingFlowParams);
            }
        });
    }

    private class LoadAdapter extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... params) {
            for (String pname : programs.keySet()) {

                ProgramMeta pm = programs.get(pname);
                String catname = pm.getCat().toString();
                CategoryGroup g = null;

                /* Check if I already have a group with that name */
                for (CategoryGroup g2 : groups) {
                    if (g2.getName().equals(catname)) {
                        g = g2;
                    }
                }
                if (g == null) {
                    g = new CategoryGroup(catname);

                    try {
                        g.setNiceName(getString(R.string.class.getField("group_" + catname.toLowerCase()).getInt(null)));
                    } catch (Exception e) {
                        // pass
                    }

                    groups.add(g);
                }

                g.add(pm, DefaultProgramsBuilder.getProgram(pm));
//            g.setProgram();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                ArrayList<PresetModel> arrayList = (ArrayList<PresetModel>) ((BBeatApp) getApplicationContext()).getDbHelper().getAll(PresetModel.class);
                if (arrayList != null && arrayList.size() > 0) {
                    CategoryGroup categoryGroup = new CategoryGroup("CP");
                    categoryGroup.setNiceName(BBeat.this.getString(R.string.custom_preset));
                    ArrayList<Program> programArrayList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        Program program = new Program(arrayList.get(i).getName());
                        program.setAuthor(arrayList.get(i).getAuthor());
                        program.setDescription(arrayList.get(i).getDescription());
                        if (arrayList.get(i).getPeriodModelArray() != null && !TextUtils.isEmpty(arrayList.get(i).getPeriodModelArray())) {
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(arrayList.get(i).getPeriodModelArray()).getAsJsonArray();
                            Type listType = new TypeToken<ArrayList<PeriodModel>>() {
                            }.getType();

                            ArrayList<PeriodModel> periodModelArrayList = new Gson().fromJson(jsonArray, listType);
                            if (periodModelArrayList != null && periodModelArrayList.size() > 0) {
                                for (int j = 0; j < periodModelArrayList.size(); j++) {
                                    if (periodModelArrayList.get(j).getVoiceModelArray() != null && !TextUtils.isEmpty(periodModelArrayList.get(j).getVoiceModelArray())) {
                                        JsonParser parser1 = new JsonParser();
                                        JsonArray jsonArray1 = parser1.parse(periodModelArrayList.get(j).getVoiceModelArray()).getAsJsonArray();
                                        Type listType1 = new TypeToken<ArrayList<VoiceModel>>() {
                                        }.getType();

                                        ArrayList<VoiceModel> voiceModelArrayList = new Gson().fromJson(jsonArray1, listType1);
                                        periodModelArrayList.get(j).setVoiceModelArrayList(voiceModelArrayList);
                                    }
                                }
                            }
                            arrayList.get(i).setPeriodModelArrayList(periodModelArrayList);
                        }
                        if (arrayList.get(i).getPeriodModelArrayList() != null && arrayList.get(i).getPeriodModelArrayList().size() > 0) {
                            for (int a = 0; a < arrayList.get(i).getPeriodModelArrayList().size(); a++) {
                                PeriodModel periodModel = arrayList.get(i).getPeriodModelArrayList().get(a);
                                SoundLoop soundLoop = SoundLoop.NONE;
                                if (periodModel.getBackground() != null && !TextUtils.isEmpty(periodModel.getBackground())) {
                                    if (periodModel.getBackground().equalsIgnoreCase("None")) {
                                        soundLoop = SoundLoop.NONE;
                                    } else if (periodModel.getBackground().equalsIgnoreCase("White Noise")) {
                                        soundLoop = SoundLoop.WHITE_NOISE;
                                    } else if (periodModel.getBackground().equalsIgnoreCase("Unity")) {
                                        soundLoop = SoundLoop.UNITY;
                                    }
                                }
                                Visualization visualization = null;
                                if (periodModel.getVisualizer() != null && !TextUtils.isEmpty(periodModel.getVisualizer())) {
                                    if (periodModel.getVisualizer().equalsIgnoreCase("Aurora")) {
                                        visualization = new Aurora();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Black")) {
                                        visualization = new Black();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Flash")) {
                                        visualization = new Flash();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("GL Black")) {
                                        visualization = new GLBlack();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Hiit")) {
                                        visualization = new Hiit();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Hypno Flash")) {
                                        visualization = new HypnoFlash();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Hypnotic Spiral")) {
                                        visualization = new HypnoticSpiral();
//                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Image")) {
//                                        visualization = new Image();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Leds")) {
                                        visualization = new Leds();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("LSD")) {
                                        visualization = new LSD();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Mandelbrot")) {
                                        visualization = new Mandelbrot();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Morphine")) {
                                        visualization = new Morphine();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("None")) {
                                        visualization = new None();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Plasma")) {
                                        visualization = new Plasma();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Spiral Dots")) {
                                        visualization = new SpiralDots();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Star Field")) {
                                        visualization = new Starfield();
                                    } else if (periodModel.getVisualizer().equalsIgnoreCase("Star Field 3D")) {
                                        visualization = new Starfield3D();
                                    }
                                }
                                Period period = new Period(periodModel.getDuration(), soundLoop, Float.valueOf(periodModel.getBackgroundVolume()) / 100f, null);
                                if (periodModel.getVoiceModelArrayList() != null && periodModel.getVoiceModelArrayList().size() > 0) {
                                    for (int b = 0; b < periodModel.getVoiceModelArrayList().size(); b++) {
                                        VoiceModel voiceModel = periodModel.getVoiceModelArrayList().get(b);
                                        period.addVoice(new BinauralBeatVoice(voiceModel.getFreqStart(), voiceModel.getFreqEnd(), voiceModel.getVolume() / 100f));
                                    }
                                }
                                period.setV(visualization);
                                program.addPeriod(period);
                            }
                        }
//
                        try {
                            ProgramMeta.Category cat = ProgramMeta.Category.CP;
                            String nice_name = BBeat.this.getString(R.string.custom_preset);
                            ProgramMeta meta = new ProgramMeta(null, nice_name, cat);
                            categoryGroup.add(meta, program);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        groups.add(categoryGroup);
                        programArrayList.add(program);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ProgramListAdapter adapter = new ProgramListAdapter(BBeat.this, groups);
            mPresetList.setAdapter(adapter);
            // Expand all
            for (int groupPosition = 0; groupPosition < adapter.getGroupCount(); groupPosition++) {
                if (mPresetList.isGroupExpanded(groupPosition) == false) {
                    mPresetList.expandGroup(groupPosition);
                }
            }
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(BBeat.this); // this = YourActivity
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            if (intent.getExtras().getBoolean("refresh")) {
                programs = DefaultProgramsBuilder.getProgramMethods(this);
                groups = new ArrayList<CategoryGroup>();

                new LoadAdapter().execute();

            }
        }
    }
}
