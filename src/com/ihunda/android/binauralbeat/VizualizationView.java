package com.ihunda.android.binauralbeat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class VizualizationView extends SurfaceView implements Callback {
	
	protected static final long DRAW_REFRESH_INTERVAL_NS = 1000 * 1000 * 1000 / 10; // 10 refresh per seconds
	private SurfaceHolder mSurfaceHolder;
	private int width;
	private int height;
	private Visualization v;
	private boolean running;
	
	protected float pos;
	protected float length;
	
	private Thread vThread;
	
	public VizualizationView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
		assert(this.v == null);
		assert(vThread == null);
		
		drawClear();
		
		if (v == null)
			return;
		
		this.v = v;
		this.pos = 0;
		this.length = length;
		
		running = true;
		vThread = new Thread(vizThread);
		vThread.start();
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
			while(running == true) {
				long now = System.nanoTime();
				drawMain(pos, length);
				
				long elapsed = System.nanoTime() - now;
				while(elapsed < DRAW_REFRESH_INTERVAL_NS) {
					try {
						Thread.sleep((DRAW_REFRESH_INTERVAL_NS - elapsed) / 1000 / 1000, 0);
					} catch (InterruptedException e) {

					}
					elapsed = System.nanoTime() - now;
				}
			}
		}
	};
}
