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

/**
 * @author giorgio
 *
 * This class is used to keep track of android soundpool streams
 * 
 */
public class StreamVoice {

	public int streamID;
	public float leftVol;
	public float rightVol;
	public int loop;
	public float rate;
	
	public StreamVoice(int streamID, float leftVol, float rightVol, int loop,
			float rate) {
		super();
		this.streamID = streamID;
		this.leftVol = leftVol;
		this.rightVol = rightVol;
		this.loop = loop;
		this.rate = rate;
	}
}
