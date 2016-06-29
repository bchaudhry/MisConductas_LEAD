package com.phitlab.lowliteracymexicanw.logDB;

public class ReminderDetail {

	private int rec_id;
	private int user_id;
	private long reminder_time;
	private int reminder_type;
	private int reminder_response;
	private long response_time;
	
	public ReminderDetail(int rec_id, int user_id, int reminder_type, long reminder_time, 
			int reminder_response, long response_time) {
		this.setRec_id(rec_id);
		this.setUserID(user_id);
		this.setReminder_time(reminder_time);
		this.setReminder_type(reminder_type);
		this.setReminder_Response(reminder_response);
		this.setResponse_Time(response_time);		
	}
	
	
	public int getUserID() {
		return this.user_id;
	}
	
	public void setUserID(int userID) {
		this.user_id = userID;
	}
	
	public long getReminder_Response() {
		return reminder_response;
	}
	
	public void setReminder_Response(int reminder_response) {
		this.reminder_response = reminder_response;
	}
	
	public long getResponse_Time() {
		return response_time;
	}
	
	public void setResponse_Time(long response_time) {
		this.response_time = response_time;
	}


	public long getReminder_time() {
		return reminder_time;
	}


	public void setReminder_time(long reminder_time) {
		this.reminder_time = reminder_time;
	}


	public int getReminder_type() {
		return reminder_type;
	}


	public void setReminder_type(int reminder_type) {
		this.reminder_type = reminder_type;
	}

	public int getRec_id() {
		return rec_id;
	}


	public void setRec_id(int rec_id) {
		this.rec_id = rec_id;
	}
}
