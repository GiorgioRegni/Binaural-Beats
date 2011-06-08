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

import java.util.ArrayList;

public class Program {

	protected String name;
	protected String description;
	protected ArrayList<Period> seq;
	private String author = "@GiorgioRegni";
	
	public Program(String name) {
		this.name = name;
		seq = new ArrayList<Period>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String d) {
		description = d;
	}
	
	public Program addPeriod(Period p) {
		seq.add(p);
		return this;
	}
	
	public int getLength() {
		int len = 0;
		
		for (Period p: seq) {
			len += p.getLength();
		}
		
		return len;
	}

	public void setAuthor(String name) {
		author  = name;
	}
	
	public String getAuthor() {
		return author;
	}
	
}
