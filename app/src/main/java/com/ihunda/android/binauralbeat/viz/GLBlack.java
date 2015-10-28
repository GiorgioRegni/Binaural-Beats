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

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.ihunda.android.binauralbeat.GLVisualization;

public class GLBlack implements GLVisualization {
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	
	private boolean glInited = false;
	
	public GLBlack() {
	}
 
	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

	@Override
	public void setup(GL11 gl, int width, int height) {
		// init the GL settings
		if (glInited) {
			resetGl(gl);
		}
		initGl(gl, width, height);
		
		// init the GL texture
		initGlTexture(gl);
	}
	
	private void resetGl(GL11 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPopMatrix();				
	}
	
	private void initGl(GL11 gl, int surfaceWidth, int surfaceHeight) {
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glFrontFace(GL10.GL_CCW);
        
        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrthof(0.0f, surfaceWidth, 0.0f, surfaceHeight, 0.0f, 1.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        
        glInited = true;
	}
	
	private void releaseTexture(GL11 gl) {
		
	}
	
	private void initGlTexture(GL11 gl) {
		
	}
	
	@Override
	public void dispose(GL11 gl) {
		releaseTexture(gl);
	}

	@Override
	public void redraw(GL11 gl, int width, int height, float now,
			float totalTime) {
		// window [-2.0, 1.0, -1.5, 1.5]
	
		
		// Clear the surface
		gl.glClearColorx(0, 0, 0, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	
	}
}
