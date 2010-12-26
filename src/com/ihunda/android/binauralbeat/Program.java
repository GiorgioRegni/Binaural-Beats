package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

public class Program {

	protected String name;
	protected String description;
	protected ArrayList<Period> seq;
	
	public Program(String name) {
		this.name = name;
		seq = new ArrayList<Period>();
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
	
}
