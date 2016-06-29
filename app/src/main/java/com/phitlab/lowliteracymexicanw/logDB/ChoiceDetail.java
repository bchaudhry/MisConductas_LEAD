package com.phitlab.lowliteracymexicanw.logDB;

public class ChoiceDetail {

	private int user_id;
	private int choice_id;
	private long choice_time;
	private int behavior_id;
	private String behavior_duration;
	private int number_items;
	private int embarrass;
	private int loss_of_control;
	private int context_people;
	private int item_type;
	private String photo_identifier;
	private String voice_recording;
	
	public ChoiceDetail(int user_id, int choice_id, long choice_time, int behavior_id, 
			String behavior_duration, int number_items, int embarrass, int loss_of_control, 
			int context_people, int item_type, String photo_identifier, String voice_recording){
		
		this.user_id = user_id;
		this.choice_id = choice_id;
		this.choice_time = choice_id;
		this.behavior_id = behavior_id;
		this.behavior_duration = behavior_duration;
		this.number_items = number_items;
		this.embarrass = embarrass;
		this.loss_of_control = loss_of_control;
		this.context_people = context_people;
		this.item_type = item_type;
		this.photo_identifier = photo_identifier;
		this.voice_recording = voice_recording;
		
	}
	
	
	public int getUserID() {
		return user_id;
	}
	
	public void setUserID(int userID) {
		this.user_id = userID;
	}
	
	public int getChoiceID() {
		return choice_id;
	}
	
	public void setChoiceID(int choiceID) {
		this.choice_id = choiceID;
	}
	
	public long getChoiceTime() {
		return choice_time;
	}
	
	public void setUserID(long choice_time) {
		this.choice_time = choice_time;
	}
	
	public int getbehavior_id() {
		return behavior_id;
	}
	
	public void setbehavior_id(int behavior_id) {
		this.behavior_id = behavior_id;
	}
	
	public String getbehavior_Duration() {
		return behavior_duration;
	}
	
	public void setbehavior_Duration(String behavior_duration) {
		this.behavior_duration = behavior_duration;
	}
	
	public int getNumberItems() {
		return number_items;
	}
	
	public void setNumberItems(int number_items) {
		this.number_items = number_items;
	}
	public int getLOC() {
		return loss_of_control;
	}
	
	public void setLOC(int loc) {
		this.loss_of_control = loc;
	}
	
	public int getEmbarrass() {
		return embarrass;
	}
	
	public void setEmbarrass(int embarrass) {
		this.embarrass = embarrass;
	}
	public int getCOP() {
		return context_people;
	}
	
	public void setCOP(int cop) {
		this.context_people = cop;
	}
	
	public int getItemType() {
		return item_type;
	}
	
	public void setItemType(int item_type) {
		this.item_type = item_type;
	}
	public String getPhoto_Identifier() {
		return photo_identifier;
	}
	
	public void setPhoto_Identifier(String photo_identifier) {
		this.photo_identifier = photo_identifier;
	}


	public String getVoice_recording() {
		return voice_recording;
	}


	public void setVoice_recording(String voice_recording) {
		this.voice_recording = voice_recording;
	}
}
