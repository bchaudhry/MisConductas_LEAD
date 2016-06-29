package com.phitlab.lowliteracymexicanw.objects;


import java.util.ArrayList;
import org.apache.http.NameValuePair;
import android.util.Log;
import com.phitlab.lowliteracymexicanw.logDB.LogDBAdapter;

/* Singleton */
public class Registry 
{	
	private static Registry instance=null;
	private LogDBAdapter log;
	private int user_id;
	private ArrayList<NameValuePair> reminderTimes;
	private boolean loginStatus=false;
	private int alarmID;
	private String user_name;
	private boolean FIRST_ALARM;
	private boolean SECOND_ALARM;
	private boolean THIRD_ALARM;
    private boolean FIRST_SIGNAL;
    private boolean SECOND_SIGNAL;
    private boolean THIRD_SIGNAL;
	private boolean MORNING_RESET;
	private boolean EVENING_RESET;
	private int delay_time = 45;
	private boolean EOD_RESET;
	private boolean REMINDER;

	
	
	protected Registry() {
//		loginStatus = false;
//		alarmID=3;
	}
	
	public static Registry getInstance() {
		if(instance == null) {
			instance = new Registry();	
		}
		return instance;
	}

	public LogDBAdapter getLog() {
		Log.d("Registry","getting logs");
		return this.log;
	}
	
	public void setLog(LogDBAdapter log) {
		this.log = log;
		Log.d("Registry", "The database has been set");
	}
	
	public int getUserId() {
		return user_id;
	}
	
	public void setUserId(int userID) {
		this.user_id = userID;
	}
	
	public void setUserName(String userName) {
		this.user_name = userName;
	}
	
	public String getUserName() {
		return this.user_name;
	}
	
	public ArrayList<NameValuePair> getReminders() {
		return reminderTimes;
	}

	public boolean getLoginStatus() {
		return loginStatus;
	}
	
	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	public int getAlarmID() {
		return alarmID;
	}

	public void setAlarmID() {
		this.alarmID = (alarmID++%10)+3;
	}
	
	public void setFirstAlarm(boolean bool) {
		FIRST_ALARM = bool;
	}
	
	public boolean getFirstAlarm() {
		return FIRST_ALARM;
	}
	
	public void setSecondAlarm(boolean bool) {
		SECOND_ALARM = bool;
	}
	
	public boolean getSecondAlarm() {
		return SECOND_ALARM;
	}
	
	public void setThirdAlarm(boolean bool) {
		THIRD_ALARM = bool;
	}
	
	public boolean getThirdAlarm() {
		return THIRD_ALARM;
	}
	
	public void setDelayTime(int delay) {
		delay_time = delay;
	}
	
	public int getDelayTime() {
		return delay_time;
	}
	
	public void setMorningReset(boolean bool) {
		MORNING_RESET = bool;
	}

	public boolean getMorningReset() {
		return MORNING_RESET;
	}

	public void setEveningReset(boolean bool) {
		EVENING_RESET = bool;
	}

	public boolean getEveningReset() {
		return EVENING_RESET;
	}

	public boolean getEODReset() {
		return EOD_RESET ;
	}

	public void setEODReset(boolean bool) {
		EOD_RESET = bool;
	}

    public void setFirstSignal (boolean bool) {
       FIRST_SIGNAL = bool;
    }

    public boolean getFirstSignal() {
        return FIRST_SIGNAL;
    }

    public void setSecondSignal(boolean bool) {
        SECOND_SIGNAL = bool;
    }

    public boolean getSecondSignal() {
        return SECOND_SIGNAL;
    }

    public void setThirdSignal(boolean bool) {
        THIRD_SIGNAL = bool;
    }

    public boolean getThirdSignal() {
        return THIRD_SIGNAL ;
    }

	public void setReminder(boolean b) {
		REMINDER = b;
	}
	public boolean getReminder() {
		return REMINDER;
	}
}
