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

import com.ihunda.android.binauralbeat.viz.*;

public class DefaultProgramsBuilder {
	
	public static Program SELF_HYPNOSIS(Program p) {
		p.setDescription("Short meditation preset to unite your conscious and subconsious mind. " +
				"Glide down to theta waves, plateau for 10 minutes then slowly come back up to awake state.");
		
		p.addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.8f, null).
				addVoice(new BinauralBeatVoice(12f, 4f, 0.6f)).
				setV(new HypnoFlash())
		).
		addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 0.8f, null).
				addVoice(new BinauralBeatVoice(4f, 4f, 0.65f)).
				setV(new HypnoFlash())
		).
		addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.8f, null).
				addVoice(new BinauralBeatVoice(4f, 12f, 0.6f)).
				setV(new HypnoFlash())
		);
		
		return p;
	}
	
	public static Program AWAKE(Program p) {
		p.setDescription("A quick cafeine boost, this preset shorly reaches Gamma waves, provides "+
				"higher mental activity, including perception, problem solving, and consciousness.");
		
		p.addPeriod(new Period(120, SoundLoop.WHITE_NOISE, 0.8f, null).
				addVoice(new BinauralBeatVoice(12f, 70f, 0.60f)).
				setV(new Leds())
		).
		addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 0.8f, null).
				addVoice(new BinauralBeatVoice(70f, 50f, 0.65f)).
				setV(new Leds())
		).
		addPeriod(new Period(120, SoundLoop.WHITE_NOISE, 0.8f, null).
				addVoice(new BinauralBeatVoice(50f, 12f, 0.60f)).
				setV(new Leds())
		);
		
		return p;
	}
	
	public static Program UNITY(Program p) {
		
		p.setDescription("Wander in deep relaxing delta waves and let your mind explore freely and without bounds." 
				+" May induce dreamless sleep and loss of body awareness.");
		
		p.addPeriod(new Period(3600, SoundLoop.UNITY,  0.8f, null).
				addVoice(new BinauralBeatVoice(3.7f, 3.7f, 0.6f)).
				addVoice(new BinauralBeatVoice(2.5f, 2.5f, 0.6f)).
				addVoice(new BinauralBeatVoice(5.9f, 5.9f, 0.6f)).
				setV(new Aurora())
		);
		
		return p;
	}
	
	public static Program MORPHINE(Program p) {
		
		p.setDescription("A relaxing, southing mix of beats that slowly but surely appease any pain point."
				+ " The brain runs the body as you will discover with this preset.");
		
		// From http://www.bwgen.com/presets/desc263.htm
		p.addPeriod(new Period(3600, SoundLoop.UNITY, 0.8f, null).
				addVoice(new BinauralBeatVoice(15f, 0.5f, 0.5f)).
				addVoice(new BinauralBeatVoice(10f, 10f, 0.5f)).
				addVoice(new BinauralBeatVoice(9f, 9f, 0.5f)).
				addVoice(new BinauralBeatVoice(7.5f, 7.5f, 0.50f)).
				addVoice(new BinauralBeatVoice(38f, 38f, 0.50f)).
				setV(new Morphine())
		);
		
		return p;
	}
}
