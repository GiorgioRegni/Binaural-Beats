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
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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
import java.lang.ref.WeakReference;
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

public class BBeat extends AppCompatActivity {

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

    private static final String SOURCE_CODE_URL = "https://bit.ly/BBeats";
    private static final String BLOG_URL = "https://bit.ly/BBeatsBlog";
    private static final String HELP_URL = "https://bit.ly/BBeatsHelp";
    private static final String FACEBOOK_URL = "https://www.facebook.com/pages/Binaural-Beat-Therapy/121737064536801";
    private static final String FORUM_URL = FACEBOOK_URL;
    private static final String CONTACT_EMAIL = "binaural-beats@ihunda.com";
    private static final String FACEBOOK_INSTALL_URL = "https://bit.ly/BBTFBSHARE";
    private static final String FACEBOOK_SHARE_IMG = "https://i.imgur.com/bG9coHF.png";
    private static final String LOGBBEAT = "BBT-MAIN";

    /* All dialogs declaration go here */
    private static final int DIALOG_WELCOME = 1;
    private static final int DIALOG_CONFIRM_RESET = 2;
    private static final int DIALOG_GETTING_INVOLVED = 3;
    private static final int DIALOG_JOIN_COMMUNITY = 4;
    private static final int DIALOG_PROGRAM_PREVIEW = 5;

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

    Map<String, ProgramMeta> programNameToMetaMap;
    ArrayList<CategoryGroup> allProgramCategories;

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
    boolean mIsBillingServiceConnected = false;
    int mBillingClientResponseCode = 0;

    SharedPref mSharedPref = SharedPref.getInstance();
    String mDonationLevel = null;
    private int currentHistoryId = -1;
    private long historyTotalTimeElapsed = 0;
    private String historyProgramName = "";
    private DrawerLayout drawerLayout;

    private BBeatService.LocalBinder   beatSvc;   // will hold the binder
    private boolean                    boundSvc = false;
    private ServiceConnection connSvc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Facebook */
        // This is deprecated but I keep it for older devices and android versions
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Facebook
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(getApplication());

        setContentView(R.layout.main);

        /* Prevent using the entire vertical space on notched phones */
        View content = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(content, (v, insets) -> {
            Insets sys = insets.getInsets(
                    WindowInsetsCompat.Type.statusBars()
                            | WindowInsetsCompat.Type.navigationBars()
            );
            // apply as padding to your root view:
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });

        /* Initialize Fabric and Crashlytics */
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        /* Init sounds */
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mSharedPref.initialize(this);

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

        // JENLA change to new better code
        programNameToMetaMap = DefaultProgramsBuilder.getProgramMethods(this);
        allProgramCategories = new ArrayList<CategoryGroup>();

