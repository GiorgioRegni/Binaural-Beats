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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ihunda.android.binauralbeat.viz.None;

public class Program {
	
	protected String name;
	protected String description;
	protected final ArrayList<Period> seq = new ArrayList<Period>();
	private String author = "@GiorgioRegni";
	boolean useGL = false;
	
	public Program(String name) {
		this.name = name;
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
	
	protected Iterator<Period> getPeriodsIterator() {
		return seq.iterator();
	}

	public int getLength() {
		int len = 0;
		
		for (Period p: seq) {
			len += p.getLength();
		}
		
		return len;
	}
	
	public int getNumPeriods() {
		return seq.size();
	}

	public void setAuthor(String name) {
		author  = name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setGL() {
		useGL = true;
	}
	
	public boolean doesUseGL() {
		return useGL;
	}
	
	public static Program fromGnauralFactory(String data) {
		Program p = null;
		Visualization v =  new None();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;

			db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(new StringReader(data)));
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			String title = (String) xpath.evaluate("/schedule/title/text()", doc, XPathConstants.STRING);
			String descr = (String) xpath.evaluate("/schedule/schedule_description/text()", doc, XPathConstants.STRING);
			String author = (String) xpath.evaluate("/schedule/author/text()", doc, XPathConstants.STRING);
			
			p = new Program(title);
			p.setDescription(descr);
			p.setAuthor(author);
			
			Period oldPeriod = null;
			
			NodeList NVoices = (NodeList) xpath.evaluate("/schedule/voice[1]/entries/entry", doc, XPathConstants.NODESET);
			for (int i = 0; i<NVoices.getLength(); i++) {
				Node n = NVoices.item(i);
				NamedNodeMap a = n.getAttributes();
				
				int len = new Float(a.getNamedItem("duration").getTextContent()).intValue();
				float vol = (new Float(a.getNamedItem("volume_left").getTextContent()) +
						new Float(a.getNamedItem("volume_right").getTextContent()))*1.5f;
				if (vol > 1f)
					vol = 1f;
				float beatfreq = new Float(a.getNamedItem("beatfreq").getTextContent());
				float basefreq = new Float(a.getNamedItem("basefreq").getTextContent());
				
				BinauralBeatVoice voice = new BinauralBeatVoice(beatfreq, beatfreq, vol, basefreq);
				Period period = new Period(len, SoundLoop.WHITE_NOISE, 0.1f, null).
				addVoice(voice).
				setV(v);
				
				if (oldPeriod != null)
					oldPeriod.getVoices().get(0).freqEnd = voice.freqStart;
				
				p.addPeriod(period);
				
				oldPeriod = period;
			}
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return p;
	}
}
