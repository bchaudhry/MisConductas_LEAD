package com.phitlab.lowliteracymexicanw.logDB;

public class UserDetail {

	private int userId;
	private String username;
	private String password;
	private String regId;
	private String reminder1_begin;
	private String reminder1_end;
	private String reminder2_begin;
	private String eod_reminder;
	private String recruitment_date;
	private String reminder2_end;
	
	public UserDetail () {
		this.userId = 0;
		this.username = "";
		this.password = "";
		this.reminder1_begin = "";
		this.reminder1_end = "";
		this.reminder2_begin = "";
		this.reminder2_end = "";
		this.eod_reminder = "";
		this.recruitment_date = "";
	}
	
	public UserDetail(int userID, String username, String password, String regId, String reminder1_begin,
			String reminder1_end, String reminder2_begin, String reminder2_end, String eod_reminder, 
			String recruitment_date) {
		
		this.userId = userID;
		this.username = username;
		this.password = password;
		this.regId = regId;
		this.reminder1_begin = reminder1_begin;
		this.reminder1_end = reminder1_end;
		this.reminder2_begin = reminder2_begin;
		this.reminder2_end = reminder2_end;
		this.eod_reminder = eod_reminder;
		this.recruitment_date = recruitment_date;
	}	
	
	public int getUserID() {
		return userId;
	}
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public String getRegId() {
		return regId;
	}
	
	public void setRegId(String regId) {
		this.regId = regId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setReminder1_begin(String reminder1_begin) {
		this.reminder1_begin = reminder1_begin;
	}
	
	public String getReminder1_begin() {
		return reminder1_begin;
	}
	
	public void setReminder1_end(String reminder1_end) {
		this.reminder1_end = reminder1_end;
	}
	
	public String getReminder1_end() {
		return reminder1_end;
	}
	
	public void setReminder2_begin(String reminder2_begin) {
		this.reminder2_begin = reminder2_begin;
	}
	
	public String getReminder2_begin() {
		return reminder2_begin;
	}
	
	public void setReminder2_end(String reminder2_end) {
		this.reminder2_end = reminder2_begin;
	}
	
	public String getReminder2_end() {
		return reminder2_end;
	}
	
	public void setEod_reminder(String eod_reminder) {
		this.eod_reminder = eod_reminder;
	}
	
	public String getEod_reminder() {
		return eod_reminder;
	}
	
	public void setRecruitment_Date(String recruitment_date) {
		this.recruitment_date = recruitment_date;
	}
	
	public String getRecruitment_Date() {
		return recruitment_date;
	}
}
