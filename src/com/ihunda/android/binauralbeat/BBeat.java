package com.ihunda.android.binauralbeat;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ihunda.android.binauralbeat.Note.NoteK;

public class BBeat extends Activity {
	
	enum eState {START, RUNNING, PAUSE, END};
	enum appState {NONE, SETUP, INPROGRAM};
	
	private static final int MAX_STREAMS = 10;

	public static final float W_DELTA_FREQ = 2.00f;
	public static final float W_THETA_FREQ = 6.00f;
	public static final float W_ALPHA_FREQ = 10.00f;
	public static final float W_BETA_FREQ = 20.00f;
	public static final float W_GAMMA_FREQ = 60.00f;
	
	private static final long ANIM_TIME_MS = 600;
	
	private LinearLayout mPresetView;
	private LinearLayout mInProgram;
	private VizualizationView mVizV;
	private TextView mStatus;
	private ListView mPresetList;
	private ArrayList<String> lv_preset_arr;
	private ArrayList<Program> all_programs;
	
	private appState state;
	
	private int soundA440;
	private int soundWhiteNoise;
	private int soundUnity;
	private SoundPool mSoundPool;
	
	private Note A440 = new Note(NoteK.A, 4);
	
	private NotificationManager mNotificationManager;
	private static final int NOTIFICATION_STARTED = 1;
	
	private Handler mHandler = new Handler();
	private RunProgram programFSM;
	private int pause_time = -1;
	
	private Vector<Integer> playingStreams = new Vector<Integer>(MAX_STREAMS);
	private int playingVoices[];
	private int playingBackground = -1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* Init sounds */
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundA440 = mSoundPool.load(this, R.raw.a440, 1);
        soundWhiteNoise = mSoundPool.load(this, R.raw.whitenoise, 1);
        soundUnity = mSoundPool.load(this, R.raw.unity, 1);
        
