package com.ihunda.android.binauralbeat;

public class BinauralBeatVoice {
	private static final float A440 = 440f;

	/**
	 * Beat frequency in Hz
	 */
	float freqStart;
	float freqEnd;
	
	/**
	 * Beat volume 0f-1f
	 */
	float volume;
	
	/**
	 * 440 = default
	 */
	float pitch;

	public BinauralBeatVoice(float freqStart, float freqEnd, float volume, float pitch) {
		super();
		this.freqStart = freqStart;
		this.freqEnd = freqEnd;
		this.volume = volume;
		this.pitch = pitch;
	}

	public BinauralBeatVoice(float freqStart, float freqEnd, float volume) {
		super();
		this.freqStart = freqStart;
		this.freqEnd = freqEnd;
		this.volume = volume;
		this.pitch = A440;
	}	
}
