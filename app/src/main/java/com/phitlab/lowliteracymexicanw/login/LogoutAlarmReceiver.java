package com.phitlab.lowliteracymexicanw.login;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.os.Bundle;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class LogoutAlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
	   try {
		   Log.d("alarm", "received");  
		   PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		   PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,  "BusSnoozeAlarm");
		   wl.acquire();
		   
		   Intent newIntent = new Intent(context, LogoutActivity.class);
		   //newIntent.putExtra("alarm_message", message);
		   newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   context.startActivity(newIntent);
		   
		   Uri notifyuri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	       final Ringtone r = RingtoneManager.getRingtone(context, notifyuri);
	       new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							r.play();
							Thread.sleep(5000);
							r.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
		  	   }).start();	
	       
		   if(wl.isHeld()){
               wl.release();
           }
	    } 
	   catch (Exception e) {
	      Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
	      e.printStackTrace();
	 
	    }
	}
	
	public void SetAlarm(Context context, int identifier) {
		
		  AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		  Calendar cal = Calendar.getInstance();
		  cal.add(Calendar.SECOND, 900);
	      Intent intent = new Intent(context, LogoutAlarmReceiver.class);
	      PendingIntent pi = PendingIntent.getBroadcast(context, identifier, intent, 0);
	      am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
	      Log.d("LogoutAlarmReceiver", "alarm has been set");
	   }
	
	 public void CancelAlarm(Context context, int identifier) {
	        Intent intent = new Intent(context, LogoutAlarmReceiver.class);
	        PendingIntent sender = PendingIntent.getBroadcast(context, identifier, intent, 0);
	        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	        alarmManager.cancel(sender);
	        Log.d("LogoutAlarmReceiver", "alarm has been cancelled");
	    }	    

}

