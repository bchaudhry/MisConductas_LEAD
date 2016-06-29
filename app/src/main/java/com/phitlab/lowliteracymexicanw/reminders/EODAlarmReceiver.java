package com.phitlab.lowliteracymexicanw.reminders;

import java.util.Calendar;

import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.objects.ReminderTimes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
//import android.os.Bundle;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class EODAlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		
	   try {
		   Log.d("ENDALARAM", "eND OF DAY ALARM");		   
		   PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		   PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,  "BusSnoozeAlarm");
		   wl.acquire();
		   
		   //Intent newIntent = new Intent(context, PopUpActivity.class);
		   //newIntent.putExtra("alarm_message", message);
		   Registry registry = Registry.getInstance();
		   ReminderTimes rTimes = new ReminderTimes();
		   long current_time = Calendar.getInstance().getTimeInMillis();
	       long eod_reminder = rTimes.getLimitMillis(4);
		   Calendar eod_cal = Calendar.getInstance();
		   eod_cal.setTimeInMillis(eod_reminder);
		   eod_cal.set(Calendar.HOUR_OF_DAY, 23);
		   eod_cal.set(Calendar.MINUTE, registry.getDelayTime());
		   eod_cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		   eod_cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		   long eod_time = eod_cal.getTimeInMillis();
		   
		   if ( !registry.getThirdAlarm() && current_time >= eod_reminder && current_time <= eod_time) {
			   registry.setFirstAlarm(false);
				registry.setSecondAlarm(false);
				registry.setThirdAlarm(true);
				startEODReminderActivity(context);
		   }
		   
		   if(wl.isHeld()){
               wl.release();
           }
	    } 
	   catch (Exception e) {
	      Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
	      e.printStackTrace();
	 
	    }
	}

	private void startEODReminderActivity(final Context context) {
		Intent newIntent = new Intent(context, EODReminderActivity.class);
		   newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   newIntent.addCategory(Intent.CATEGORY_HOME);
		   context.startActivity(newIntent);
		   
		   Uri notifyuri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		   final Ringtone r = RingtoneManager.getRingtone(context, notifyuri);
		   new Thread(new Runnable() {
			   @Override
			public void run() {
				   try {
					   AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					   if(audio.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
						   r.play();
						   Thread.sleep(5000);
						   r.stop();
					   }
					   else if ((audio.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) || 
							    (audio.getRingerMode() == AudioManager.RINGER_MODE_SILENT)) {
						   Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
						   long pattern[] = { 0, 100, 200, 300, 400 };
						   vibrator.vibrate(pattern, 0);
						   Thread.sleep(5000);
						   vibrator.cancel();
					   }
				   } catch (InterruptedException e) {
					   e.printStackTrace();
				   }
			   }
		   }).start();
	}
	
	public void setAlarm(Context context, long reminderTime, int identifier) {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, EODAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, identifier, intent, 0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY,  pi);
     }
	
	 public void cancelAlarm(Context context, int identifier) {
	   AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	   Intent intent = new Intent(context, EODAlarmReceiver.class);
       PendingIntent sender = PendingIntent.getBroadcast(context, identifier, intent, 0);
       alarmManager.cancel(sender);
       Log.d("LogoutAlarmReceiver", "alarm has been cancelled");
   }
	 
	 public void setOnceAlarm(Context context, long reminderTime, int identifier) {
	    AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, EODAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, identifier, intent, 0);
        Log.d("LogoutAlarmReceiver", "alarm has been reset");
        am.set(AlarmManager.RTC_WAKEUP, reminderTime, pi);
	 }

}

