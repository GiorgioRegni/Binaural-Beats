package com.ihunda.android.binauralbeat;

import android.app.*;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.*;
import android.os.Process;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Vector;

public class BBeatService extends Service {

    public static final String ACTION_START_PROGRAM = "BBT.ACTION_START_PROGRAM";
    public static final String ACTION_STOP_PROGRAM  = "BBT.ACTION_STOP_PROGRAM";
    public static final String ACTION_PAUSE_RESUME  = "BBT.ACTION_PAUSE_RESUME";

    /** Sent every TIMER_FSM_DELAY with the current frequency/progress for the UI */
    public static final String ACTION_TICK          = "BBT.ACTION_TICK";
    public static final String EXTRA_FREQ           = "freq";
    public static final String EXTRA_POS            = "pos";

    private static final int NOTIFICATION_ID = 42;

    private static final int MAX_STREAMS = 5;

    /* ----- audio & FSM ----- */
    private SoundPool           mSoundPool;
    private VoicesPlayer        vp;
    private HandlerThread       workerThread;
    private Handler             workerH;          // Handler bound to workerThread
    private Handler uiH;
    private BBeat.RunProgram         programFSM;
    private Vector<StreamVoice> playingStreams = new Vector<>(5);

    /**  Binder clients receive.  Made public & static so other
     *   classes in the same package (your BBeat activity) can see it. */
    public static class LocalBinder extends Binder {

        private final BBeatService svc;

        LocalBinder(BBeatService s) { this.svc = s; }

        /** Expose whatever control methods you need */
        public void startProgram(Program p, BBeat ui) { svc.runProgramForeground(p, ui); }
        public void catchUpAfterPause(long delta) {svc.catchUpAfterPause(delta);};
        public void setGraphicsEnabled(boolean on)  {svc.setGraphicsEnabled(on);}
        //public void togglePause()                     { svc.togglePause();      }
        public void stopProgram()                     { svc.stopForegroundService();}
        //public boolean isRunning()                    { return svc.programFSM != null; }

        /** Classic helper so clients can obtain the full service if desired */
        public BBeatService getService() { return svc; }
    }

    /* ------------------------------------------------------------------ */
    @Nullable @Override public IBinder onBind(Intent i) {
        /* hand the *same* binder to every client; create it once */
        if (binder == null) binder = new LocalBinder(this);
        return binder;
    }
    private LocalBinder binder;           // keep a reference

    @Override public void onCreate() {
        super.onCreate();

        /* dedicated thread so the service never blocks the main looper */
        workerThread = new HandlerThread("BBT‑Worker", Process.THREAD_PRIORITY_AUDIO);
        workerThread.start();
        workerH = new Handler(workerThread.getLooper());
        uiH = new Handler(Looper.getMainLooper());

        /* move SoundPool / VoicesPlayer initialisation from the activity */
        mSoundPool  = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        vp          = new VoicesPlayer();
        vp.start();
    }
    /*
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;

        switch (intent.getAction()) {
            case ACTION_START_PROGRAM:
                Program p = (Program) intent.getSerializableExtra("program");
                BBeat parent = (BBeat) intent.getSerializableExtra("parent");
                startForegroundService(p, parent);
                break;

            case ACTION_STOP_PROGRAM:
                stopForegroundService();
                break;

            case ACTION_PAUSE_RESUME:
                togglePause();
                break;
        }
        // We want the service to be restarted if the process dies
        return START_STICKY;
    }
*/

    public void runProgramForeground(Program p, BBeat parent) {
        /* Build persistent notification (you already had the code)           */
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this,
                NotificationChannelHelper.getDefaultChannel(this))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notif_started))
                .setContentText(getString(R.string.notif_descr, p.getName()))
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        startForeground(NOTIFICATION_ID, nb.build());

        /* Run the FSM on the worker thread exactly as before                 */
        stopCurrentProgram();             // if any

        programFSM = new BBeat.RunProgram(p, workerH, uiH, parent);
    }

    private void stopForegroundService() {
        stopCurrentProgram();
        stopForeground(true);
        stopSelf();
    }

    private void stopCurrentProgram() {
        if (programFSM != null) programFSM.stopProgram();
        programFSM = null;
        vp.stopVoices();
        for (StreamVoice v : playingStreams) mSoundPool.stop(v.streamID);
        playingStreams.clear();
    }

    private void catchUpAfterPause(long delta) {
        if (programFSM != null)
            programFSM.catchUpAfterPause(delta);
    }

    private void setGraphicsEnabled(boolean on) {
        if (programFSM != null)
            programFSM.setGraphicsEnabled(on);
    }

    @Override public void onDestroy() {
        stopCurrentProgram();
        vp.shutdown();
        mSoundPool.release();
        workerThread.quitSafely();
        super.onDestroy();
    }
}
