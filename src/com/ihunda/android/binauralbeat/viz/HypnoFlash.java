package com.ihunda.android.binauralbeat.viz;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ihunda.android.binauralbeat.BBeat;
import com.ihunda.android.binauralbeat.R;
import com.ihunda.android.binauralbeat.Visualization;

public class HypnoFlash implements Visualization {

	private static final int COLOR_FLASH1 = Color.argb(100, 255, 255, 255);
	private static final int COLOR_FLASH2 = Color.argb(150, 255, 255, 255);
	
	/**
	 * Beat frequency in Hz
	 */
	private float period;
	private Paint pFlash1;
	private Paint pFlash2;
	
	private Bitmap background;
	
	public HypnoFlash() {
		pFlash1 = new Paint();
		pFlash1.setStyle(Paint.Style.FILL);
		pFlash1.setColor(COLOR_FLASH1);
		
		pFlash2 = new Paint();
		pFlash2.setStyle(Paint.Style.FILL);
		pFlash2.setColor(COLOR_FLASH2);
		
		background = BitmapFactory.decodeResource(BBeat.getInstance().getResources(), R.drawable.hypnosisspiral);
		
	}
	
	
	public void redraw(Canvas c, int width, int height, float now,
			float totalTime) {
		float ratio;
		float dperiod = period * 2 * 2;
		
		ratio = (now % dperiod) / dperiod;
	
		if (ratio < 0.8) 
			c.drawBitmap(background, 0, 0, null);
		else {
			Random r = new Random();
			if (r.nextBoolean())
				c.drawColor(COLOR_FLASH1);
			else
				c.drawColor(COLOR_FLASH2);
		}
	}

	public void setFrequency(float beat_frequency) {
		period = 1f / beat_frequency;
	}

}
