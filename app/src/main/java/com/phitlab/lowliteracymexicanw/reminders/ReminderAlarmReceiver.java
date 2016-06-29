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
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class ReminderAlarmReceiver extends BroadcastReceiver {

	private Registry registry;

	@Override
	public void onReceive(final Context context, Intent intent) {
		
	   try {
		   PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		   PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,  "BusSnoozeAlarm");
		   //Acquire the lock
		   wl.acquire();
	
		   registry = Registry.getInstance();

	        ReminderTimes rTimes = new ReminderTimes();
	        long reminder1_begin_time = rTimes.getLimitMillis(0);
	        long reminder1_end_time = rTimes.getLimitMillis(1);
	    	long reminder2_begin_time = rTimes.getLimitMillis(2);
	    	long reminder2_end_time = rTimes.getLimitMillis(3);
	    	long current_time = Calendar.getInstance().getTimeInMillis();
			
	    	Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(reminder1_end_time);
			cal.add(Calendar.MINUTE, registry.getDelayTime());
			cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
	     	cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		
			Calendar cal2 = Calendar.getInstance();
			cal2.setTimeInMillis(reminder2_end_time);
			cal2.add(Calendar.MINUTE, registry.getDelayTime());
			cal2.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
	     	cal2.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

			if (!registry.getFirstAlarm() && current_time >= reminder1_begin_time && current_time <= reminder1_end_time) {
				startReminderActivity(context);
				registry.setFirstAlarm(true);
				registry.setSecondAlarm(false);
				registry.setThirdAlarm(false);
			}
			else if (!registry.getSecondAlarm() && current_time >= reminder2_begin_time && current_time <= reminder2_end_time) {				
				startReminderActivity(context);	
				registry.setFirstAlarm(false);
				registry.setSecondAlarm(true);
				registry.setThirdAlarm(false);
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

	private void startReminderActivity(final Context context) {
		Intent newIntent = new Intent(context, ReminderActivity.class);
		   newIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
		   //newIntent.putExtra("alarm_message", message);
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
		Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, identifier, intent, 0);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Log.d("LogoutAlarmReceiver", "alarm has been reset repeating" + identifier);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY,  pi);
     }


	 public void cancelAlarm(Context context, int identifier) {
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, identifier, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.d("LogoutAlarmReceiver", "alarm has been cancelled");
    }
	 
	 public void setOnceAlarm(Context context, long reminderTime, int identifier) {
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, identifier, intent, 0);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Log.d("LogoutAlarmReceiver", "alarm has been reset once");
        am.set(AlarmManager.RTC_WAKEUP, reminderTime, pi);
     }
	
}

