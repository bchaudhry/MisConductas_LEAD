package com.phitlab.lowliteracymexicanw.gcm;
/* http://techlovejump.in/2013/11/android-push-notification-using-google-cloud-messaging-gcm-php-google-play-service-library/
 * techlovejump.in
 * tutorial link
 * 
 *  */

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.objects.ReminderTimes;
import com.phitlab.lowliteracymexicanw.reminders.EODAlarmReceiver;
import com.phitlab.lowliteracymexicanw.reminders.ReminderAlarmReceiver;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class GcmIntentService extends IntentService{
	Context context;
	public static final int NOTIFICATION_ID = 4;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "GCM Demo";
    Registry registry; 
    
	public GcmIntentService() {
		super("GcmIntentService");
		registry = Registry.getInstance();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		String msg = "";
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		String msg2 = "";
		
		if (!extras.isEmpty()) {
			 
			 if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
	                sendNotification("Send error: " + extras.toString(), "GCM Notification", R.drawable.ic_launcher);
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_DELETED.equals(messageType)) {
	                sendNotification("Deleted messages on server: " +
	                        extras.toString(), "GCM Notification", R.drawable.ic_launcher);
	            // If it's a regular GCM message, do some work.
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
	            	int icon = R.drawable.check;
	            	//System.out.println(""+intent.getStringExtra("INFO").trim());
	            	if (intent.getStringExtra("PASSWORD")!= null) {
	            		msg = intent.getStringExtra("PASSWORD").trim();
	            		registry.getLog().updatePassword(msg);
	            		msg2 = "Contraseña Nueva";
	            		icon = R.drawable.new_pass;
	            	}
	            	else if (intent.getStringExtra("REMINDER1_BEGIN")!= null) {
	            		String begin_time = intent.getStringExtra("REMINDER1_BEGIN").trim();
	            		String end_time = intent.getStringExtra("REMINDER1_END").trim();
	            		msg = begin_time + "-" + end_time;
	            		msg2 = "Tiempo de la Señal Nuevo";
	            		icon = R.drawable.time8;
	            		registry.getLog().updateReminderTime(1, begin_time, end_time);
		        		ReminderTimes reminder_times = new ReminderTimes(); 
		        		ReminderAlarmReceiver alarm_receiver = new ReminderAlarmReceiver();
		        		alarm_receiver.cancelAlarm(getApplicationContext(), 0);
			        	alarm_receiver.setAlarm(getApplicationContext(), reminder_times.getNewReminderMillis(0), 0);
			        	registry.getLog().clearUpdate(1);
	            	}
	            	else if (intent.getStringExtra("REMINDER2_BEGIN")!= null) {
	            		String begin_time = intent.getStringExtra("REMINDER2_BEGIN").trim();
	            		String end_time = intent.getStringExtra("REMINDER2_END").trim();
	            		msg = begin_time + "-" + end_time;
	            		msg2 = "Tiempo de la Señal Nuevo";
	            		icon = R.drawable.time8;
	            		registry.getLog().updateReminderTime(2, begin_time, end_time);
		        		ReminderTimes reminder_times = new ReminderTimes(); 
		        		ReminderAlarmReceiver alarm_receiver = new ReminderAlarmReceiver();
		        		alarm_receiver.cancelAlarm(getApplicationContext(), 1);
			        	alarm_receiver.setAlarm(getApplicationContext(), reminder_times.getNewReminderMillis(2), 1);
			        	registry.getLog().clearUpdate(2);
	            	}
	            	else if (intent.getStringExtra("EOD_REMINDER")!= null) {
	            		msg = intent.getStringExtra("EOD_REMINDER").trim();
	            		msg2 = "Tiempo de la Señal Nuevo";
	            		icon = R.drawable.time8;
	            		registry.getLog().updateEODReminder(msg);
		        		ReminderTimes reminder_times = new ReminderTimes(); 
	            		EODAlarmReceiver eod_alarm = new EODAlarmReceiver();
	            		eod_alarm.cancelAlarm(getApplicationContext(), 2);
			        	eod_alarm.setAlarm(getApplicationContext(), reminder_times.getNextEODMillis(), 2);
			        	registry.getLog().clearUpdate(3);
	            	}
	            	else if (intent.getStringExtra("URL") != null) {
	            		msg = intent.getStringExtra("URL").trim();
	            		msg2 = "Downloading ..."; 
	                    icon = R.drawable.ic_launcher;
	            	}
	            	else if (intent.getStringExtra("INFORMATION")!= null) {
	            		msg = intent.getStringExtra("INFORMATION").trim();
	                    msg2 = "Your Details";
	                    icon = R.drawable.question_mark;
	            	}
	                sendNotification(msg, msg2, icon);
	                Intent myintent = new Intent(this, ReceiveActivity.class);
	                myintent.putExtra("title", msg2);
	                myintent.putExtra("notification", msg);
	                myintent.putExtra("ICON", icon);
	                myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(myintent);
	                Log.i(TAG, "Received: " + extras.toString());
	            }
	        }
		 GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	
	private void sendNotification(String msg, String msg2, int icon) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(this, ReceiveActivity.class);
        myintent.putExtra("title", msg2);
        myintent.putExtra("notification", msg);
        myintent.putExtra("ICON", icon);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 5,
        		myintent, PendingIntent.FLAG_CANCEL_CURRENT);
        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(icon)
        .setContentTitle(msg2)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}