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

import com.ihunda.android.binauralbeat.R;
import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.CanvasVisualization;

public class Hiit implements CanvasVisualization {

	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	Paint pLed;
	
	//private static final int COLOR_BG_REST = Color.rgb(70, 147, 171);
	
	private Bitmap bgWork;
	private Bitmap bgRest;
	private Rect srcR;
	private Rect dstR;
	private Random r;
	boolean isWork = true;
	
	public Hiit() {
		pLed = new Paint();
		pLed.setStyle(Paint.Style.FILL);
		
		bgWork = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.hiit_work);
		bgRest = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.hiit_rest);
		
		srcR = new Rect(0,0,bgWork.getWidth(),bgWork.getHeight());
		dstR = new Rect(0,0,0,0);
		
		r = new Random();
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		int ledRadius = width / 8;
		float ratio;
		float dperiod = period*50;
		float trans;
		
		dstR.right = width;
		dstR.bottom = height;
		
		ratio = (now % dperiod) / dperiod;
		
		if (ratio > 0.5)
			trans = (1-ratio) * 200;
		else
			trans = ratio * 200;
		
		if (isWork) {
			c.drawBitmap(bgWork, srcR, dstR, null);

			float wx = r.nextFloat();
			float wy = r.nextFloat();

			
			pLed.setColor(Color.argb((int) trans, 255, 30, 30));

			if (ratio > 0.5f) {
				c.drawCircle(width/10+8*width/10*wx, height/10+8*height/10*wy, (int) (ledRadius*(ratio-0.5f)*2f), pLed);
			}
			else {
				c.drawCircle(width/10+8*width/10*wx, height/10+8*height/10*wy, (int) (ledRadius*(0.5f-ratio)*2f), pLed);
			}
		} else {
			c.drawBitmap(bgRest, srcR, dstR, null);
			
			float radius = width/2;
			
			pLed.setColor(Color.argb((int) (trans*0.7), 255, 255, 255));
			c.drawCircle(width/2, height/2, radius, pLed);
		}
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
		
		if (freq < 20)
			isWork = false;
		else
			isWork = true;
	}

}
