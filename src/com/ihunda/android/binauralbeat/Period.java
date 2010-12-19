package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

public class Period {
	/**
	 * Length of this period in second
	 */
	int length;
	ArrayList<BinauralBeatVoice> voices;
	
	SoundLoop  background;
	SoundLoop  effect;
	
	Visualization v;
	
	/**
	 * 0 for always looping effect,
	 * any other value controls when to replay effect in seconds
	 */
	float effectPeriod;

	public Period(int length, SoundLoop background,
			SoundLoop effect, Visualization v, float effectPeriod) {
		super();
		this.length = length;
		this.background = background;
		this.effect = effect;
		this.v = v;
		this.effectPeriod = effectPeriod;
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

	public SoundLoop getEffect() {
		return effect;
	}

	public void setEffect(SoundLoop effect) {
		this.effect = effect;
	}

	public float getEffectPeriod() {
		return effectPeriod;
	}

	public void setEffectPeriod(float effectPeriod) {
		this.effectPeriod = effectPeriod;
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