        // Refresh the list of presets with the custom ones
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                refreshPresetsListWithCustomPresets();
            }
        });

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
                if (allProgramCategories.get(groupPosition).getObjets().get(childPosition).getMethod() != null) {
                    selectProgram(allProgramCategories.get(groupPosition).getObjets().get(childPosition));
                } else {
                    selectCreateProgram(allProgramCategories.get(groupPosition).getProgram().get(childPosition));
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

        // TODO for now disable preset builder to decide if people need to buy it
        b.setVisibility(View.GONE);
        b.setEnabled(false);

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

        if (boundSvc) { unbindService(connSvc); boundSvc = false; }
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
                pause_time = -1;


                //_start_notification(programFSM.getProgram().getName());
                runComeBackAnimationOnView(mInProgram);
                /* JENLA
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
                */

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
                beatSvc.catchUpAfterPause(delta);
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
            beatSvc.setGraphicsEnabled(on);
        }
        vizEnabled = on;
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
        // This seems to be unnecessary now, see
        // https://stackoverflow.com/questions/42649153/appeventslogger-deactivateappcontext-context-deprecated
        // https://developers.facebook.com/docs/reference/androidsdk/current/facebook/com/facebook/appevents/appeventslogger.html/
        // AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        Log.v(LOGBBEAT, "onResume");
        super.onResume();
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
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle(getString(R.string.notif_started))
                        .setContentText(getString(R.string.notif_descr, programName))
                        .setOngoing(true);

        Intent notificationIntent = this.getIntent(); //new Intent(this, hiit.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

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

        //programFSM = new RunProgram(p, mHandler);

        /* --- connection object ------------------------------------------------ */
        connSvc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName n, IBinder b) {
                beatSvc = (BBeatService.LocalBinder) b;
                boundSvc = true;

                /* >>> Start the program now that we’re connected <<< */

                beatSvc.startProgram(p /* Program */,
                        BBeat.this       /* Activity instance */);
            }

            @Override
            public void onServiceDisconnected(ComponentName n) {
                boundSvc = false;
                beatSvc = null;
                connSvc = null;
            }
        };
        Intent i = new Intent(this, BBeatService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(i);   // make sure the service exists
        else
                startService(i);

        bindService(i, connSvc, Context.BIND_AUTO_CREATE); // get the binder

        goToState(appState.INPROGRAM);
    }

    private void stopProgram() {
        if (beatSvc != null) {
            beatSvc.stopProgram();
            beatSvc = null;
        }
        panic(); // stop all sound

        mToolbar.setTitle(getString(R.string.app_name));

        // Leave time for the services to stop
        new Handler().postDelayed(() -> {goToState(appState.SETUP);}
        , RunProgram.TIMER_FSM_DELAY*10);
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

    public static class RunProgram implements Runnable {

        public static final long TIMER_FSM_DELAY = 1000 / 20;

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
        private Handler srvH; // Handler for sound, timer and CPU task, no UI calls
        private Handler uiH; // Handler for UI update tasks
        LineGraphView graphView;
        private final WeakReference<BBeat> uiRef;  // weak ref
        private long _last_graph_update;

        /** Quick helper; returns null after the activity is destroyed. */
        @Nullable
        private BBeat ui() { return uiRef.get(); }

        public RunProgram(Program pR, Handler srvH, Handler uiH, @Nullable BBeat ui) {
            this.pR = pR;
            this.srvH = srvH;
            this.uiRef   = new WeakReference<>(ui);
            this.uiH = uiH;

            programLength = pR.getLength();
            sProgramLength = ui().getString(R.string.time_format,
                    formatTimeNumberwithLeadingZero((int) programLength / 60),
                    formatTimeNumberwithLeadingZero((int) programLength % 60));
            formatString = ui().getString(R.string.info_timing);
            format_INFO_TIMING_MIN_SEC = ui().getString(R.string.time_format_min_sec);
            startTime = _getClock();
            oldDelta = -1;
            _last_graph_update = 0;

            s = eState.START;

            uiH.post(new Runnable() {
                @Override
                public void run() {
                    BBeat u = ui();
                    if (u == null) return;

                    u.mVizHolder.setVisibility(View.VISIBLE);

                    boolean glMode = pR.doesUseGL();
                    if (glMode) {
                        u.mVizV = new GLVizualizationView(u.getBaseContext());
                    } else {
                        u.mVizV = new CanvasVizualizationView(u.getBaseContext());
                    }
                    u.mVizHolder.addView(u.mVizV);
                }
            });

            srvH.postDelayed(this, TIMER_FSM_DELAY);
        }

        public Period getCurrentPeriod() {
            return currentPeriod;
        }

        public void stopProgram() {
            BBeat u = ui();
            if (u != null) {
                u.stopAllVoices();
                u.stopBackgroundSample();
            }

            uiH = null;
            srvH.removeCallbacks(this);
        }

        private void startPeriod(Period p) {
            uiH.post(() -> {
                BBeat u = ui();
                if (u == null) return;

                if (u.vizEnabled) {
                    ((VizualisationView) u.mVizV).startVisualization(p.getV(), p.getLength());
                } else {
                    Visualization v;
                    if (((Object) u.mVizV).getClass() == GLVizualizationView.class) {
                        v = new GLBlack();
                    } else {
                        v = new Black();
                    }
                    ((VizualisationView) u.mVizV).startVisualization(v, p.getLength());
                }

                ((VizualisationView) u.mVizV).setFrequency(p.getVoices().get(0).freqStart);
            });
            BBeat u = ui();
            if (u != null) {
                u.playVoices(p.voices);
                u.vp.setFade(FADE_MIN);
                u.playBackgroundSample(p.background, p.getBackgroundvol());
            }

            Log.v(LOGBBEAT, String.format("New Period - duration %d", p.length));
        }

        private void inPeriod(long now, Period p, float pos) {
            long delta = (now - startTime) / 50; // Do not refresh too often
            BBeat u = ui();
            if (u == null) return;

            float freq = u.skewVoices(p.voices, pos, p.length, oldDelta != delta);

            uiH.post(() -> {
                try{
                        ((VizualisationView) u.mVizV).setFrequency(freq);
                        ((VizualisationView) u.mVizV).setProgress(pos);
                        updatePeriodGraph((now - startTime) / 1000);
                    }
                catch (Exception e) {}
            });

            if (oldDelta != delta) {
                oldDelta = delta;
                delta = delta / 20; // Down to seconds

                long finalDelta = delta; // required to be passed as into the runnable below
                uiH.post(() -> {
                    u.mStatus.setText(String.format(formatString,
                                    freq,
                                    formatTimeNumberwithLeadingZero((int) finalDelta / 60),
                                    formatTimeNumberwithLeadingZero((int) finalDelta % 60)
                            )
                                    +
                                    sProgramLength
                    );
                });
            }
        }

        private void endPeriod() {
            BBeat u = ui();
            if (u != null) {
                u.stopAllVoices();
                u.stopBackgroundSample();
            }

            uiH.post(() -> {
                ((VizualisationView) u.mVizV).stopVisualization();
            });
        }

        public void catchUpAfterPause(long delta) {
            cT += delta;
            startTime += delta;
        }
        public void setGraphicsEnabled(boolean on) {
            uiH.post(() -> {
                BBeat u = ui();
                if (u == null) return;

                if (on == false) {
                    // Disable Viz
                    Period p = getCurrentPeriod();
                    Visualization v;

                    if (((Object) u.mVizV).getClass() == GLVizualizationView.class) {
                        v = new GLBlack();
                    } else {
                        v = new Black();
                    }

                    ((VizualisationView) u.mVizV).stopVisualization();
                    ((VizualisationView) u.mVizV).startVisualization(v, p.getLength());
                    ((VizualisationView) u.mVizV).setFrequency(p.getVoices().get(0).freqStart);

                    if (u.mWl.isHeld()) {
                        u.mWl.release();
                    }

                    u.ToastText(R.string.graphics_off);
                } else  {
                    // Enable viz
                    Period p = getCurrentPeriod();
                    ((VizualisationView) u.mVizV).stopVisualization();
                    ((VizualisationView) u.mVizV).startVisualization(p.getV(), p.getLength());
                    ((VizualisationView) u.mVizV).setFrequency(p.getVoices().get(0).freqStart);

                    if (u.mWl.isHeld() == false) {
                        u.mWl.acquire();
                    }

                    u.ToastText(R.string.graphics_on);
                }
            });
        }

        public void run() {
            // Just a quick check if the service was stopped while the postdelayed was still in program
            BBeat u = ui();
            if (u == null) return;
            if (u.beatSvc == null) return;

            long now = _getClock();

            switch (s) {
                case START:
                    s = eState.RUNNING;
                    periodsIterator = pR.getPeriodsIterator();
                    cT = now;
                    uiH.post(() -> {
                                drawPeriodGraph();
                            });
                    nextPeriod();
                    break;

                case RUNNING:
                    if (u.isPaused()) {
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
                    u.stopProgram();
                    return;
            }

            srvH.postDelayed(this, TIMER_FSM_DELAY);
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

            BBeat u = ui();
            if (u == null) return;

            graphView = new LineGraphView(
                    u // context
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

            u.mGraphVoicesLayout.removeAllViews();
            u.mGraphVoicesLayout.addView(graphView);
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

    private static String formatTimeNumberwithLeadingZero(int t) {
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
                    // JENLA45
                    // .setContentTitle("Binaural Beats Therapy App")
                    //.setContentDescription("Already approved by hundred thousands of people, a very powerful self-improvement, brain enhancement and stress-relief app.")
                    .setContentUrl(Uri.parse(FACEBOOK_INSTALL_URL))
                    //.setImageUrl(Uri.parse(FACEBOOK_SHARE_IMG))
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

    private static long _getClock() {
        return SystemClock.elapsedRealtime();
    }

    private class LoadAdapter extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... params) {
            /* Creates the list of groups and attach ProgramMeta into each group */
            for (String pname : programNameToMetaMap.keySet()) {

                ProgramMeta pm = programNameToMetaMap.get(pname);
                String catName = pm.getCat().toString();
                CategoryGroup g = null;
                int catGroupIndex = -1;

                /* Check if I already have a group with that name */
                catGroupIndex = allProgramCategories.indexOf(catName);
                if (catGroupIndex < 0) {
                    g = new CategoryGroup(catName);

                    try {
                        g.setNiceName(getString(R.string.class.getField("group_" + catName.toLowerCase()).getInt(null)));
                    } catch (Exception e) {
                        // pass
                    }
                    allProgramCategories.add(g);
                } else {
                    g = allProgramCategories.get(catGroupIndex);
                }

                g.add(pm, DefaultProgramsBuilder.getProgram(pm));
//            g.setProgram();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                // Go through all the user presets
                ArrayList<PresetModel> allUserPresetModels = (ArrayList<PresetModel>) ((BBeatApp) getApplicationContext()).getDbHelper().getAll(PresetModel.class);
                if (allUserPresetModels != null && allUserPresetModels.size() > 0) {
                    CategoryGroup categoryGroup = new CategoryGroup("CP");
                    categoryGroup.setNiceName(BBeat.this.getString(R.string.custom_preset));
                    ArrayList<Program> allUserPrograms = new ArrayList<>();
                    for (int i = 0; i < allUserPresetModels.size(); i++) {
                        PresetModel presetModel = allUserPresetModels.get(i);

                        Program program = new Program(presetModel.getName());
                        program.setAuthor(presetModel.getAuthor());
                        program.setDescription(presetModel.getDescription());
                        if (presetModel.getPeriodModelArray() != null && !TextUtils.isEmpty(presetModel.getPeriodModelArray())) {
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(presetModel.getPeriodModelArray()).getAsJsonArray();
                            Type listType = new TypeToken<ArrayList<PeriodModel>>() {
                            }.getType();

                            ArrayList<PeriodModel> allPresetPeriodModels = new Gson().fromJson(jsonArray, listType);
                            if (allPresetPeriodModels != null && allPresetPeriodModels.size() > 0) {
                                for (int j = 0; j < allPresetPeriodModels.size(); j++) {
                                    PeriodModel periodModel = allPresetPeriodModels.get(j);

                                    if (periodModel.getVoiceModelArray() != null && !TextUtils.isEmpty(periodModel.getVoiceModelArray())) {
                                        JsonParser parser1 = new JsonParser();
                                        JsonArray jsonArray1 = parser1.parse(periodModel.getVoiceModelArray()).getAsJsonArray();
                                        Type listType1 = new TypeToken<ArrayList<VoiceModel>>() {
                                        }.getType();

                                        ArrayList<VoiceModel> voiceModelArrayList = new Gson().fromJson(jsonArray1, listType1);
                                        periodModel.setVoiceModelArrayList(voiceModelArrayList);
                                    }
                                }
                            }
                            presetModel.setPeriodModelArrayList(allPresetPeriodModels);
                        }
                        if (presetModel.getPeriodModelArrayList() != null && presetModel.getPeriodModelArrayList().size() > 0) {
                            for (int a = 0; a < presetModel.getPeriodModelArrayList().size(); a++) {
                                PeriodModel periodModel = presetModel.getPeriodModelArrayList().get(a);
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
                        allProgramCategories.add(categoryGroup);
                        allUserPrograms.add(program);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ProgramListAdapter adapter = new ProgramListAdapter(BBeat.this, allProgramCategories);
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

    private void refreshPresetsListWithCustomPresets () {
        programNameToMetaMap = DefaultProgramsBuilder.getProgramMethods(this);
        allProgramCategories = new ArrayList<CategoryGroup>();

        /* Creates the list of groups and attach ProgramMeta into each group */
        for (String pname : programNameToMetaMap.keySet()) {

            ProgramMeta pm = programNameToMetaMap.get(pname);
            String catName = pm.getCat().toString();
            CategoryGroup g = null;
            int catGroupIndex = -1;

            /* Check if I already have a group with that name */
            for (int i = 0; i < allProgramCategories.size(); i++) {
                if (allProgramCategories.get(i).getName().equals(catName)) {
                    catGroupIndex = i;
                    break;
                }
            }

            if (catGroupIndex < 0) {
                g = new CategoryGroup(catName);

                try {
                    g.setNiceName(getString(R.string.class.getField("group_" + catName.toLowerCase()).getInt(null)));
                } catch (Exception e) {
                    // pass
                }
                allProgramCategories.add(g);
            } else {
                g = allProgramCategories.get(catGroupIndex);
            }

            g.add(pm, DefaultProgramsBuilder.getProgram(pm));
        }

        try {
            // Go through all the user presets
            ArrayList<PresetModel> allUserPresetModels = (ArrayList<PresetModel>) ((BBeatApp) getApplicationContext()).getDbHelper().getAll(PresetModel.class);
            if (allUserPresetModels != null && allUserPresetModels.size() > 0) {
                // Create and add the custom preset category
                CategoryGroup categoryGroup = new CategoryGroup("CP");
                categoryGroup.setNiceName(BBeat.this.getString(R.string.custom_preset));
                allProgramCategories.add(categoryGroup);

                ArrayList<Program> allUserPrograms = new ArrayList<>();
                for (int i = 0; i < allUserPresetModels.size(); i++) {
                    PresetModel presetModel = allUserPresetModels.get(i);

                    Program program = new Program(presetModel.getName());
                    program.setAuthor(presetModel.getAuthor());
                    program.setDescription(presetModel.getDescription());
                    if (presetModel.getPeriodModelArray() != null && !TextUtils.isEmpty(presetModel.getPeriodModelArray())) {
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(presetModel.getPeriodModelArray()).getAsJsonArray();
                        Type listType = new TypeToken<ArrayList<PeriodModel>>() {
                        }.getType();

                        ArrayList<PeriodModel> allPresetPeriodModels = new Gson().fromJson(jsonArray, listType);
                        if (allPresetPeriodModels != null && allPresetPeriodModels.size() > 0) {
                            for (int j = 0; j < allPresetPeriodModels.size(); j++) {
                                PeriodModel periodModel = allPresetPeriodModels.get(j);

                                if (periodModel.getVoiceModelArray() != null && !TextUtils.isEmpty(periodModel.getVoiceModelArray())) {
                                    JsonParser parser1 = new JsonParser();
                                    JsonArray jsonArray1 = parser1.parse(periodModel.getVoiceModelArray()).getAsJsonArray();
                                    Type listType1 = new TypeToken<ArrayList<VoiceModel>>() {
                                    }.getType();

                                    ArrayList<VoiceModel> voiceModelArrayList = new Gson().fromJson(jsonArray1, listType1);
                                    periodModel.setVoiceModelArrayList(voiceModelArrayList);
                                }
                            }
                        }
                        presetModel.setPeriodModelArrayList(allPresetPeriodModels);
                    }
                    if (presetModel.getPeriodModelArrayList() != null && presetModel.getPeriodModelArrayList().size() > 0) {
                        for (int a = 0; a < presetModel.getPeriodModelArrayList().size(); a++) {
                            PeriodModel periodModel = presetModel.getPeriodModelArrayList().get(a);
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
                            Visualization visualization = DefaultProgramsBuilder.getVizualisationfromName(periodModel.getVisualizer());

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
                        ProgramMeta meta = new ProgramMeta(null, program.name, cat);
                        categoryGroup.add(meta, program);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    allUserPrograms.add(program);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ProgramListAdapter adapter = new ProgramListAdapter(BBeat.this, allProgramCategories);
        mPresetList.setAdapter(adapter);
        // Expand all
        for (int groupPosition = 0; groupPosition < adapter.getGroupCount(); groupPosition++) {
            if (mPresetList.isGroupExpanded(groupPosition) == false) {
                mPresetList.expandGroup(groupPosition);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            if (intent.getExtras().getBoolean("refresh")) {
                // Refresh the list of presets with the custom ones
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshPresetsListWithCustomPresets();
                    }
                });
            }
        }
    }
}
