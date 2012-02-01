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

import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.CanvasVisualization;
import com.ihunda.android.binauralbeat.R;

public class Starfield3D implements CanvasVisualization {


	
	private static final int NUM_STARS = 191;
	private static final int MOD_PERIOD = 47;
	private static final int COLOR_BG = Color.rgb(0, 0, 0);
	private static final int DOT_RADIUS = 1;

	private static final int FOCALE = 2000;
	private static final int SPEED = 2;
	private static final int VIEWPORT = 60;
	
	private int z_to_color[];
	
	private class Star {
		int x;
		int y;
		float z;
		
		public Star(int x, int y, int z) {
			super();
			set(x,y,z);
		}
		
		public void set(int x, int y, int z) {
			this.x = x * FOCALE;
			this.y = y * FOCALE;
			this.z = z;
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
	
	public Starfield3D() {
		pStar = new Paint();
		pStar.setStyle(Paint.Style.FILL);
		pBG = new Paint();
		pBG.setStyle(Paint.Style.FILL);
		pBG.setColor(COLOR_BG);
		
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.oobe);
		
		z_to_color = new int[NUM_STARS+1];
		for (int i = 0; i < NUM_STARS; i++) {
			int val = 66 + (NUM_STARS-i) * (255-66) / NUM_STARS;
			z_to_color[i] = Color.rgb(val,val,val);
		}
		z_to_color[NUM_STARS] = Color.rgb(0,0,0);
	}
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		//float ratio;
		//float dperiod = 5 * period;
		int w2 = width / 2;
		int h2 = height / 2;
		int speed;

		//ratio = (now % dperiod) / dperiod;
		speed = (int) (SPEED + freq/4);

		//c.drawColor(COLOR_BG);
		c.drawBitmap(background, 0, 0, null);

		if (stars == null) {

			// Init starts
			stars = new Star[NUM_STARS];
			for (int i=0; i<NUM_STARS; i++) {
				stars[i] = new Star(-VIEWPORT/2 + r.nextInt(VIEWPORT),
						-VIEWPORT/2 + r.nextInt(VIEWPORT),
						1 + i);
			}
		}

		for (Star s: stars) {
			boolean ok = false;
			int xp = 0, yp = 0;

			// move it
			s.z -= speed;

			if (s.z > 0) {

				xp = (int) (s.x / s.z) + w2;
				yp = (int) (s.y / s.z) + h2;

				if (xp < 0 || xp > width ||  yp < 0 || yp > height) {

				}
				else
					ok = true;
			}

			if (ok) {
				pStar.setColor(z_to_color[(int) Math.floor(s.z)]);
				c.drawCircle(xp,
						yp,
						DOT_RADIUS, pStar);
			} else {
				s.set(-VIEWPORT/2 + r.nextInt(VIEWPORT), -VIEWPORT/2 + r.nextInt(VIEWPORT), 1 + r.nextInt(NUM_STARS));
			}
		}
	}


	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
