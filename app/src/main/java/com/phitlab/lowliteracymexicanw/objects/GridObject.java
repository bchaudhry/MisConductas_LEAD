package com.phitlab.lowliteracymexicanw.objects;

public class GridObject {


	private int rid = -1;
	private String rTitle = "";
	private String rUrl = "";
	private int count = 0;
	private String media = "";
	
	public GridObject(int rid, String title) {
		this.rid = rid;
		this.rTitle = title;
	}


	public GridObject(String rUrl, String title) {
		this.rUrl = rUrl;
		this.rTitle = title;
	}

	public GridObject(int count, int rid, String title) {
		this.setCount(count);
		this.rid = rid;
		this.rTitle = title;
	}
	
	public GridObject(int rid, String title, String media) {
		this.rid = rid;
		this.rTitle = title;
		this.setMedia(media);
	}

	public GridObject(String rUrl, String title, String media) {
		this.rUrl = rUrl;
		this.rTitle = title;
		this.setMedia(media);
	}
	
	/**
	 * @return the rid
	 */
	public int getRid() {
		return rid;
	}


	/**
	 * @return the rTitle
	 */
	public String getrTitle() {
		return rTitle;
	}


	/**
	 * @return the rUrl
	 */
	public String getrUrl() {
		return rUrl;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public String getMedia() {
		return media;
	}


	public void setMedia(String media) {
		this.media = media;
	}
	


}
