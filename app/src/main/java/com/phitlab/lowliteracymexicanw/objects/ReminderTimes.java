package com.phitlab.lowliteracymexicanw.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.util.Log;

public class ReminderTimes {
	private ArrayList<String> reminderTimes;
	Registry registry;
	
	public int size() {
		
		return reminderTimes.size();
	}
	
	public ReminderTimes() {
		registry = Registry.getInstance();
		reminderTimes = registry.getLog().getReminderTimes();
	}
	
	public Calendar getReminderLimits  (int i) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
		String formattedDate = df.format(calendar.getTime());
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
		Date date = new Date();
		try {
			Log.d("ReminderTimes", "i is " + i + "reminderTime is " + reminderTimes.get(i));
			date = df1.parse(formattedDate + " " + reminderTimes.get(i));
			Log.d("Parsed Date", df1.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		return calendar;
	}
	
	public long getReminderMillis  (int i) {
		long millis = calculateTime(i).getTimeInMillis();
		while (millis > getReminderLimits(i+1).getTimeInMillis()) {
			millis = calculateTime(i).getTimeInMillis();
		}
		registry.getLog().saveReminderTime(i, millis);
		return millis;
	}
	
	public long getLimitMillis  (int i) {
		long millis = getReminderLimits(i).getTimeInMillis();
		return millis;
	}
	
	public Calendar calculateTime (int i) {
		Random r = new Random();
		Calendar calendar = Calendar.getInstance();
		
		long reminder_begin_time = getReminderLimits(i).getTimeInMillis();
		long reminder_end_time = getReminderLimits(i+1).getTimeInMillis() ;
		
		Calendar cal_begin = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();
		
		cal_begin.setTimeInMillis(reminder_end_time);
		cal_end.setTimeInMillis(reminder_begin_time);
//		cal.add(Calendar.MINUTE, -2);
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
		Log.d("CalculateTime BeginTime", df1.format(cal_begin.getTime()));
		Log.d("CalculateTime EndTime", df1.format(cal_end.getTime()));
		
//		long reminder = cal_begin.getTimeInMillis() + nextLong(r, cal_end.getTimeInMillis()-cal_begin.getTimeInMillis());
		
		long reminder= nextLong(r, reminder_end_time - reminder_begin_time) + reminder_begin_time;
		calendar.setTimeInMillis(reminder);
		Log.d("Reminder Time", df1.format(calendar.getTime()));
		
		return calendar;
	}

	public String getFormattedLimits(int i) {
		return reminderTimes.get(i);
	}
	
	public String getFormattedReminder (int i) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String formattedDate = df.format(calculateTime(i).getTime());
		return formattedDate;
	}
	
	long nextLong(Random rng, long n) {
 	   long bits, val;
 	   do {
 	      bits = (rng.nextLong() << 1) >>> 1;
 	      val = bits % n;
 	   } while (bits-val+(n-1) < 0L);
 	   return val;
 	}
	
	public long getNextReminderMillis  (int i) {
		long millis = calculateNextReminder(i).getTimeInMillis();
		return millis;
	}
	
	public Calendar calculateNextReminder (int i) {
		long millis = getLimitMillis(i);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		Calendar curr_calendar = Calendar.getInstance();
		curr_calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.DAY_OF_MONTH, curr_calendar.get(Calendar.DAY_OF_MONTH)+1);
		return calendar;
	}
	
	public long getNextEODMillis  () {
		long millis = calculateNextEOD().getTimeInMillis();
		return millis;
	}
	
	public Calendar calculateNextEOD () {
		long millis = getLimitMillis(4);
		Calendar reminder = Calendar.getInstance();
		reminder.setTimeInMillis(millis);
		long oldmillis = registry.getLog().getReminderTime(4);
		Calendar old = Calendar.getInstance();
		old.setTimeInMillis(oldmillis);
		Calendar current = Calendar.getInstance();
		
		if(current.get(Calendar.HOUR_OF_DAY) > old.get(Calendar.HOUR_OF_DAY)) {
			reminder.add(Calendar.DAY_OF_MONTH, 1);
		}
		else if(current.get(Calendar.HOUR_OF_DAY) == old.get(Calendar.HOUR_OF_DAY)) {
			 if(current.get(Calendar.MINUTE) > old.get(Calendar.HOUR_OF_DAY)) {
				 reminder.add(Calendar.DAY_OF_MONTH, 1);
			 } 
		}
		registry.getLog().saveReminderTime(4, reminder.getTimeInMillis());
		return reminder;
	}	
		