        Button b = (Button) findViewById(R.id.MenuSetup);
        b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			}
		});
        
        b = (Button) findViewById(R.id.MenuBack);
        b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				stopProgram();
			}
		});
        
        mInProgram = (LinearLayout) findViewById(R.id.inProgramLayout);
        mPresetView = (LinearLayout) findViewById(R.id.presetLayout);
        mVizV = (VizualizationView) findViewById(R.id.VisualizationView);
        mStatus = (TextView) findViewById(R.id.Status);
        
        setupProgramList();
        
        mPresetList = (ListView) findViewById(R.id.presetListView);
        mPresetList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lv_preset_arr));
        mPresetList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startProgram(all_programs.get(arg2));
			}
		});
        
        
        state = appState.NONE;
        goToState(appState.SETUP);
    }
    
    private void setupProgramList() {
    	lv_preset_arr = new ArrayList<String>();
    	lv_preset_arr.add(getString(R.string.program_self_hypnosis));
    	lv_preset_arr.add(getString(R.string.program_highest_mental_activity));
    	lv_preset_arr.add(getString(R.string.program_unity));
    	
    	all_programs = new ArrayList<Program>();
    	all_programs.add(DefaultProgramsBuilder.SELF_HYPNOSIS(new Program(getString(R.string.program_self_hypnosis))));
    	all_programs.add(DefaultProgramsBuilder.AWAKE(new Program(getString(R.string.program_highest_mental_activity))));
    	all_programs.add(DefaultProgramsBuilder.UNITY(new Program(getString(R.string.program_unity))));
    }

	private void goToState(appState newState) {
		switch(state) {
		case NONE:
			mInProgram.setVisibility(View.GONE);
			mPresetView.setVisibility(View.GONE);
			break;
		case INPROGRAM:
			runGoneAnimationOnView(mInProgram);
			_cancel_all_notifications();
			break;
		case SETUP:
			runGoneAnimationOnView(mPresetView);
			break;
		default:
			break;
		}
		
		state = newState;
		switch(state) {
		case SETUP:
			runComeBackAnimationOnView(mPresetView);
			mPresetList.invalidate();
			mVizV.setVisibility(View.GONE);
			break;
		case INPROGRAM:
			_start_notification(programFSM.getProgram().getName());
			runComeBackAnimationOnView(mInProgram);
			mVizV.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		panic();
		_cancel_all_notifications();
    	
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}
    
	private void panic() {
		// Stop all sounds
		for (Integer i: playingStreams)
			mSoundPool.stop(i);
		playingStreams.clear();
	}
	
    private void _start_notification(String programName) {
    	Notification notification = new Notification(R.drawable.icon, getString(R.string.notif_started), System.currentTimeMillis());
    	
    	Context context = getApplicationContext();
    	CharSequence contentTitle = getString(R.string.notif_started);
    	CharSequence contentText = getString(R.string.notif_descr, programName);
    	Intent notificationIntent = this.getIntent(); //new Intent(this, hiit.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    	notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    	notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
    	
    	mNotificationManager.notify(NOTIFICATION_STARTED, notification);
    }
    
	
    private void _cancel_all_notifications() {
    	mNotificationManager.cancelAll();
    }
	
	private void startProgram(Program p) {
		if (programFSM != null)
			programFSM.stopProgram();
		
		((TextView) findViewById(R.id.programName)).setText(p.getName());
		

		programFSM = new RunProgram(p, mHandler);
		goToState(appState.INPROGRAM);
	}
	
	private void stopProgram() {
		programFSM.stopProgram();
		programFSM = null;
		goToState(appState.SETUP);
	}
	
	/*
	 * Record all playing stream ids to be able to stop sound on pause/panic
	 */
    private void registerStream(int i) {
		 playingStreams.add(new Integer(i));
		 if (playingStreams.size() > MAX_STREAMS)
			 playingStreams.remove(0);
    }
	
    
    /*
     * Return the speed ratio to use to skew the basic note on the
     * left earbud to play a given note
     */
	private float getSpeedRatioLeft(Note base) {
		return (float) (base.getPitchFreq() / A440.getPitchFreq());
	}
	
    /*
     * Return the speed ratio to use to skew the basic note on the
     * right earbud to play a binaural beat of frequency
     */
	private float getSpeedRatioRight(Note base, float binaural_freq) {
		double baseFreq = base.getPitchFreq();
		double goal = baseFreq + binaural_freq;
		
		return (float) (goal / A440.getPitchFreq());
	}
    
	private int[] playBeat(Note base, float binaural_freq, float volume) {
		int idLeft, idRight;
		
		idLeft = mSoundPool.play(soundA440, volume, 0, 1, -1, getSpeedRatioLeft(base));
		idRight = mSoundPool.play(soundA440, 0, volume, 1, -1, getSpeedRatioRight(base, binaural_freq));
		registerStream(idLeft);
		registerStream(idRight);
		
		int[] res = {idLeft, idRight};
		
		return res;
	}
	
	private void playBackgroundSample(SoundLoop background, float vol) {
		switch(background) {
		case WHITE_NOISE:
			playingBackground = mSoundPool.play(soundWhiteNoise, vol, vol, 1, -1, 1.0f);
			break;
		case UNITY:
			playingBackground = mSoundPool.play(soundUnity, vol, vol, 1, -1, 1.0f);
			break;
		default:
			playingBackground = -1;
			break;
		}
		
		if (playingBackground != -1)
			registerStream(playingBackground);
	}
	
	private void stopBackgroundSample() {
		if (playingBackground != -1) {
			mSoundPool.stop(playingBackground);
		}
		playingBackground = -1;
	}
	
	/**
	 * Go through a list of voices and start playing them with their start frequency
	 * @param voices list of voices to play
	 */
	protected void playVoices(ArrayList<BinauralBeatVoice> voices) {
		playingVoices = new int[voices.size()*2];
		
		int i = 0;
		int voiceId = 0;
		for (BinauralBeatVoice b: voices) {
			int[] ids = playBeat(voicetoNote(voiceId), b.freqStart, b.volume);
			playingVoices[i++] = ids[0];
			playingVoices[i++] = ids[1];
			voiceId++;
		}
		
	}
	
	/**
	 * @param voices
	 * @param pos
	 * @param length
	 * @return beat frequency of first voice
	 */
	protected float skewVoices(ArrayList<BinauralBeatVoice> voices, float pos, float length) {
		int i = 0;
		float res = 1;
		int voiceId = 0;
		
		for (BinauralBeatVoice v: voices) {
			@SuppressWarnings("unused")
			int idLeft = playingVoices[i++];
			int idRight = playingVoices[i++];
			float ratio = (v.freqEnd - v.freqStart) / length;
			
			if (i == 2)
				res = ratio*pos + v.freqStart;
			//mSoundPool.setRate(idLeft, rate);
			mSoundPool.setRate(idRight, getSpeedRatioRight(voicetoNote(voiceId), ratio*pos + v.freqStart));
			voiceId++;
		}
		
		return res;
	}
	
	/**
	 * Go through all currently running beat voices and stop them
	 */
	protected void stopAllVoices() {
		for (int i: playingVoices) {
			mSoundPool.stop(i);
		}
	}
	
	private Note voicetoNote(int i) {
		switch (i) {
		case 0:
			return new Note(NoteK.A);
		case 1:
			return new Note(NoteK.C);
		case 2:
			return new Note(NoteK.E);
		case 3:
			return new Note(NoteK.G);
		case 4:
			return new Note(NoteK.C, 5);
		case 5:
			return new Note(NoteK.E, 6);
		default:
			return new Note(NoteK.A, 7);	
		}
	}
	
	class RunProgram implements Runnable {

		private static final long TIMER_FSM_DELAY = 50;
		
		private Program pR; 
		private int c; // current Period
		private long cT; // current Period start time
		private long startTime;
		
		private eState s;
		private Handler h;

		public RunProgram(Program pR, Handler h) {
			this.pR = pR;
			this.h = h;
			
			s = eState.START;
			
			h.post(this);
		}
		
		public void stopProgram() {
			endPeriod();
			h.removeCallbacks(this);
		}
		
		private void startPeriod(Period p) {
			mVizV.startVisualization(p.getV(), p.getLength());
			mVizV.setFrequency(p.getVoices().get(0).freqStart);
			playVoices(p.voices);
			playBackgroundSample(p.background, p.getBackgroundvol());
		}

		private void inPeriod(Period p, float pos) {
			float freq = skewVoices(p.voices, pos, p.length);

			mVizV.setFrequency(freq);
			mVizV.setProgress(pos);
			
			mStatus.setText(getString(R.string.info_timing,
					freq,
					formatTimeNumberwithLeadingZero((int) pos/60),
					formatTimeNumberwithLeadingZero((int) pos%60),
					formatTimeNumberwithLeadingZero((int) p.length/60),
					formatTimeNumberwithLeadingZero((int) p.length%60)
			));
		}
		
		private void endPeriod() {
			stopAllVoices();
			stopBackgroundSample();
			mVizV.stopVisualization();
		}
		
		public void run() {
			long now = System.currentTimeMillis();
			
			switch(s) {
			case START:
				startTime = now;
				s = eState.RUNNING;
				c = 0;
				cT = now;
				startTime = -1;
				startPeriod(pR.seq.get(c));
			break;
			
			case RUNNING:
				Period p = pR.seq.get(c);
				float pos = (now - cT) / 1000f;
				
				if (pos > p.length) {
					endPeriod();
					
					// Current period is over
					if (++c >= pR.seq.size()) {
						// Finished
						s = eState.END;
					} else {
						// this is a new period
						cT = now;
						p = pR.seq.get(c);
						startPeriod(p);
					}
				} else {
					/**
					 * In the middle of current period, adjust each beat voice
					 */
					inPeriod(p, pos);
				}
				break;
				
			case END:
				break;
			}
			
			h.postDelayed(this, TIMER_FSM_DELAY);
		}

		public Program getProgram() {
			return pR;
		}
	}

	public Animation runGoneAnimationOnView(View target) {
  	  Animation animation = AnimationUtils.loadAnimation(this,
  	                                                     android.R.anim.slide_out_right );
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
	                                                     android.R.anim.slide_in_left );
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
    	if (t > 9)
    		return String.format("%2d", t);
    	else
    		return String.format("0%1d", t);
    }
}