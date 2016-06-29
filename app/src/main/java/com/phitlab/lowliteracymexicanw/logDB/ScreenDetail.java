package com.phitlab.lowliteracymexicanw.logDB;

import java.util.ArrayList;

public class ScreenDetail {

	private int screenID;
	private String screenName;
	private ArrayList<String> urlAL;
	
	public ScreenDetail(int id, String name, ArrayList<String> al) {
		
		this.screenID = id;
		this.screenName = name;
		urlAL = new ArrayList<String>(al);
	}
	
	
	public int getScreenID() {
		return screenID;
	}
	
	public void setScreenID(int screenID) {
		this.screenID = screenID;
	}
	
	public String getScreenName() {
		return screenName;
	}
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	
	public ArrayList<String> getUrlAL() {
		return urlAL;
	}
	
	
	public void setUrlAL(ArrayList<String> urlAL) {
		this.urlAL = urlAL;
	}
	
}
