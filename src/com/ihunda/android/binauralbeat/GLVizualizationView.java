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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GLVizualizationView extends GLSurfaceView implements GLSurfaceView.Renderer, VizualisationView  {
	
	protected static final long DRAW_REFRESH_INTERVAL_NS = 1000 * 1000 * 1000 / 16; // # time between refreshes
	protected static final long DRAW_REFRESH_INTERVAL_MIN_NS = 1000 * 1000 * 1000 / 20; // # time between refreshes
	private static final String LOGVIZVIEW = "BBT-GLVIZ";
	
	private int width;
	private int height;
	private GLVisualization v;
	protected float pos;
	protected float length;
	
	GL10 gl;
	private boolean v_was_setup = false;
	
	public GLVizualizationView(Context context) {
		super(context);
		
		// Turn on error-checking and logging
	    //setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
	    
		 // register ourself to OpenGL
	    setRenderer(this);
	        
		// register our interest in hearing about changes to our surface
        //mSurfaceHolder  = getHolder();
        //mSurfaceHolder.addCallback(this);
        
        setFocusable(true); // make sure we get key events
        v = null;
	}
	
	public void startVisualization(Visualization v, float length) {
		// TODO drawClear();
		
		if (v == null)
			return;
		
		this.v = (GLVisualization) v;
		
		v_was_setup  = false;
		
		this.pos = 0;
		this.length = length;
	}
	
	public void stopVisualization() {	
		this.v = null;
		drawClear();
	}
	
	public void setProgress(float pos) {
		this.pos = pos;
	}
	
	public void setFrequency(float freq) {
		if (v != null)
			v.setFrequency(freq);
	}
	
	void drawClear() {
	}
	

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		width = w;
		height = h;
		gl.glViewport(0, 0, w, h);
		this.gl = gl;
	}

	public void onDrawFrame(GL10 gl) {
		
		long now = System.nanoTime();
		
		if (v == null) {
			gl.glClearColor(0.7f, 0.2f, 0.2f, 1.0f);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		} else {
			if (!v_was_setup) {
				v.setup((GL11)gl, width, height);
				v_was_setup = true;
			}
			v.redraw((GL11) gl, width, height, pos, length);
		}
		
		long elapsed = System.nanoTime() - now;
		
		if (elapsed < DRAW_REFRESH_INTERVAL_NS) {
			try {
				Thread.sleep((DRAW_REFRESH_INTERVAL_NS - elapsed) / 1000 / 1000, 0);
			} catch (InterruptedException e) {
			}
		}
	}
}