//		if (old_calendar.get(Calendar.HOUR_OF_DAY) < calendar.get(Calendar.HOUR_OF_DAY)) {
//			if(old_calendar.get(Calendar.DAY_OF_MONTH) <= curr_calendar.get(Calendar.DAY_OF_MONTH) )
//				calendar.add(Calendar.DAY_OF_MONTH, 1);
//			Log.d("ReminderTime", "Set Reminder Time0");
//			Log.d("ReminderTime", "Old Reminder Hour" + old_calendar.get(Calendar.HOUR_OF_DAY));
//			Log.d("ReminderTime", "Current Hour" + curr_calendar.get(Calendar.HOUR_OF_DAY));
//		}
//		else if (old_calendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY) && 
//				old_calendar.get(Calendar.MINUTE) <= curr_calendar.get(Calendar.MINUTE)) {
//			if(old_calendar.get(Calendar.DAY_OF_MONTH) <= curr_calendar.get(Calendar.DAY_OF_MONTH) )
//				calendar.add(Calendar.DAY_OF_MONTH, 1);
//			Log.d("ReminderTime", "Set Reminder Time1");
//			Log.d("ReminderTime", "Old Reminder Hour" + old_calendar.get(Calendar.HOUR_OF_DAY));
//			Log.d("ReminderTime", "Current Hour" + curr_calendar.get(Calendar.HOUR_OF_DAY));
//		}
		
//		Log.d("ReminderTime", "Old Reminder Time" + old_calendar.getTime().toString());
//		Log.d("ReminderTime", "New Reminder Time" + calendar.getTime().toString());
//		registry.getLog().saveReminderTime(4, calendar.getTimeInMillis());
//		return calendar;
//	}
	
	public long getNewReminderMillis  (int i) {
		long millis = calculateNextReminder(i).getTimeInMillis();
		long oldmillis = registry.getLog().getReminderTime(i);
		Calendar reminder = Calendar.getInstance();
		Calendar current = Calendar.getInstance();
		Calendar old = Calendar.getInstance();
		old.setTimeInMillis(oldmillis);
		reminder.setTimeInMillis(millis);
		
		if(current.get(Calendar.HOUR_OF_DAY) < old.get(Calendar.HOUR_OF_DAY)) {
			reminder.add(Calendar.DAY_OF_MONTH, -1);
		}
		else if(current.get(Calendar.HOUR_OF_DAY) == old.get(Calendar.HOUR_OF_DAY)) {
			 if(current.get(Calendar.MINUTE) <= old.get(Calendar.HOUR_OF_DAY)) {
				 reminder.add(Calendar.DAY_OF_MONTH, -1);
			 } 
		}
		//return millis;
		registry.getLog().saveReminderTime(i, reminder.getTimeInMillis());
		return reminder.getTimeInMillis();
	}
	
	public Calendar calculateNewReminder (int i) {
		long millis = getReminderMillis(i);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		
		long old_millis = registry.getLog().getReminderTime(i);
		Calendar old_calendar = Calendar.getInstance();
		old_calendar.setTimeInMillis(old_millis);

		Registry.getInstance().getLog().saveReminderTime(i, calendar.getTimeInMillis());
		Log.d("ReminderTime", "Old Reminder Time" + old_calendar.getTime().toString());
		Log.d("ReminderTime", "New Reminder Time" + calendar.getTime().toString());
		return calendar;
	}		
//		long millis = registry.getLog().getReminderTime(i);
//		Calendar curr_calendar = Calendar.getInstance();
//		curr_calendar.setTimeInMillis(System.currentTimeMillis());
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(millis);
//		calendar.set(Calendar.DAY_OF_MONTH, curr_calendar.get(Calendar.DAY_OF_MONTH));
//		long old_millis = registry.getLog().getReminderTime(i);
//		Calendar old_calendar = Calendar.getInstance();
//		old_calendar.setTimeInMillis(old_millis);
//
//		if (old_calendar.get(Calendar.HOUR_OF_DAY) < curr_calendar.get(Calendar.HOUR_OF_DAY)) {
//			if(old_calendar.get(Calendar.DAY_OF_MONTH) <= curr_calendar.get(Calendar.DAY_OF_MONTH) )
//				calendar.add(Calendar.DAY_OF_MONTH, 1);
//			Log.d("ReminderTime", "Old Calendar Hour" + old_calendar.get(Calendar.HOUR_OF_DAY));
//			Log.d("ReminderTime", "Current Hour" + curr_calendar.get(Calendar.HOUR_OF_DAY));
//		}
//		else if (old_calendar.get(Calendar.HOUR_OF_DAY) == curr_calendar.get(Calendar.HOUR_OF_DAY) && 
//				old_calendar.get(Calendar.MINUTE) <= curr_calendar.get(Calendar.MINUTE)) {
//			if(old_calendar.get(Calendar.DAY_OF_MONTH) <= curr_calendar.get(Calendar.DAY_OF_MONTH) )
//				calendar.add(Calendar.DAY_OF_MONTH, 1);
//			Log.d("ReminderTime", "Old Calendar Hour" + old_calendar.get(Calendar.HOUR_OF_DAY));
//			Log.d("ReminderTime", "Current Hour" + curr_calendar.get(Calendar.HOUR_OF_DAY));
//		}
//		Registry.getInstance().getLog().saveReminderTime(i, calendar.getTimeInMillis());
//		Log.d("ReminderTime", "Old Reminder Time" + old_calendar.getTime().toString());
//		Log.d("ReminderTime", "New Reminder Time" + calendar.getTime().toString());
//		return calendar;
//	}
}
