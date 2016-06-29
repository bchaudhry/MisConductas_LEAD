package com.phitlab.lowliteracymexicanw.sync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.phitlab.lowliteracymexicanw.logDB.LogDBAdapter;
import com.phitlab.lowliteracymexicanw.logDB.ScreenDetail;
import com.phitlab.lowliteracymexicanw.logDB.UserDetail;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.objects.ReminderTimes;
import com.phitlab.lowliteracymexicanw.reminders.EODAlarmReceiver;
import com.phitlab.lowliteracymexicanw.reminders.ReminderAlarmReceiver;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DatabaseService extends IntentService implements AsyncTaskCompletedListener{
	
	private static ScreenDBController dbController;
	private static LogDBAdapter dbAdapter;

    public DatabaseService() {
        super("MyServiceName");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MyService", "About to execute MyTask");
        dbController = ScreenDBController.getInstance(this);
        dbAdapter = Registry.getInstance().getLog();
        
        //Execute database sync 
        new LoginDetailSync(this).execute();
        new ScreenTablesSync(this).execute();
        Log.d("MyService", "SynchTables");
        Log.d("MyService", "SynchLogin");
       // new DatabaseSync(this).execute();
    }
    

	@Override
	public void onAsyncTaskCompleted(ArrayList<ScreenDetail> al) {

		System.out.println("My Service got response"+al);
		
		dbController.open();
		for(int i=0; i<al.size(); i++) {
			dbController.update(al.get(i));
		}
		dbController.close();
		
	}

	@Override
	public void onAsyncTaskCompleted() {
		//System.out.println("My Service got response 55"+userDetail);
		//dbAdpater.logUserDetails(userDetail);	
		
    	ReminderTimes reminderTimes = new ReminderTimes(); 
        Context context = this.getApplicationContext();
        ReminderAlarmReceiver first_reminder = new ReminderAlarmReceiver();
        ReminderAlarmReceiver second_reminder = new ReminderAlarmReceiver();
        EODAlarmReceiver endofday_reminder = new EODAlarmReceiver();
        
        if(reminderTimes.size() > 0) {
	        System.out.println("User ID"+ dbAdapter.getUserID() + Registry.getInstance().getUserName());
	        Log.d("DatabaseService", "Setting Alarms");
	        Registry.getInstance().setUserId(dbAdapter.getUserID());
	       // first_reminder.setAlarm(context, reminderTimes.getReminderMillis(0), 0);
	       // second_reminder.setAlarm(context, reminderTimes.getReminderMillis(2), 1);
	        first_reminder.setAlarm(context, reminderTimes.getLimitMillis(0), 0);
	        second_reminder.setAlarm(context, reminderTimes.getLimitMillis(2), 1);
	        endofday_reminder.setAlarm(context, reminderTimes.getLimitMillis(4), 2);
	        
	        Registry registry = Registry.getInstance();
	        
//	        Calendar cal = Calendar.getInstance();
//	        cal.setTimeInMillis(reminderTimes.getLimitMillis(1));
//	        cal.add(Calendar.MINUTE, registry.getDelayTime());
//	        cal.add(Calendar.MINUTE, 10);
//	        long backup_reminder1 = cal.getTimeInMillis(); 
//	        
//	        cal.setTimeInMillis(reminderTimes.getLimitMillis(3));
//	        cal.add(Calendar.MINUTE, registry.getDelayTime());
//	        cal.add(Calendar.MINUTE, 10);
//	        long backup_reminder2 = cal.getTimeInMillis(); 
//	        
//	        //cal.setTimeInMillis(reminderTimes.getLimitMillis(4));
//	        cal.add(Calendar.HOUR_OF_DAY, 23);
//	        cal.add(Calendar.MINUTE, 50);
//	        long backup_reminder3 = cal.getTimeInMillis();
	        
//	        BackUpAlarmReceiver first_reminder_backup = new BackUpAlarmReceiver();
//			first_reminder_backup.setAlarm(context, backup_reminder1, 11);
//	        BackUpAlarmReceiver second_reminder_backup = new BackUpAlarmReceiver();
//			second_reminder_backup.setAlarm(context,  backup_reminder2, 22);
//	        BackUpAlarmReceiver endofday_reminder_backup = new BackUpAlarmReceiver();
//			endofday_reminder_backup.setAlarm(context,  backup_reminder3, 33);
	        
	        Calendar calendar = Calendar.getInstance(); 
	        //calendar.setTimeInMillis(milliseconds);
	        Date date = new Date();
	        
	        Registry.getInstance().getLog().saveReminderTime(0, reminderTimes.getLimitMillis(0));
	        Registry.getInstance().getLog().saveReminderTime(2, reminderTimes.getLimitMillis(2));
	        Registry.getInstance().getLog().saveReminderTime(4, reminderTimes.getLimitMillis(4));
	        
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
			String formattedDate = df.format(calendar.getTime());
			date.setTime(reminderTimes.getLimitMillis(0));
			System.out.println("reminder 1" + date.toString());
			date.setTime(reminderTimes.getLimitMillis(2));
			//System.out.println(formattedDate + " " + date.toString());
			System.out.println("reminder 2" + date.toString());
			date.setTime(reminderTimes.getLimitMillis(4));
			System.out.println("end of reminder" + date.toString());
        }
	}

//	@Override
//	public void onAsyncTaskCompleted() {
		
//	}
	
}
