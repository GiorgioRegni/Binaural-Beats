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
		p.setAuthor("@GiorgioRegni");

		p.addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.3f, null).
				addVoice(new BinauralBeatVoice(12f, 4f, 0.6f)).
				setV(new HypnoFlash())
		).
		addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 0.3f, null).
				addVoice(new BinauralBeatVoice(4f, 4f, 0.65f)).
				setV(new HypnoFlash())
		).
		addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.3f, null).
				addVoice(new BinauralBeatVoice(4f, 12f, 0.6f)).
				setV(new HypnoFlash())
		);

		return p;
	}

	public static Program AWAKE(Program p) {
		p.setDescription("A quick cafeine boost, this preset shorly reaches Gamma waves, provides "+
		"higher mental activity, including perception, problem solving, and consciousness.");
		p.setAuthor("@GiorgioRegni");

		p.addPeriod(new Period(120, SoundLoop.WHITE_NOISE, 0.3f, null).
				addVoice(new BinauralBeatVoice(12f, 70f, 0.60f)).
				setV(new Leds())
		).
		addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 0.3f, null).
				addVoice(new BinauralBeatVoice(70f, 50f, 0.65f)).
				setV(new Leds())
		).
		addPeriod(new Period(120, SoundLoop.WHITE_NOISE, 0.3f, null).
				addVoice(new BinauralBeatVoice(50f, 12f, 0.60f)).
				setV(new Leds())
		);

		return p;
	}

	public static Program UNITY(Program p) {

		p.setDescription("Wander in deep relaxing delta waves and let your mind explore freely and without bounds." 
				+" May induce dreamless sleep and loss of body awareness.");
		p.setAuthor("@GiorgioRegni");

		p.addPeriod(new Period(3600, SoundLoop.UNITY,  0.5f, null).
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
		p.setAuthor("@GiorgioRegni");

		// From http://www.bwgen.com/presets/desc263.htm
		p.addPeriod(new Period(3600, SoundLoop.UNITY, 0.5f, null).
				addVoice(new BinauralBeatVoice(15f, 0.5f, 0.5f)).
				addVoice(new BinauralBeatVoice(10f, 10f, 0.5f)).
				addVoice(new BinauralBeatVoice(9f, 9f, 0.5f)).
				addVoice(new BinauralBeatVoice(7.5f, 7.5f, 0.50f)).
				addVoice(new BinauralBeatVoice(38f, 38f, 0.50f)).
				setV(new Morphine())
		);

		return p;
	}

	public static Program LEARNING(Program p) {

		p.setDescription("Enhanced learning, ability to concentrate and think clearly increased significantly, reduce unwillingness to work. Students can't get enough of this program.");
		p.setAuthor("@GiorgioRegni");
		p.addPeriod(new Period(120, SoundLoop.NONE, 0.6f, null).
				addVoice(new BinauralBeatVoice(60f, 14f, 0.60f)).
				addVoice(new BinauralBeatVoice(70f, 22f, 0.50f)).
				setV(new Flash())
		).
		addPeriod(new Period(7200-120, SoundLoop.NONE,  0.6f, null).
				addVoice(new BinauralBeatVoice(14f, 14f, 0.6f)).
				addVoice(new BinauralBeatVoice(22f, 22f, 0.5f)).
				addVoice(new BinauralBeatVoice(12f, 12f, 0.4f)).
				addVoice(new BinauralBeatVoice(6f, 6f, 0.4f)).
				setV(new Flash())
		);

		return p;
	}

	public static Program CREATIVITY(Program p) {
		p.setDescription("Meditation to assist in Creative Thinking. " +
		"Begin at 10hz then varying from 8 to 6 hz with a glide back to 8hz at the end.");
		p.setAuthor("@thegreenman");

		p.addPeriod(new Period(15, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(10f, 8f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(240, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(8f, 8f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(15, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(8f, 6f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(240, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(6f, 6f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(15, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(6f, 8f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(555, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(8f, 8f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(15, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(8f, 6f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(75, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(6f, 6f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(15, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(6f, 8f, 0.6f)).
				setV(new Flash())
		).addPeriod(new Period(45, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(8f, 8f, 0.6f)).
				setV(new Flash())
		);

		return p;
	}

	public static Program SCHUMANN_RESONANCE(Program p) {
		p.setDescription("A meditation to put your mind in balance with the Earth." +
				" Glide down to Schumann Resonance, low frequency portion of the Earth's electromagnetic field," +
				" resonate for 10 minutes then" +
		" slowly come back up to awake state.");

		p.setAuthor("@thegreenman");

		p.addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(12f,	7.83f, 0.6f)).
				setV(new Aurora())
		).
		addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(7.83f, 7.83f, 0.65f)).
				setV(new Aurora())
		).
		addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(7.83f, 12f, 0.6f)).
				setV(new Aurora())
		);

		return p;
	}

	public static Program ASTRAL_01_RELAX(Program p) {
		p.setDescription("From the book Mastering Astral Projection, Week 1: Relaxation. " +
				"Use a low to medium sound level, sit in a quiet place, and listen to the preset with eyes closed." +
				" A hard-backed chair without neck support is recommended to prevent falling asleep.");

		p.setAuthor("@GiorgioRegni");

		p.addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(15f,	10f, 0.65f)).
				addVoice(new BinauralBeatVoice(15f,	10f, 0.65f)).
				addVoice(new BinauralBeatVoice(15f,	10f, 0.65f)).
				setV(new Starfield())
		).
		addPeriod(new Period(60*80, SoundLoop.WHITE_NOISE, 0.4f, null).
				addVoice(new BinauralBeatVoice(10f, 10f, 0.60f)).
				addVoice(new BinauralBeatVoice(10f, 10f, 0.60f)).
				addVoice(new BinauralBeatVoice(10f, 10f, 0.60f)).
				addVoice(new BinauralBeatVoice(4.5f, 2.5f, 0.60f)).
				setV(new Starfield())
		);

		return p;
	}
	
	public static Program LSD(Program p) {
		p.setDescription("Lysergic acid diethylamide, abbreviated LSD , " +
				"also known as acid, is a semisynthetic psychedelic drug...");

		p.setAuthor("@GiorgioRegni");
		p.setGL();
		
		p.addPeriod(new Period(600, SoundLoop.WHITE_NOISE, 0.2f, null).
				addVoice(new BinauralBeatVoice(15f,	10f, 0.65f)).
				addVoice(new BinauralBeatVoice(15f,	10f, 0.65f)).
				addVoice(new BinauralBeatVoice(15f,	10f, 0.65f)).
				setV(new Plasma()));

		return p;
	}
	
	public static Program SLEEP_INDUCTION(Program p) {
		
		Visualization v = new None();
		
        p.setDescription("Insomnia relief for use at bed time" +
                        " 6 minute drop into delta then 54 minutes of relaxing delta ");
        p.setAuthor("@thegreenman");

        p.addPeriod(new Period(360, SoundLoop.UNITY, 0.7f, null).
                        addVoice(new BinauralBeatVoice(9.7f, 3.4f, 0.6f)).
          setV(v)
        ).addPeriod(new Period(1620, SoundLoop.UNITY, 0.7f, null).
                        addVoice(new BinauralBeatVoice(3.4f, 2.4f, 0.6f)).
          setV(v)
        ).addPeriod(new Period(1620, SoundLoop.UNITY, 0.7f, null).
                        addVoice(new BinauralBeatVoice(2.4f, 3.4f, 0.6f)).
          setV(v)
        );
        return p;
	}
	
	public static Program LUCID_DREAMS(Program p) {
		
		Morphine m = new Morphine();
        p.setDescription("Stimulates lucid dreaming. Play while sleeping" +
                        " 5hz base frequency with 8 hz spikes.");
        p.setAuthor("@thegreenman");

        p.addPeriod(new Period(60, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(10f, 8f, 0.6f)).
          setV(m)
        ).addPeriod(new Period(60, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(8f, 8f, 0.6f)).
          setV(m)
        ).addPeriod(new Period(60, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(8f, 6f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(60, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(6f, 6f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(120, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(6f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(360, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 8f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(8f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(180, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 8f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(8f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(90, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 8f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(8f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(240, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 8f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(8f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(480, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 8f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(10, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(8f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(360, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 5f, 0.6f)).
                        setV(m)
        ).addPeriod(new Period(480, SoundLoop.NONE, 0.7f, null).
                        addVoice(new BinauralBeatVoice(5f, 12f, 0.6f)).
                        setV(m)
        );
        return p;
	}
	
	public static Program SHAMANIC_RHYTHM(Program p) {
		Visualization v = new None();
		
		p.setDescription("Shamanic Drum Rhythm" +
				"Begin at 9hz then glide down to 4.5hz for 50 minutes and back up at the end.");
                p.setAuthor("@thegreenman");

		p.addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.7f, null).
				addVoice(new BinauralBeatVoice(9f, 4.5f, 0.6f)).
				setV(v)
		).addPeriod(new Period(3000, SoundLoop.WHITE_NOISE, 0.7f, null).
				addVoice(new BinauralBeatVoice(4.5f, 4.5f, 0.6f)).
				setV(v)
		).addPeriod(new Period(300, SoundLoop.WHITE_NOISE, 0.7f, null).
				addVoice(new BinauralBeatVoice(4.5f, 9f, 0.6f)).
				setV(v)
		);

		return p;
	}

}
