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

import com.ihunda.android.binauralbeat.GLVisualization;

import android.graphics.Color;

import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

public class Plasma implements GLVisualization {
	private static final int PALETTE_SIZE = 257;
	private static final int COS_TABLE_SIZE = 256;
	
	/**
	 * Beat frequency in Hz
	 */
	float freq;
	float period;
	
	private int palette[] = new int[PALETTE_SIZE];
	private int cosTbl[] = new int[COS_TABLE_SIZE];
	
	 byte p1,p2,p3,p4; 
	byte t1,t2,t3,t4; 
	
	private int textureWidth;
	private int textureHeight;
	private ByteBuffer pixelBuffer;
	private static final int bytesPerPixel = 3;
	private int glTextureId = -1;
	private int[] textureCrop = new int[4];	
	private boolean glInited = false;
	
	public Plasma() {
		
		for (int x=0; x<64; x++) {
			int a = x*3;
			// Black -> yellow
			palette[x] = Color.rgb(a, a, 0);
			// Yellow -> red
			palette[x+64] = Color.rgb(192, 192-a, 0);
			// Red -> Blue
			palette[x+128] = Color.rgb(192-a, 0, a);
			// Blue -> Green
			palette[x+192] = Color.rgb(0, a, 192-a);
		}
		palette[256] = palette[255];
		
		int i;
		double step = 2 * Math.PI / COS_TABLE_SIZE;
		
		for (i=0; i<COS_TABLE_SIZE; i++)
			cosTbl[i] = (int) (Math.cos(i*step)*32)+32;	
	}
 
	public void setFrequency(float beat_frequency) {
		freq = beat_frequency;
		period = 1f / beat_frequency;
	}

	@Override
	public void setup(GL11 gl, int width, int height) {
		// TODO: choose values smarter
		// but remember they have to be powers of 2
		if (width < height) {
			textureWidth = 196;
			textureHeight = 256;
		} else {
			textureWidth = 256;
			textureHeight = 196;
		}
		textureCrop[0] = 0;
		textureCrop[1] = 0;
		textureCrop[2] = textureWidth;
		textureCrop[3] = textureHeight;
		
		
		// init the pixel buffer
		pixelBuffer = ByteBuffer.allocate(textureWidth * textureHeight * bytesPerPixel);
		
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
		if (glTextureId != -1) {
			gl.glDeleteTextures(1, new int[] { glTextureId }, 0);
		}		
	}
	
	private void initGlTexture(GL11 gl) {
		releaseTexture(gl);
		
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		glTextureId = textures[0];
	
		// we want to modify this texture so bind it
		gl.glBindTexture(GL10.GL_TEXTURE_2D, glTextureId);

		// GL_LINEAR gives us smoothing since the texture is larger than the screen
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, 
        				   GL10.GL_TEXTURE_MAG_FILTER,
        				   GL10.GL_LINEAR);        
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, 
        				   GL10.GL_TEXTURE_MIN_FILTER,
        				   GL10.GL_LINEAR);
        // repeat the edge pixels if a surface is larger than the texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                		   GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                           GL10.GL_REPEAT); 
        
        pixelBuffer.rewind();
        // we need to output the pixels upside down due to glDrawTex peculiarities
		for (int y = textureWidth*textureHeight - textureWidth; y > 0; y -= textureWidth) {
			for (int x = 0; x < textureWidth; ++x) {
				int pixel = 0;
				pixelBuffer.put((byte) (pixel >> 16));
				pixelBuffer.put((byte) ((pixel >> 8) & 0xff));
				pixelBuffer.put((byte) (pixel & 0xff));
			}
		}
        
        
        // and init the GL texture with the pixels
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGB, textureWidth, textureHeight,
				0, GL10.GL_RGB, GL10.GL_UNSIGNED_BYTE, pixelBuffer);        
		
		// at this point, we are OK to further modify the texture
		// using glTexSubImage2D
	}
	
	@Override
	public void dispose(GL11 gl) {
		releaseTexture(gl);
	}

	@Override
	public void redraw(GL11 gl, int width, int height, float now,
			float totalTime) {
		// window [-2.0, 1.0, -1.5, 1.5]
		
	    float dperiod = period * 2;
	    float ratio = (now % dperiod) / dperiod;
		//int img[] = new int[textureHeight*textureWidth];
	    
	    int x,y,col,pos;
		 
	    pixelBuffer.rewind();
	    
		pos = 0; // position dans le buffer
		t1 = p1; // on sauvegarde les positions des "pointeurs" de colonne
		t2 = p2;
		for (y=0; y<textureHeight; y++) {
			t3 = p3; // on sauvegarde les positions des "pointeurs" de ligne
			t4 = p4;
			for (x=0; x<textureWidth; x++) {
				col = cosTbl[t1&0XFF] + cosTbl[t2&0XFF]+ cosTbl[t3&0XFF] + cosTbl[t4&0XFF]; 
				int pixel = palette[col];
				pixelBuffer.put((byte) (pixel >> 16));
				pixelBuffer.put((byte) ((pixel >> 8) & 0xff));
				pixelBuffer.put((byte) (pixel & 0xff));
				
				pos++;
				t3 -= 1; // on incrmente les "pointeurs" de ligne.
				t4 += 4; // ceci fait bouger la courbe du plasma sur l'axe horizontal
			}
			t1 -= 4; // on incrmente les "pointeurs" de colonne.
			t2 += 2; // ceci fait bouger la courbe du plasma sur l'axe vertical
		}
		p1 += 1; // on incrmente les positions des diff�rents pointeurs
		p2 += 4; // pour faire bouger le plasma sur les 2 axes1
		p3 += 1;
		p4 -= (int) Math.ceil((8*ratio));	
		
		// Clear the surface
		gl.glClearColorx(0, 0, 0, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	
		
		// Choose the texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, glTextureId);

		// Update the texture
		gl.glTexSubImage2D(GL10.GL_TEXTURE_2D, 0, 0, 0, textureWidth, textureHeight, 
						   GL10.GL_RGB, GL10.GL_UNSIGNED_BYTE, pixelBuffer);
		
		// Draw the texture on the surface
		gl.glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, textureCrop, 0);
		((GL11Ext) gl).glDrawTexiOES(0, 0, 0, width, height);
	}
}
