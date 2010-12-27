package com.ihunda.android.binauralbeat;

import com.ihunda.android.binauralbeat.viz.Flash;
import com.ihunda.android.binauralbeat.viz.Leds;

public class DefaultProgramsBuilder {
	
	public static Program SELF_HYPNOSIS(Program p) {
		p.setDescription("Deep meditation preset to unite your conscious and subconsious mind. " +
				"Glides down to theta waves, plateau for 10 minutes then slowly come back up to awake state.");
		
		p.addPeriod(new Period(300, SoundLoop.OCEAN_WAVES, 1f, null).
				addVoice(new BinauralBeatVoice(12f, 4f, 1f)).
				setV(new Flash())
		).
		addPeriod(new Period(600, SoundLoop.OCEAN_WAVES, 1f, null).
				addVoice(new BinauralBeatVoice(4f, 4f, 0.9f)).
				setV(new Flash())
		).
		addPeriod(new Period(300, SoundLoop.OCEAN_WAVES, 1f, null).
				addVoice(new BinauralBeatVoice(4f, 12f, 0.9f)).
				setV(new Flash())
		);
		
		return p;
	}
	
	public static Program AWAKE(Program p) {
		p.addPeriod(new Period(120, SoundLoop.WHITE_NOISE, 1f, null).
				addVoice(new BinauralBeatVoice(12f, 70f, 0.25f)).
				setV(new Leds())
		).
		addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 1f, null).
				addVoice(new BinauralBeatVoice(70f, 50f, 0.35f)).
				setV(new Leds())
		).
		addPeriod(new Period(120, SoundLoop.WHITE_NOISE, 1f, null).
				addVoice(new BinauralBeatVoice(50f, 12f, 0.25f)).
				setV(new Leds())
		);
		
		return p;
	}
}
