package com.ihunda.android.binauralbeat;

public class Note {
	public static final double A_FREQ = 440.0;
	
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
