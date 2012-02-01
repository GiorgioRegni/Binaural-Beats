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

public class Leds implements CanvasVisualization {

	private static final int COLOR_LED = Color.rgb(200, 30, 30);
	private static final int COLOR_BG = Color.rgb(0, 0, 0);
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	Paint pLed;
	
	public Leds() {
		pLed = new Paint();
		pLed.setStyle(Paint.Style.FILL);
		pLed.setColor(COLOR_LED);
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		int ledRadius = width / 8;
		float ratio;
		float dperiod = period * 2 * 2 * 2;
		
		ratio = (now % dperiod) / dperiod;
		
		c.drawColor(COLOR_BG);
		
		if (ratio > 0.5f) {
			c.drawCircle(width/5, height/3, (int) (ledRadius*(ratio-0.5f)*2f), pLed);
		}
		else {
			c.drawCircle(4*width/5, 2*height/3, (int) (ledRadius*(0.5f-ratio)*2f), pLed);
		}
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
