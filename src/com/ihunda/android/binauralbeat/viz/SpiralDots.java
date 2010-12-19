package com.ihunda.android.binauralbeat.viz;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ihunda.android.binauralbeat.Visualization;

public class SpiralDots implements Visualization {

	private static final int COLOR_1 = Color.rgb(0x77, 0x88, 0x77);
	private static final int COLOR_2_R = 0xaa;
	private static final int COLOR_2_G = 0xbb;
	private static final int COLOR_2_B = 0xbb;
	private static final int COLOR_BG = Color.rgb(0, 0, 0);
	private static final int DOT_RADIUS = 10;
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	double period;
	Paint pLed;
	
	public SpiralDots() {
		pLed = new Paint();
		pLed.setStyle(Paint.Style.FILL);
	}
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		int r = (Math.min(width, height) - DOT_RADIUS) / 2;
		double ratio;
		double phase;
		double dperiod = 6 * period;
		
		ratio = (now % dperiod) / dperiod;
		phase = - ratio * 2 * Math.PI;
		c.drawColor(COLOR_BG);
		
		pLed.setColor(COLOR_1);
		
		double inc = 0.15;
		for (double i=0; i<1; i+=inc) {
			float x = width/2 + (int) (Math.cos(phase + 2*Math.PI*i*4)*r*i);
			float y = height/2 + (int) (Math.sin(phase + 2*Math.PI*i*4)*r*i);
			
			pLed.setColor(Color.rgb((int) (COLOR_2_R *(0.1+i)), (int) (COLOR_2_G *(0.1+i)), (int) (COLOR_2_B *(0.1+i))));
			c.drawCircle(x,
					y,
					(int) (1+DOT_RADIUS*i), pLed);
			inc -= 0.01;
		}
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
