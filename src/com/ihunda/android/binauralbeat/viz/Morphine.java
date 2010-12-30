package com.ihunda.android.binauralbeat.viz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.R;
import com.ihunda.android.binauralbeat.Visualization;

public class Morphine implements Visualization {

	private static final int COLOR_LED = Color.argb(150, 120, 255, 30);
	
	/**
	 * Beat frequency in Hz
	 */
	private float period;
	private Paint pLed;
	private Bitmap background;
	
	public Morphine() {
		pLed = new Paint();
		pLed.setStyle(Paint.Style.FILL);
		pLed.setColor(COLOR_LED);
		
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.morphine);
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		int ledRadius = width / 2;
		float ratio;
		float dperiod = period * 2 * 2 * 2;
		
		ratio = (now % dperiod) / dperiod;
		
		c.drawBitmap(background, 0, 0, null);
		if (ratio > 0.5f) {
			c.drawCircle(width/2, height/2, (int) (ledRadius*(ratio-0.5f)*2f), pLed);
		}
		else {
			c.drawCircle(width/2, height/2, (int) (ledRadius*(0.5f-ratio)*2f), pLed);
		}
	}

	public void setFrequency(float beat_frequency) {
		period = 1f / beat_frequency;
	}

}
