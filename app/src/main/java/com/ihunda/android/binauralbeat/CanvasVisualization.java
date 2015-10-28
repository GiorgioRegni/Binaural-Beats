package com.ihunda.android.binauralbeat;

import android.graphics.Canvas;

public interface CanvasVisualization extends Visualization {
	
	/**
	 * Called at each beat,
	 * total time is the entire length of the current period.
	 * 
	 * @param c      Canvas element to redraw into
	 * @param width  Canvas width
	 * @param height Canvas height
	 * @param now    Position in the current period
	 * @param totalTime Entire length of the current period.
	 */
	void redraw(Canvas c, int width, int height, float now, float totalTime);
}
