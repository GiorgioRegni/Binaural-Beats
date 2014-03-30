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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.CanvasVisualization;

public class Image implements CanvasVisualization {

	/**
	 * Beat frequency in Hz
	 */
	//private float period;
	int last = 0;
	private Bitmap background;
	private Rect srcR;
	private Rect dstR;
	
	public Image(int id) {
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), id);
		last = 0;
		
		srcR = new Rect(0,0,background.getWidth(),background.getHeight());
		dstR = new Rect(0,0,0,0);
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		
		if (last++ % 50 == 0 || now < 1)
		{
			dstR.right = width;
			dstR.bottom = height;
			c.drawBitmap(background, srcR, dstR, null);
			
			//Log.w("ABC", String.format("%f", now));
		}	
	}

	public void setFrequency(float beat_frequency) {
		//period = 1f / beat_frequency;
	}

}
