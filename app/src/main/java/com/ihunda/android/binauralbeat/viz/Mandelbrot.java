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
import android.graphics.Canvas;
import android.graphics.Color;

import com.ihunda.android.binauralbeat.Visualization;

public class Mandelbrot implements Visualization {
	private static final int PALETTE_SIZE = 768;
	private static final double ESCAPE = 4;
	private static final double LOG_ESCAPE = 1.38629436;
	
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	
	private int palette[] = new int[PALETTE_SIZE];
	
	public Mandelbrot() {
		palette[0] = Color.BLACK;
		for (int i=1; i<palette.length; i++) {
			palette[i] = Color.argb(255, i % 256, (i - 47) % 200, (i + 83) % 200);
		}
	}
	
    // return number of iterations to check if c = a + ib is in Mandelbrot set
    public double mand(double re, double im, int max) {
    	double re0 = re;
    	double im0 = im;
    	double xtemp;
    	
    	for (int t = 0; t < max; t++) {
    		double re_re = re * re;
    		double im_im = im * im;
    		double z = re_re + im_im;
    		
            if (z > ESCAPE) {
                double vz = t - Math.log(Math.log(z)/LOG_ESCAPE);
            	return vz;
            }
            
            xtemp = re_re - im_im + re0;
            im = 2*re*im + im0;

            re = xtemp;
        }
        return 0;
    }
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		// window [-2.0, 1.0, -1.5, 1.5]
		
		float dperiod = period * 2 * 200;
		//double xc = -0.742522478103764;
        //double yc = -0.143708014488453;
		double xc = -0.10894500736830963;
		double yc = -0.8955496975621973;
		xc = -0.10535958795257323;
		yc = -0.9237887438236229;
		double zoom;
        int N   = 80; //Math.min(width, height);   // create N-by-N image
        int max = 40;   // maximum number of iterations
        
		float ratio = (now % dperiod) / dperiod;
		
		if (ratio > 0.5)
			zoom = 1/((1-ratio)+0.3);
		else
			zoom = 1/(ratio+0.3);
		zoom = 1 / (now + 0.1);
		
		//xc = xc * Math.cos(2*Math.PI*ratio);
		//yc = yc * Math.sin(2*Math.PI*ratio);
		
	    double size_div_2 = zoom/2;
	    double size_inc = zoom/N;
	    
	    int img[] = new int[N*N];
	    int base = 0;
	    
		for (int i = 0; i < N; i++) {
			double x0 = xc - size_div_2 + size_inc*i;
			for (int j = 0; j < N; j++) {
				double y0 = yc - size_div_2 + size_inc*j;
				
				int cr = Math.abs((int) (mand(x0, y0, max)/max * PALETTE_SIZE));
				/*if (cr >= PALETTE_SIZE)
					cr = 0;*/
				img[j+base] = palette[cr % PALETTE_SIZE];
			}
			base += N;
		}
		Bitmap bm = Bitmap.createBitmap(img, N, N, Bitmap.Config.ARGB_4444);
		
		/* Paste the bitmap multiple times */
		int wm = width/N;
		double wmd = (double) width/N/wm;
		int hm = height/N;
		double hmd = (double) height/N/hm;
		
		for (int i=0; i< wm; i++) {
			for (int j=0; j< hm; j++) {
				c.drawBitmap(bm, (int) (i*N*wmd) , (int) (j*N*hmd), null);
			}
		}
		
		

	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
