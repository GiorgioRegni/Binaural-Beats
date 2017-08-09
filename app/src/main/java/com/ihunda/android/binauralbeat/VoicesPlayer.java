package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

import com.ihunda.android.binauralbeat.Note.NoteK;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class VoicesPlayer extends Thread {

	private static final float DEFAULT_VOLUME = .7f;
	private static final String LOGVP = "VoiceP";
	private static final int MAX_VOICES = 10;
	AudioTrack track;
	static int HZ = 16384;
	private static final int AUDIOBUF_SIZE = HZ/4;
	float freqs[];
	float pitchs[];
	float vols[];
	int anglesL[] = new int[MAX_VOICES];
	int anglesR[] = new int[MAX_VOICES];
	int anglesISO[] = new int[MAX_VOICES];
	private FloatSinTable sinT;
	long startTime;
	int TwoPi;
	static int ISCALE = 1440;
	
	//short[] fakeS;
	
	boolean playing = false;
	
	boolean want_shutdown;
	private float fade;
	private float volume;

	public VoicesPlayer()
	{
		sinT = new FloatSinTable(ISCALE);
		TwoPi = ISCALE;
		
		want_shutdown = false;

		setPriority(Thread.MAX_PRIORITY);
		
		volume = DEFAULT_VOLUME;

		initTrack();
	}

	private void initTrack() {
        int minSize = AudioTrack.getMinBufferSize(HZ, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT );

        assert(AUDIOBUF_SIZE > minSize);

        if (track != null) {
            try {
                track.release();
            } catch(Exception e) {

            };
        }

        track = new AudioTrack( AudioManager.STREAM_MUSIC, HZ,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                AUDIOBUF_SIZE*2, AudioTrack.MODE_STREAM);
        Log.e(LOGVP, String.format("minsize %d bufsize %d ", minSize, AUDIOBUF_SIZE*2));
    }
	
	public void playVoices(ArrayList<BinauralBeatVoice> voices) {
		synchronized (voices) {
			freqs = new float[voices.size()];
			for (int i =0; i<freqs.length; i++) {
				freqs[i] = voices.get(i).freqStart;
			}
			pitchs = new float[voices.size()];
			for (int i =0; i<pitchs.length; i++) {
				pitchs[i] = voices.get(i).pitch;
			}
			vols = new float[voices.size()];
			for (int i =0; i<freqs.length; i++) {
				vols[i] = voices.get(i).volume;
			}
		}
		
		fade = 1f;
		
		playing = true;
		if (track.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
			try {
                track.play();
            } catch (java.lang.IllegalStateException e) {
                Log.e(LOGVP, "track.play " + e.toString());
                initTrack();
            }
		}
	}
	
	public void setFreqs(float freqs[]) {
		assert(freqs.length == this.freqs.length);
		this.freqs = freqs.clone();
	}
	
	public void setVolume(float vol) {
		track.setStereoVolume(vol*fade, vol*fade);
		volume = vol;
	}
	
	public void stopVoices() {
		playing = false;
		track.stop();
		
		anglesL = new int[MAX_VOICES];
		anglesR = new int[MAX_VOICES];
		anglesISO = new int[MAX_VOICES];
	}

	public void shutdown() {
		setVolume(0);
		want_shutdown = true;
	}
	
	public void setFade(float f) {
		fade = f;
		setVolume(volume);
		//Log.v(LOGVP, String.format("Fade %f", fade));
	}
	
	@Override
	public synchronized void run() {
		
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
		
		while(!want_shutdown)
		{
			// Sleep if no voice loaded
			if (!playing) {
				try {
					sleep(500);
					//Log.v(LOGVP, "Sleeping");
				} catch (InterruptedException e) {
					// ignore
				}
				continue;
			}
			
			
			short[] samples = fillSamples();
			int totalWritten = 0;
			int total = samples.length;
			
			while (totalWritten != total) {
				int out = track.write(samples, 0, samples.length);
				boolean sleep = false;
				
				//Log.v(LOGVP, String.format("Written %d short out of %d", out, samples.length));
				
				if (out == AudioTrack.ERROR_BAD_VALUE) {
					Log.v(LOGVP, String.format("Got BAD VALUE"));
					// It means somehow the audiotrack is not playing anymore, let's just stop
					break;
				} else if (out < 0) {
					Log.v(LOGVP, String.format("Got strange %d", out));
					break;
				}
				else
				{
					totalWritten += out;
					
					if (out != samples.length) {
						sleep = true;
						
					    short[] sf = new short[samples.length - out];
					    System.arraycopy(samples, out, sf, 0, samples.length - out);
					    
					    //Log.v(LOGVP, String.format("Shoud be equal %d %d", total - totalWritten, samples.length - out));
					    
					    samples = sf;
					}	
				}
				
				if (!want_shutdown && sleep) {
					Log.v(LOGVP, String.format("we're going too fast - throttling"));
					try {
						sleep(50);
					} catch (InterruptedException e) {
						// ignore
					}
				}
			}
		}
		
		track.release();
		
		Log.v(LOGVP, String.format("Graceful shutdown"));
	}
	
	private short[] fillSamplesIsoChronic() {
		assert(freqs != null);
				
		short samples[] = new short[AUDIOBUF_SIZE];
		float ws[] = new float[samples.length];
		
		for (int j=0; j<freqs.length; j++) { //freqs.length
			float base_freq;

			if (BinauralBeatVoice.DEFAULT == pitchs[j])
				base_freq = voicetoPitch(j);
			else
				base_freq = pitchs[j];

			float cur_freq = base_freq+freqs[j];
			
			int inc1 = (int) (TwoPi * (cur_freq) / HZ);
			int inc2 = (int) (TwoPi * (base_freq) / HZ);
			int inciso = (int) (TwoPi * (freqs[j]) / HZ);
			int angle1 = anglesL[j];
			int angle2 = anglesR[j];
			int angleiso = anglesISO[j];

			for(int i = 0; i < samples.length; i+=2)
			{
				if (angleiso > ISCALE/2) {
					ws[i] += 0;
					ws[i+1] += 0;
				}
				else
				{
					ws[i] += sinT.sinFastInt(angle1) * vols[j]; 
					ws[i+1] += sinT.sinFastInt(angle2) * vols[j];
				}
				angle1 += inc1;
				angle2 += inc2;
				angleiso += inciso;
				angleiso = angleiso % ISCALE;
			}

			anglesL[j] = angle1 % ISCALE;
			anglesR[j] = angle2 % ISCALE;
			anglesISO[j] = angleiso % ISCALE;
		}


		for(int i = 0; i < samples.length; i+=2)
		{
			samples[i] = (short) (ws[i]*Short.MAX_VALUE/freqs.length);
			samples[i+1] = (short) (ws[i+1]*Short.MAX_VALUE/freqs.length);
		}

		return samples;
	}

	private short[] fillSamples() {
		if (freqs == null || freqs.length == 0)
			return new short[AUDIOBUF_SIZE];
		
		short samples[] = new short[AUDIOBUF_SIZE];
		float ws[] = new float[samples.length];
		
		for (int j=0; j<freqs.length; j++) { //freqs.length
			float base_freq;

			if (BinauralBeatVoice.DEFAULT == pitchs[j])
				base_freq = voicetoPitch(j);
			else
				base_freq = pitchs[j];

			int inc1 = (int) (TwoPi * (base_freq+freqs[j]) / HZ);
			int inc2 = (int) (TwoPi * (base_freq) / HZ);
			int angle1 = anglesL[j];
			int angle2 = anglesR[j];

			for(int i = 0; i < samples.length; i+=2)
			{
				ws[i] += sinT.sinFastInt(angle1) * vols[j]; 
				ws[i+1] += sinT.sinFastInt(angle2) * vols[j];
				angle1 += inc1;
				angle2 += inc2;
			}

			anglesL[j] = angle1 % ISCALE;
			anglesR[j] = angle2 % ISCALE;
		}


		for(int i = 0; i < samples.length; i+=2)
		{
			samples[i] = (short) (ws[i]*Short.MAX_VALUE/freqs.length);
			samples[i+1] = (short) (ws[i+1]*Short.MAX_VALUE/freqs.length);
		}

		return samples;
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
	
	private float voicetoPitch(int i) {
		Note n = voicetoNote(i);
		return (float) n.getPitchFreq();
	}
}