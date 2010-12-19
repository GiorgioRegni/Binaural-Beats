package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

public class Program {

	protected String name;
	protected ArrayList<Period> seq;
	
	public Program(String name) {
		this.name = name;
		seq = new ArrayList<Period>();
	}
	
	public Program addPeriod(Period p) {
		seq.add(p);
		return this;
	}
	
}
