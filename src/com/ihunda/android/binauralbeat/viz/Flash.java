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

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ihunda.android.binauralbeat.CanvasVisualization;

public class Flash implements CanvasVisualization {

	private static final int COLOR_FLASH1 = Color.rgb(30, 30, 200);
	private static final int COLOR_FLASH2 = Color.rgb(150, 150, 240);
	private static final int COLOR_BG = Color.rgb(0, 0, 0);
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	Paint pFlash1;
	Paint pFlash2;
	
	public Flash() {
		pFlash1 = new Paint();
		pFlash1.setStyle(Paint.Style.FILL);
		pFlash1.setColor(COLOR_FLASH1);
		
		pFlash2 = new Paint();
		pFlash2.setStyle(Paint.Style.FILL);
		pFlash2.setColor(COLOR_FLASH2);
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		float ratio;
		float dperiod = period * 2;
		
		ratio = (now % dperiod) / dperiod;
	
		if (ratio < 0.8) 
			c.drawColor(COLOR_BG);
		else {
			Random r = new Random();
			if (r.nextBoolean())
				c.drawColor(COLOR_FLASH1);
			else
				c.drawColor(COLOR_FLASH2);
		}
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
