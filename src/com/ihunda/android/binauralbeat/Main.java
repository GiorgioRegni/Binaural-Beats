package com.ihunda.android.binauralbeat;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.NotificationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main extends Activity {
	
	enum eState {START, RUNNING, PAUSE, END};
	enum appState {NONE, SETUP, INPROGRAM};
	
	private static final int MAX_STREAMS = 10;
	public static final float A4_FREQ = 440.00f;

	public static final float W_DELTA_FREQ = 2.00f;
	public static final float W_THETA_FREQ = 6.00f;
	public static final float W_ALPHA_FREQ = 10.00f;
	public static final float W_BETA_FREQ = 20.00f;
	public static final float W_GAMMA_FREQ = 60.00f;
	
	private static final long ANIM_TIME_MS = 600;
	
	private LinearLayout mPresetList;
	private LinearLayout mInProgram;
	private VizualizationView mVizV;
	private TextView mStatus;
	
	private appState state;
	
	private int soundA440;
	private int soundWhiteNoise;
	private SoundPool mSoundPool;
	
	private NotificationManager mNotificationManager;
	
	private Handler mHandler = new Handler();
	private RunProgram programFSM;
	
	private Vector<Integer> playingStreams = new Vector<Integer>(MAX_STREAMS);
	private int playingVoices[];
	
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
        
        Button b = (Button) findViewById(R.id.MenuCreate);
        b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startProgram(DefaultProgramsBuilder.SELF_HYPNOSIS());
			}
		});
        
        b = (Button) findViewById(R.id.MenuBack);
        b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				stopProgram();
			}
		});
        
        
        mInProgram = (LinearLayout) findViewById(R.id.inProgramLayout);
        mPresetList = (LinearLayout) findViewById(R.id.presetLayout);
        mVizV = (VizualizationView) findViewById(R.id.VisualizationView);
        mStatus = (TextView) findViewById(R.id.Status);
        
        state = appState.NONE;
        goToState(appState.SETUP);
    }

	private void goToState(appState newState) {
		switch(state) {
		case NONE:
			mInProgram.setVisibility(View.GONE);
			mPresetList.setVisibility(View.GONE);
			break;
		case INPROGRAM:
			runGoneAnimationOnView(mInProgram);
			break;
		case SETUP:
			runGoneAnimationOnView(mPresetList);
			break;
		default:
			break;
		}
		
		state = newState;
		switch(state) {
		case SETUP:
			runComeBackAnimationOnView(mPresetList);
			break;
		case INPROGRAM:
			runComeBackAnimationOnView(mInProgram);
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
		
		panic();
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
	
    private void _cancel_all_notifications() {
    	mNotificationManager.cancelAll();
    }
	
	private void startProgram(Program p) {
		if (programFSM != null)
			programFSM.stopProgram();
		goToState(appState.INPROGRAM);
		programFSM = new RunProgram(p, mHandler);
	}
	
	private void stopProgram() {
		programFSM.stopProgram();
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
     * right earbud to play a binaural beat of frequency
     */
	private float getSpeedRatioRight(float binaural_freq) {
		float goal = A4_FREQ + binaural_freq;
		
		return goal / A4_FREQ;
	}
    
	private int[] playBeat(float binaural_freq, float volume) {
		int idLeft, idRight;
		
		idLeft = mSoundPool.play(soundA440, volume, 0, 1, -1, 1.0f);
		idRight = mSoundPool.play(soundA440, 0, volume, 1, -1, getSpeedRatioRight(binaural_freq));
		registerStream(idLeft);
		registerStream(idRight);
		
		int[] res = {idLeft, idRight};
		
		return res;
	}
	
	/**
	 * Go through a list of voices and start playing them with their start frequency
	 * @param voices list of voices to play
	 */
	protected void playVoices(ArrayList<BinauralBeatVoice> voices) {
		playingVoices = new int[voices.size()*2];
		
		int i = 0;
		for (BinauralBeatVoice b: voices) {
			int[] ids = playBeat(b.freqStart, b.volume);
			playingVoices[i++] = ids[0];
			playingVoices[i++] = ids[1];
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
		
		for (BinauralBeatVoice v: voices) {
			@SuppressWarnings("unused")
			int idLeft = playingVoices[i++];
			int idRight = playingVoices[i++];
			float ratio = (v.freqEnd - v.freqStart) / length;
			
			if (i == 2)
				res = ratio*pos + v.freqStart;
			//mSoundPool.setRate(idLeft, rate);
			mSoundPool.setRate(idRight, getSpeedRatioRight(ratio*pos + v.freqStart));
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