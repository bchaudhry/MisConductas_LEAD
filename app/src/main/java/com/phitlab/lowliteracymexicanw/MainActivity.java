package com.phitlab.lowliteracymexicanw;

import java.util.Calendar;

import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.login.LoginActivity;
import com.phitlab.lowliteracymexicanw.login.UpdatePasswordActivity;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.objects.ReminderTimes;
import com.phitlab.lowliteracymexicanw.reminders.ReminderActivity;
import com.phitlab.lowliteracymexicanw.reminders.EODAlarmReceiver;
import com.phitlab.lowliteracymexicanw.reminders.ReminderAlarmReceiver;

import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/*
 * This activity is the main menu activity
 * Here user selects behavior he performed or end-of-day questionnaire to take
 * */

public class MainActivity extends BaseActivity {

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main_menu);

	        registry = Registry.getInstance();
	        
	        LinearLayout leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
	        RelativeLayout passwordLayout = (RelativeLayout) findViewById(R.id.passwordLayout);
	        LinearLayout rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
	        if(TRAIN) {
	        	leftLayout.setVisibility(View.VISIBLE);
	        	passwordLayout.setVisibility(View.GONE);
	        	rightLayout.setVisibility(View.VISIBLE);
	        }
	        else {
	        	leftLayout.setVisibility(View.GONE);
	        	passwordLayout.setVisibility(View.VISIBLE);
	        	rightLayout.setVisibility(View.GONE);
	        }
	        
			ImageView titleSound = (ImageView) findViewById(R.id.playVoice);
			titleSound.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					playSound("main.ogg");	
					registry.getLog().logClick(1, "title_sound");
				}
				
			});
			soundfile = this.getIntent().getStringExtra("SOUND");
			//playSound(soundfile);
			registry.getLog().logNewClick(0);
						
	        ((ImageButton)findViewById(R.id.btnresetPassword)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, UpdatePasswordActivity.class);
			    	intent.putExtra("TITLE", getResources().getString(R.string.update_password));
			    	intent.putExtra("SOUND", "new_password.ogg");
			    	registry.getLog().logFinalClick("icon2", 1);
			        startActivity(intent);
				}	
	        });
	        
	        ((ImageButton) findViewById(R.id.updatePassword)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					playSound("enter_new_password.ogg");	
					registry.getLog().logClick(1, "title_sound");
				}
				
			});
	        
	        ((ImageButton) findViewById(R.id.btnforceLogout)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//	                Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext());
//	                notificationBuilder.setSmallIcon(R.drawable.check)
//	                        .setTicker("hello")
//	                        .setWhen(System.currentTimeMillis())
//	                        .setContentTitle("New message")
//	                        .setContentText("How are you")
//	                        .setContentInfo("")
//	                        .setOngoing(false);
//
//	                Notification notification = notificationBuilder.build();
//	                notification.defaults = Notification.DEFAULT_ALL;
//	                notificationManager.notify(1, notification);
	                
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			        startActivity(intent);
			        finish();
				}
				
			});
	        
	        ((ImageButton) findViewById(R.id.btnforceReminder)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
			        startActivity(intent);
				}
				
			});
	        //logoutWarning.SetAlarm(this, registry.getUserId());   
	        super.SetAlarm(); 
	    }
	    
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
	        return true;
	    }

		/** Called when the user clicks the Send button */
	    public void behaviorOnClick(View view) {
	    	Intent intent = new Intent(this, BehaviorActivity.class);
	    	intent.putExtra("TITLE", getResources().getString(R.string.behavior_title));
	    	intent.putExtra("SOUND", "behavior.ogg");
	    	registry.getLog().logFinalClick("icon1", 1);
	    	//playSound("main2.ogg");	
	        startActivity(intent);
	        Log.d("onClick", "behavior on click");
	    }
		
	    @Override
	    public void onBackPressed() {
	    	moveTaskToBack(true);
	    }
	    
	    @Override
	    public void onResume() {
	    	super.onResume();
	    	checkReminderReset();
	    }
	    
	    public void checkReminderReset() {
			
    		ReminderTimes reminderTimes = new ReminderTimes(); 
	    	
    		if(registry.getLog().getReminderStatus(1)) {
	    		ReminderAlarmReceiver reminderAlarm = new ReminderAlarmReceiver();
	    		reminderAlarm.cancelAlarm(context, 0);
	    		reminderAlarm.setAlarm(context, reminderTimes.getNewReminderMillis(0), 0);
	    		Log.e("BaseActivity", "alarm has been reset first reminder");
	    		registry.getLog().clearUpdate(1);
	    	}
			
    		if(registry.getLog().getReminderStatus(2)) {
	    		ReminderAlarmReceiver reminderAlarm = new ReminderAlarmReceiver();
	    		reminderAlarm.cancelAlarm(context, 1);
	    		reminderAlarm.setAlarm(context, reminderTimes.getNewReminderMillis(2), 1);
	    		Log.e("BaseActivity", "alarm has been reset second reminder");
	    		registry.getLog().clearUpdate(2);
	    	}
	    	
    		if(registry.getLog().getReminderStatus(3)) {
	    		EODAlarmReceiver reminderAlarm = new EODAlarmReceiver();
	    		reminderAlarm.cancelAlarm(context, 2);
	    		reminderAlarm.setAlarm(context, reminderTimes.getNextEODMillis(), 2);
	    		Log.e("BaseActivity", "alarm has been reset last reminder");
	    		registry.getLog().clearUpdate(3);
	    	}
			Log.d("BaseActivity", "alarm has been reset");
		}

}
