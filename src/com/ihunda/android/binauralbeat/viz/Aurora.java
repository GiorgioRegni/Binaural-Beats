package com.ihunda.android.binauralbeat.viz;

import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ihunda.android.binauralbeat.Visualization;

public class Aurora implements Visualization {	
	
	/**
	 * Beat frequency in Hz
	 */
	private double freq;
	private double period;
	private Bitmap background;
	private Paint pTag;
	
	public Aurora() {
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.aurora);
		pTag = new Paint();
		pTag.setStyle(Paint.Style.FILL);
	}
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		// window [-2.0, 1.0, -1.5, 1.5]
		
		double dperiod = period * 2 * 10;
	
		double ratio = (now % dperiod) / dperiod;
		
		c.drawBitmap(background, 0, 0, null);
		if (ratio > 0.5)
			pTag.setColor(Color.argb((int) ((1-ratio)*75), 255, 255, 255));
		else
			pTag.setColor(Color.argb((int) (ratio*75), 255, 255, 255));
		c.drawCircle(width/4, height/2, width/2, pTag);
	}

	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

}
