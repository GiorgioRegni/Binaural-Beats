package com.ihunda.android.binauralbeat.viz;

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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ihunda.android.binauralbeat.CanvasVisualization;

public class SpiralDots implements CanvasVisualization {

	private static final int COLOR_1 = Color.rgb(0x99, 0xee, 0x99);
	private static final int COLOR_2_R = 0x99;
	private static final int COLOR_2_G = 0xee;
	private static final int COLOR_2_B = 0x99;
	private static final int COLOR_BG = Color.rgb(0, 0, 0);
	private static final int DOT_RADIUS = 10;
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	Paint pLed;
	float Twopi;
	
	public SpiralDots() {
		pLed = new Paint();
		pLed.setStyle(Paint.Style.FILL);
		Twopi = (float) (2 * Math.PI);
	}
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		int r = (Math.min(width, height) - DOT_RADIUS) / 2;
		float ratio;
		float phase;
		float dperiod = 8 * period;
		
		ratio = (now % dperiod) / dperiod;
		phase = - ratio * Twopi;
		c.drawColor(COLOR_BG);
		
		pLed.setColor(COLOR_1);
		
		float inc = 0.20f;
		for (float i=0; i<1; i+=inc) {
			float x = width/2 + (int) (Math.cos(phase + Twopi*4)*r*i);
			float y = height/2 + (int) (Math.sin(phase + Twopi*i*4)*r*i);
			
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
