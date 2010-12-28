package com.ihunda.android.binauralbeat.viz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ihunda.android.binauralbeat.Visualization;

public class Glow implements Visualization {

	private static final int COLOR_LED = Color.rgb(120, 255, 30);
	private static final int COLOR_BG = Color.rgb(0, 0, 0);
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	Paint pLed;
	
	public Glow() {
		pLed = new Paint();
		pLed.setStyle(Paint.Style.FILL);
		pLed.setColor(COLOR_LED);
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		int ledRadius = width / 2;
		float ratio;
		float dperiod = period * 2 * 2 * 2;
		
		ratio = (now % dperiod) / dperiod;
		
		c.drawColor(COLOR_BG);
		
		if (ratio > 0.5f) {
			c.drawCircle(width/2, height/2, (int) (ledRadius*(ratio-0.5f)*2f), pLed);
		}
		else {
			c.drawCircle(width/2, height/2, (int) (ledRadius*(0.5f-ratio)*2f), pLed);
		}
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
