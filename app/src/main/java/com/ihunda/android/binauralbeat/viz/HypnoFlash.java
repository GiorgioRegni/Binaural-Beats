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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.CanvasVisualization;
import com.ihunda.android.binauralbeat.R;

public class HypnoFlash implements CanvasVisualization {

	private static final int COLOR_FLASH1 = Color.argb(100, 255, 255, 255);
	private static final int COLOR_FLASH2 = Color.argb(150, 255, 255, 255);
	
	/**
	 * Beat frequency in Hz
	 */
	private float period;
	private Paint pFlash1;
	private Paint pFlash2;
	
	private Bitmap background;
	private Rect srcR;
	private Rect dstR;
	
	public HypnoFlash() {
		pFlash1 = new Paint();
		pFlash1.setStyle(Paint.Style.FILL);
		pFlash1.setColor(COLOR_FLASH1);
		
		pFlash2 = new Paint();
		pFlash2.setStyle(Paint.Style.FILL);
		pFlash2.setColor(COLOR_FLASH2);
		
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.hypnosisspiral);

		srcR = new Rect(0,0,background.getWidth(),background.getHeight());
		dstR = new Rect(0,0,0,0);
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		float ratio;
		float dperiod = period * 2 * 2;
		
		ratio = (now % dperiod) / dperiod;
	
		if (ratio < 0.8) {
			dstR.right = width;
			dstR.bottom = height;
			c.drawBitmap(background, srcR, dstR, null);
		}
		else {
			Random r = new Random();
			if (r.nextBoolean())
				c.drawColor(COLOR_FLASH1);
			else
				c.drawColor(COLOR_FLASH2);
		}
	}

	public void setFrequency(float beat_frequency) {
		period = 1f / beat_frequency;
	}

}
