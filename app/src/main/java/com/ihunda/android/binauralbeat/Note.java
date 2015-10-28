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

public class Note {
	public static final double A_FREQ = 432.0; // The nicest pitch http://www.omega432.com/music.html
	
	public enum NoteK {A, AD, B, C, CD, D, DD, E, F, FD, G, GD}; 
	int octave; // 4 = A4
	
	double k;
	
	public Note() {
		k = NoteK.A.ordinal();
		octave = 4;
	}
	
	public Note(Note n) {
		k = n.k;
		octave = n.octave;
	}
	
	public Note(NoteK k) {
		this.k = k.ordinal();
		this.octave = 4;
	}
	
	public Note(NoteK k, int octave) {
		this.k = k.ordinal();
		this.octave = octave;
	}

	public Note(double k) {
		this.k = k;
		this.octave = 4;
	}
	
	public Note(double k, int octave) {
		this.k = k;
		this.octave = octave;
	}
	
	public void alterNote(float offset) {
		k += offset;
	}
	
	public double getPitchFreq() {
		double pos = (double) k;
		return Math.pow(2, pos/12.0 + octave - 4)*A_FREQ;
	}
}
