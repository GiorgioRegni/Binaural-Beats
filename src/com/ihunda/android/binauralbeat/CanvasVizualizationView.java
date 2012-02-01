package com.ihunda.android.binauralbeat;

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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CanvasVizualizationView extends SurfaceView implements Callback, VizualisationView {
	
	protected static final long DRAW_REFRESH_INTERVAL_NS = 1000 * 1000 * 1000 / 16; // # time between refreshes
	protected static final long DRAW_REFRESH_INTERVAL_MIN_NS = 1000 * 1000 * 1000 / 20; // # time between refreshes
	private static final String LOGVIZVIEW = "BBT-VIZ";
	private SurfaceHolder mSurfaceHolder;
	private int width;
	private int height;
	private CanvasVisualization v;
	private boolean running;
	
	protected float pos;
	protected float length;
	
	private Thread vThread;
	
	public CanvasVizualizationView(Context context) {
		super(context);
		// register our interest in hearing about changes to our surface
        mSurfaceHolder  = getHolder();
        mSurfaceHolder.addCallback(this);
        
        setFocusable(true); // make sure we get key events
        v = null;
        running = false;
	}
	
    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    	this.width = width;
    	this.height = height;
   }
    
	public void surfaceCreated(SurfaceHolder holder) {
		if (vThread == null) {
			Log.e(LOGVIZVIEW, "SURFACE NEW THREAD");	
			running = true;
			vThread = new Thread(vizThread);
			vThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		running = false;
		boolean retry = true;
		
		if (vThread != null)  {
			while (retry) {
				try {
					vThread.join();
					retry = false;
					vThread = null;
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	public void startVisualization(Visualization v, float length) {
		drawClear();
		
		if (v == null)
			return;
		
		this.v = (CanvasVisualization) v;
		this.pos = 0;
		this.length = length;
				
		running = true;
		if (vThread == null) {
			Log.e(LOGVIZVIEW, "START VIZ NEW THREAD");
				
			vThread = new Thread(vizThread);
			vThread.start();
		}
	}
	
	public void stopVisualization() {
		boolean retry = true;
		
		this.v = null;
		running = false;
		
		if (vThread != null)  {
			while (retry) {
				try {
					vThread.join();
					retry = false;
					vThread = null;
				} catch (InterruptedException e) {
				}
			}
		}
		drawClear();
	}
	
	public void setProgress(float pos) {
		this.pos = pos;
	}
	
	public void setFrequency(float freq) {
		if (v != null)
			v.setFrequency(freq);
	}
	
	void drawMain(float now, float length) {
		Canvas c = null;
		try {
			c = mSurfaceHolder.lockCanvas(null);
			synchronized (mSurfaceHolder) {
				if (c != null && v != null ) {
					v.redraw(c, width, height, now, length);
				}
			} 
		}
		finally {
			// do this in a finally so that if an exception is thrown
			// during the above, we don't leave the Surface in an
			// inconsistent state
			if (c != null) {
				mSurfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}
	
	void drawClear() {
		Canvas c = null;
		try {
			c = mSurfaceHolder.lockCanvas(null);
			if (c != null)
				synchronized (mSurfaceHolder) {
					c.drawColor(Color.BLACK);
				} 
		}
		finally {
			// do this in a finally so that if an exception is thrown
			// during the above, we don't leave the Surface in an
			// inconsistent state
			if (c != null) {
				mSurfaceHolder.unlockCanvasAndPost(c);
			}
       }
	}
	
	private Runnable vizThread = new Runnable() {

		public void run() {
			int i = 0;
			
			Log.e(LOGVIZVIEW, String.format("START THREAD"));	

			while(running == true) {
				long now = System.nanoTime();
				
				//Log.e("JENlA", String.format("%d %d %s", now, i, vizThread.toString()));
				
				drawMain(pos, length);
				i++;
				
				long elapsed = System.nanoTime() - now;
				if(elapsed < DRAW_REFRESH_INTERVAL_NS) {
					try {
						Thread.sleep((DRAW_REFRESH_INTERVAL_NS - elapsed) / 1000 / 1000, 0);
					} catch (InterruptedException e) {

					}
				}
			}
			
			Log.e(LOGVIZVIEW, String.format("END THREAD redrew %d times", i));		
		}
	};
}
