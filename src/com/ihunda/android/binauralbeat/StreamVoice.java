package com.ihunda.android.binauralbeat;

/**
 * @author giorgio
 *
 * This class is used to keep track of android soundpool streams
 * 
 */
public class StreamVoice {

	public int streamID;
	public float leftVol;
	public float rightVol;
	public int loop;
	public float rate;
	
	public StreamVoice(int streamID, float leftVol, float rightVol, int loop,
			float rate) {
		super();
		this.streamID = streamID;
		this.leftVol = leftVol;
		this.rightVol = rightVol;
		this.loop = loop;
		this.rate = rate;
	}
}
