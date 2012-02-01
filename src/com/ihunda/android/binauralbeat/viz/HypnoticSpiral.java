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
import android.graphics.Canvas;
import android.graphics.Color;

import com.ihunda.android.binauralbeat.CanvasVisualization;

public class HypnoticSpiral implements CanvasVisualization {
	
	
	private static final int NUM_PRECALC_BITMAPS = 45;
	private static final int NUM_PRECALC_BITMAPS_RATIO = 360/NUM_PRECALC_BITMAPS;
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	
	Bitmap preCalc[];
	
	public HypnoticSpiral() {
		preCalc = new Bitmap[NUM_PRECALC_BITMAPS];
		int width = 300;
		int height = 300;
	    
		for (int c=0; c<360; c+= NUM_PRECALC_BITMAPS_RATIO) {
			Bitmap bm = getBitmap(c, width, height);
			preCalc[c/NUM_PRECALC_BITMAPS_RATIO] = bm;
		}
		
	}
	
	private Bitmap getBitmap(int c, int width, int height) {
		float w2=width/2;
	    float h2=height/2;
	    float w = 20; //(mouseX/5.0);
	    float wPI = (float) (w/Math.PI);
	    
	    int img[] = new int[width*height];
		    		    
	    int base = 0;
	    for (int y = 0; y < height; y++) {
	    	float dy = h2-y;

	    	for (int x = 0; x < width; x++) {
	    		float dx = w2-x;

	    		float angle = c + (float) (Math.atan2(dy,dx)) * wPI;
	    		float dist = (float) (Math.sqrt(dx*dx+dy*dy));
	    		int d = (int) ((angle+dist)/w);
	    		if (( d & 1 ) == 1) {
	    			img[x+base] = Color.WHITE;
	    		}
	    	}
	    	base+=width;
	    }

	    Bitmap bm = Bitmap.createBitmap(img, width, height, Bitmap.Config.RGB_565);
	    return bm;
	}

	
	public void redraw(Canvas ca, int width, int height, float now,
			float totalTime) {
		float dperiod = period * 60;
		float ratio = (now % dperiod) / dperiod;
		
		int c = (int) (ratio*NUM_PRECALC_BITMAPS);
		Bitmap bm = preCalc[c];
			
		ca.drawColor(Color.BLACK);
		ca.drawBitmap(bm, (width-300)/2, (height-300)/2, null);
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1 / beat_frequency;
	}

}
