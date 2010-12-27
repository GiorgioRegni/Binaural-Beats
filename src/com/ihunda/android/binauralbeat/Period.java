package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

public class Period {
	/**
	 * Length of this period in second
	 */
	int length;
	ArrayList<BinauralBeatVoice> voices;
	
	SoundLoop  background;
	private float backgroundvol;
	
	Visualization v;
	
	public Period(int length, SoundLoop background,
			float backgroundvol, Visualization v) {
		super();
		this.length = length;
		this.background = background;
		this.setBackgroundvol(backgroundvol);
		this.v = v;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public SoundLoop getBackground() {
		return background;
	}
	
	public void setBackground(SoundLoop background) {
		this.background = background;
	}

	public void setBackgroundvol(float backgroundvol) {
		this.backgroundvol = backgroundvol;
	}

	public float getBackgroundvol() {
		return backgroundvol;
	}

	public Visualization getV() {
		return v;
	}

	public Period setV(Visualization v) {
		this.v = v;
		return this;
	}

	public ArrayList<BinauralBeatVoice> getVoices() {
		return voices;
	}

	public void setVoices(ArrayList<BinauralBeatVoice> voices) {
		this.voices = voices;
	}
	
	public Period addVoice(BinauralBeatVoice v) {
		if (voices == null)
			voices = new ArrayList<BinauralBeatVoice>();
		voices.add(v);
		return this;
	}
	
}
