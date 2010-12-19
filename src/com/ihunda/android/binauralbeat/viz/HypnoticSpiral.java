package com.ihunda.android.binauralbeat.viz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.ihunda.android.binauralbeat.Visualization;

public class HypnoticSpiral implements Visualization {
	
	
	private static final int NUM_PRECALC_BITMAPS = 90;
	private static final int NUM_PRECALC_BITMAPS_RATIO = 360/NUM_PRECALC_BITMAPS;
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	double period;
	
	Bitmap preCalc[];
	
	public HypnoticSpiral() {
		preCalc = new Bitmap[NUM_PRECALC_BITMAPS];
		int width = 300;
		int height = 300;
	    double w2=width/2;
	    double h2=height/2;
	    double w = 20; //(mouseX/5.0);
	    double wPI = w/Math.PI;
	    
		for (int c=0; c<360; c+= NUM_PRECALC_BITMAPS_RATIO) {
		    int img[] = new int[width*height];
		    		    
		    int base = 0;
		    for (int y = 0; y < height; y++) {
				double dy = h2-y;
			    
				for (int x = 0; x < width; x++) {
					double dx = w2-x;
					
					double angle = c + Math.atan2(dy,dx) * wPI;
				    double dist = Math.sqrt(dx*dx+dy*dy);
				    int d = (int) ((angle+dist)/w);
				    if (( d & 1 ) == 1) {
				    	img[x+base] = Color.WHITE;
				    }
				}
				base+=width;
			}
		    
			Bitmap bm = Bitmap.createBitmap(img, width, height, Bitmap.Config.RGB_565);
			preCalc[c/NUM_PRECALC_BITMAPS_RATIO] = bm;
		}
		
	}

	
	public void redraw(Canvas ca, int width, int height, float now,
			float totalTime) {
		double dperiod = period * 60;
		double ratio = (now % dperiod) / dperiod;
		
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
