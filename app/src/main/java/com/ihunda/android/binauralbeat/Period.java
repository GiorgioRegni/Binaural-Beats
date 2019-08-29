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

import java.util.ArrayList;

public class Period {
    /**
     * Length of this period in second
     */
    int length;
    ArrayList<BinauralBeatVoice> voices;

    SoundLoop background;
    private float backgroundvol;

    Visualization v;

    boolean strechable;

    public Period(int length, SoundLoop background,
                  float backgroundvol, Visualization v) {
        super();
        this.length = length;
        this.background = background;
        this.setBackgroundvol(backgroundvol);
        this.v = v;
        this.strechable = false;
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

    public Period addHarmonicBox(float freq, float pitch, float volume) {
		/*
		 * From the forum
		 * http://www.binaural-beats.ihunda.com/forum/viewtopic.php?f=4&t=6
		 * Step 1) Add voice 1, assign pitch and beat as above

		Step 2) Add voice 2
		Step 3) For voice 2 pitch, multiply beat by 2 and subtract from
		pitch. So pitch - (beat * 2) or 500 - (15 * 2) = 470
		Step 4) For voice 2 beat, multiply the voice 1 beat (15) by -1, which
		would make it -15

		And that's it! You got yourself a harmonic box X

		The pitches are now as follows:

		V1: 507.5 | 492.5
		V2: 462.5 | 477.5

		507.5 - 492.5 = 15
		462.5 - 477.5 = -15
		492.5 - 477.5 = 15 [monaural]
		507.5 - 477.5 = 30 [crossover freq]
		492.5 - 462.5 = 30 [crossover freq]
		507.5 - 462.5 = 45 [monaural]
		 */

        BinauralBeatVoice v1 = new BinauralBeatVoice(freq, freq, volume, pitch);
        BinauralBeatVoice v2 = new BinauralBeatVoice(-freq, -freq, volume, pitch - freq * 2);

        addVoice(v1);
        addVoice(v2);

        return this;
    }


    public boolean isStrechable() {
        return strechable;
    }

    public Period setStrechable() {
        this.strechable = true;
        return this;
    }

    public float getMainBeatStart() {
        return voices.get(0).freqStart;
    }

    public float getMainBeatEnd() {
        return voices.get(0).freqEnd;
    }
}
