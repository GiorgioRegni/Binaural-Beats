package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

import com.ihunda.android.binauralbeat.Note.NoteK;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class VoicesPlayer extends Thread {

	private static final String LOGVP = "VoiceP";
	AudioTrack track;
	static int HZ = 8192;
	float freqs[];
	float vols[];
	float anglesL[];
	float anglesR[];
	int calcFrames;
	int bufFrames;
	private FloatSinTable sinT;
	long startTime;
	float TwoPi;
	
	//short[] fakeS;
	
	boolean playing = false;

	public VoicesPlayer()
	{
		int minSize = AudioTrack.getMinBufferSize(HZ, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT );        
		
		int audiobuf = 16384;
		assert(audiobuf > minSize);
		
		track = new AudioTrack( AudioManager.STREAM_MUSIC, HZ, 
				AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, 
				audiobuf, AudioTrack.MODE_STREAM);
		calcFrames = audiobuf/16;
		bufFrames = audiobuf;
		sinT = new FloatSinTable();
		TwoPi = (float) (2*Math.PI);
		
		Log.e(LOGVP, String.format("minsize %d bufsize %d ", minSize, audiobuf));
	}
	
	public void playVoices(ArrayList<BinauralBeatVoice> voices) {
		synchronized (voices) {
			freqs = new float[voices.size()];
			for (int i =0; i<freqs.length; i++) {
				freqs[i] = voices.get(i).freqStart;
			}
			vols = new float[voices.size()];
			for (int i =0; i<freqs.length; i++) {
				vols[i] = voices.get(i).volume;
			}
			anglesL = new float[voices.size()];
			for (int i =0; i<anglesL.length; i++) {
				anglesL[i] = 0;
			}
			anglesR = new float[voices.size()];
			for (int i =0; i<anglesR.length; i++) {
				anglesR[i] = 0;
			}
		}
		
		//fakeS = fillSamples();
		
		playing = true;
		track.play();
	}
	
	public void setFreqs(float freqs[]) {
		assert(freqs.length == this.freqs.length);
		this.freqs = freqs.clone();
	}
	
	public void setVolume(float vol) {
		track.setStereoVolume(vol, vol);
	}
	
	public void stopVoices() {
		playing = false;
		track.stop();
	}

	@Override
	public synchronized void run() {
		
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
		
		while(true)
		{
			// Sleep if no voice loaded
			if (!playing) {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// ignore
				}
				continue;
			}
			
			
			//long start = System.currentTimeMillis();
			short[] samples = fillSamples();
			//short[] samples = fakeS;
			//long end = System.currentTimeMillis();
			
			//Log.v(LOGVP, String.format("Calc took %d ms", end-start));
			
			//start = System.currentTimeMillis();
			int out = track.write(samples, 0, samples.length);
			//end = System.currentTimeMillis();

			//Log.v(LOGVP, String.format("Write took %d ms", end-start));
			
			
			if (out != samples.length) {
				Log.v(LOGVP, String.format("IMPOSSIBLE happened"));
				Log.v(LOGVP, String.format("Written %d short out of %d", out, samples.length));
				try {
					sleep(calcFrames*1000/HZ/2);
				} catch (InterruptedException e) {
					// ignore
				}
				continue;
			}
		}
	}

	private short[] fillSamples() {
		assert(freqs != null);
		
		short samples[] = new short[calcFrames*2];
		float ws[] = new float[samples.length];

		for (int j=0; j<freqs.length; j++) { //freqs.length
			float frequency = voicetoPitch(j);
			
			float inc1 = TwoPi * (frequency+freqs[j]) / HZ;
			float inc2 = TwoPi * (frequency) / HZ;
			float angle1 = anglesL[j];
			float angle2 = anglesR[j];
			
			for(int i = 0; i < samples.length; i+=2)
			{
				ws[i] += sinT.sinFast(angle1) * vols[j]; 
				ws[i+1] += sinT.sinFast(angle2) * vols[j];
				angle1 += inc1;
				angle2 += inc2;
			}

			anglesL[j] = ((angle1*720/TwoPi) % 720) * TwoPi / 720;
			anglesR[j] = ((angle2*720/TwoPi) % 720) * TwoPi / 720;
		}
		

		for(int i = 0; i < samples.length; i+=2)
		{
			samples[i] += (short) (ws[i]/freqs.length*Short.MAX_VALUE);
			samples[i+1] += (short) (ws[i+1]/freqs.length*Short.MAX_VALUE);
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