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

public class Starfield implements CanvasVisualization {


	
	private static final int NUM_STARS = 107;
	private static final int MOD_PERIOD = 47;
	private static final int COLOR_2_R = 0XFF;
	private static final int COLOR_2_G = 0XFF;
	private static final int COLOR_2_B = 0XA0;
	private static final int COLOR_BG = Color.rgb(0, 0, 0);
	private static final int DOT_RADIUS = 1;

	private class Star {
		int x;
		int y;
		int speedx;
		int speedy;
		
		public Star(int x, int y, int speedx, int speedy) {
			super();
			this.x = x;
			this.y = y;
			this.speedx = speedx;
			this.speedy = speedy;
		}
	}
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	Paint pStar;
	Paint pBG;
	Star stars[];
	Random r = new Random();
	private Bitmap background;
	int modulo;
	private Rect srcR;
	private Rect dstR;
	
	public Starfield() {
		pStar = new Paint();
		pStar.setStyle(Paint.Style.FILL);
		pBG = new Paint();
		pBG.setStyle(Paint.Style.FILL);
		pBG.setColor(COLOR_BG);
		
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.oobe);
		srcR = new Rect(0,0,background.getWidth(),background.getHeight());
		dstR = new Rect(0,0,0,0);
	}
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		float ratio;
		float dperiod = 5 * period;
		
		ratio = (now % dperiod) / dperiod;
		
		
		if (stars == null) {
			// Init starts
			stars = new Star[NUM_STARS];
			for (int i=0; i<NUM_STARS; i++) {
				stars[i] = new Star(r.nextInt(width), r.nextInt(height), 1+ r.nextInt(4), 0);
			}
			dstR.right = width;
			dstR.bottom = height;
		}
		
		c.drawBitmap(background, srcR, dstR, null);
		
		for (Star s: stars) {
			// move it
			s.x += s.speedx;
			s.y += s.speedy;
			
			if (s.x > width ||  s.y < 0 || s.y > height) {
				
				//modulo++; disable module for now
				if (modulo == MOD_PERIOD) {
					modulo = 0;
					s.x = 0;
					s.y = (int) (height * Math.sin(Math.PI/2*ratio));
					s.speedx = 5;
					s.speedy = 1 + r.nextInt(2);
					if (r.nextBoolean()) {
						s.speedy = - s.speedy;
					}
				} else {
					s.x = 0;
					s.y = r.nextInt(height);
					s.speedx = 1 + r.nextInt(4);
					s.speedy = 0;
				}
			}
			
			if (s.speedx > 4)
				pStar.setColor(Color.rgb(255,255,255));
			else if (s.speedx == 4)
				pStar.setColor(Color.rgb((int) (COLOR_2_R * s.speedx / 4.0 ), (int) (COLOR_2_G * s.speedx / 4.0), (int) (COLOR_2_B * s.speedx * ratio / 4.0)));
			else
				pStar.setColor(Color.rgb((int) (COLOR_2_R * s.speedx / 4.0 ), (int) (COLOR_2_G * s.speedx / 4.0), (int) (COLOR_2_B * s.speedx / 4.0)));
			
			c.drawCircle(s.x,
					s.y,
					DOT_RADIUS + (s.speedx - 1)/2, pStar);
		}
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
