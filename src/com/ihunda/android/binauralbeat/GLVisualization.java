package com.ihunda.android.binauralbeat;

import javax.microedition.khronos.opengles.GL11;

public interface GLVisualization extends Visualization {
	
	void setup(GL11 gl, int width, int height);
	
	/**
	 * Called at each beat,
	 * total time is the entire length of the current period.
	 * 
	 * @param gl     OpenGL object
	 * @param width  Canvas width
	 * @param height Canvas height
	 * @param now    Position in the current period
	 * @param totalTime Entire length of the current period.
	 */
	void redraw(GL11 gl, int width, int height, float now, float totalTime);
	
	void dispose(GL11 gl);
}
