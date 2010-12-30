package com.ihunda.android.binauralbeat.viz;

import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import com.ihunda.android.binauralbeat.Visualization;

public class Aurora implements Visualization {	
	
	/**
	 * Beat frequency in Hz
	 */
	double freq;
	double period;
	Bitmap background;
	
	public Aurora() {
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.aurora);
	}
	
 	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		// window [-2.0, 1.0, -1.5, 1.5]
		
		double dperiod = period * 2 * 200;
	
		double ratio = (now % dperiod) / dperiod;
		
		/*
		Bitmap bm = Bitmap.createBitmap(img, N, N, Bitmap.Config.ARGB_4444);
		
		// Paste
		int wm = width/N;
		double wmd = (double) width/N/wm;
		int hm = height/N;
		double hmd = (double) height/N/hm;
		
		for (int i=0; i< wm; i++) {
			for (int j=0; j< hm; j++) {
				c.drawBitmap(bm, (int) (i*N*wmd) , (int) (j*N*hmd), null);
			}
		}
		*/
		c.drawBitmap(background, 0, 0, null);
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
