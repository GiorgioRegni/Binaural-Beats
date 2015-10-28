package com.ihunda.android.binauralbeat;

public interface VizualisationView {

	public void startVisualization(Visualization v, float length);
	public void stopVisualization();
	public void setProgress(float pos);
	public void setFrequency(float freq) ;
	
}
