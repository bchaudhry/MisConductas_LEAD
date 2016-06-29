package com.phitlab.lowliteracymexicanw.reminders;

import java.util.ArrayList;
import java.util.Calendar;
import org.apache.http.NameValuePair;
import com.phitlab.lowliteracymexicanw.BaseActivity;
import com.phitlab.lowliteracymexicanw.BehaviorActivity;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.login.LoginActivity;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.objects.ReminderTimes;

import android.media.Ringtone;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public class ReminderActivity<StateHolder> extends BaseActivity {

	private Dialog dig = null;
    Registry registry = Registry.getInstance();
	protected int title_sound=0;
	int record_id = 0;
	ArrayList<NameValuePair> reminders = new ArrayList<NameValuePair>();
	Ringtone r;
	public static long MILLIS = 5000;
	private ReminderTimes rTimes;
	long reminder1_begin_time;
	long reminder1_end_time;
	long reminder2_begin_time;
	long reminder2_end_time ;
	long eod_reminder;
	Calendar cal;
	Calendar cal2;
	private int reminder_type;
    int reminderlogID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        rTimes = new ReminderTimes();
        reminder1_begin_time = rTimes.getLimitMillis(0);
    	reminder1_end_time = rTimes.getLimitMillis(1);
    	reminder2_begin_time = rTimes.getLimitMillis(2);
    	reminder2_end_time = rTimes.getLimitMillis(3);
     	eod_reminder = rTimes.getLimitMillis(4);
		reminder_type = 0;
		
     	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        	    WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		Log.d("ReminderActivity", "onCreate");
		
	 	long current_time = Calendar.getInstance().getTimeInMillis();
		if (!registry.getFirstSignal() && current_time <= reminder1_end_time && current_time >= reminder1_begin_time ) {
			 if (getIntent().hasExtra("LAUNCHER")) {
			 
			 } else {
				 reminderlogID = registry.getLog().logNewReminder("reminder_type", 1);
                 registry.setFirstSignal(true);
                 registry.setSecondSignal(false);
                 registry.setThirdSignal(false);
             }
			reminder_type =1;
			Log.d("Reminder Activity", "First Reminder");
		}
		else if (!registry.getSecondSignal() && current_time <= reminder2_end_time && current_time >= reminder2_begin_time) {
			if (getIntent().hasExtra("LAUNCHER")) {
				 
			 } else {
				 reminderlogID = registry.getLog().logNewReminder("reminder_type", 2);
                 registry.setFirstSignal(false);
                 registry.setSecondSignal(true);
                 registry.setThirdSignal(false);
			 }
			registry.setMorningReset(false);
			reminder_type =2;
			Log.d("Reminder Activity", "Second Reminder");
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("ReminderActivity", "onStart");
	}
	

	@Override
	protected void onResume() {
    	super.onResume();
    	Log.d("ReminderActivity", "onResume getMorningReset" +registry.getMorningReset());
		Log.d("ReminderActivity", "onResume getEveningReset" +registry.getEveningReset());
		if(dig!=null) 
			dismissDialog();
		else 
			displayPopup();
		Log.d("ReminderActivity", "onResume getMorningReset" +registry.getMorningReset());
		Log.d("ReminderActivity", "onResume getEveningReset" +registry.getEveningReset());
	}
	
	@Override
	protected void onDestroy() {
    	super.onDestroy();
    	Log.d("ReminderActivity", "onDestroy");
//    	if(registry.getMorningReset() || registry.getEveningReset()) {
//    		System.out.println("KEYCODE_HOME55finish");
//    		if (dig!=null) dig.dismiss();
//    		finish();			
//		} else {
//			dig.show();
//			registry.getLog().completeReminderLog(0);
//			System.out.println("KEYCODE_HOME55");
//		}
		
    	//if(registry.getMorningReset() && registry.getEveningReset()) 
    		dig.dismiss();
    	//else
    	//	dig.show();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	/*
	 * Display the reminder to take end-of-day questionnaire
	 * */
	
    public void displayPopup() {
    	Log.d("ReminderActivity", "displayPopup");
		dig = new Dialog(ReminderActivity.this);
		dig.setContentView(R.layout.dialog_behavior);
		runOnUiThread(new Runnable() {
		    @Override
			public void run() {
		    	TextView reminderQuestion = (TextView) dig.findViewById(R.id.reminder_question);
		    	reminderQuestion.setText(R.string.reminder_question);
		    }
		});
		dig.setTitle(getResources().getString(R.string.menu_settings));
		dig.setCancelable(false);
		dig.setCanceledOnTouchOutside(false);
		registry.getLog().logNewClick(9);
		
	    dig.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
	        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
	        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
	        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
	        	    WindowManager.LayoutParams.FLAG_FULLSCREEN |
	        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
	        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
	        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		Log.d("REMINDERALARM", "MIDDAY ALARM POPUP ACTIVITY");
		
		ImageView titleSound = (ImageView) dig.findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound("reminder_question.ogg");
				registry.getLog().logClick(title_sound++, "title_sound");
			}			
		});
	 	
		ImageView btnCheckButton = (ImageView) dig.findViewById(R.id.ok);
		btnCheckButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//long current_time = Calendar.getInstance().getTimeInMillis();
				registry.getLog().logFinalClick("yes_icon", 1);
				Intent intent = null;
				registry.getLog().completeReminderLog(1, reminderlogID);
				
				if(registry.getLoginStatus()) {
					intent = new Intent(ReminderActivity.this, BehaviorActivity.class);
					intent.putExtra("TITLE", getResources().getString(R.string.behavior_title));	
				} else {
					intent = new Intent(ReminderActivity.this, LoginActivity.class);
					intent.putExtra("REMINDER", 1);	
				}
				
				//if (!registry.getMorningReset() && current_time >= reminder1_begin_time && current_time <= reminder1_end_time) {		
				if (!registry.getMorningReset() && reminder_type == 1) {
					registry.setMorningReset(true);
					registry.setEveningReset(false);
					registry.setEODReset(false);
					Log.d("Reminder Activity", "Reminder 1 check");
				}
				//else if(!registry.getEveningReset() && current_time >= reminder2_begin_time && current_time <= reminder2_end_time) {
				else if (!registry.getEveningReset() && reminder_type == 2) {
					registry.setMorningReset(false);
					registry.setEveningReset(true);
					registry.setEODReset(false);					
					Log.d("Reminder Activity", "Reminder 2 check");
				} 
				
				if (intent != null) startActivity(intent);
    	    	if(dig != null && dig.isShowing()) {
    	    		ReminderActivity.this.dig.dismiss();
    	    		ReminderActivity.this.showToast();
    	    		ReminderActivity.this.finish();
    	    	}
    	    	Log.d("Reminder Activity", "MorningReset" +registry.getMorningReset());
    			Log.d("Reminder Activity", "EveningReset" +registry.getEveningReset());
			}
		});
  
		ImageView btnCrossButton = (ImageView) dig.findViewById(R.id.remove);
		btnCrossButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//long current_time = Calendar.getInstance().getTimeInMillis();
				registry.getLog().logFinalClick("no_icon", 1);	
				registry.getLog().completeReminderLog(-1, reminderlogID);
				Intent intent = null;
				
				if (!registry.getLoginStatus()) {
					intent = new Intent(ReminderActivity.this, LoginActivity.class);
					intent.putExtra("REMINDER", 1);	
				}
				
				//if (!registry.getMorningReset() && current_time >= reminder1_begin_time && current_time <= reminder1_end_time) {		
				if (reminder_type == 1 && !registry.getMorningReset() ) {
					registry.setMorningReset(true);
					registry.setEveningReset(false);
					registry.setEODReset(false);	
				}
				//else if(!registry.getEveningReset() && current_time >= reminder2_begin_time && current_time <= reminder2_end_time) {
				else if (reminder_type == 2 && !registry.getEveningReset()) {
					registry.setMorningReset(false);
					registry.setEveningReset(true);
					registry.setEODReset(false);
				} 
				
				if (intent != null) startActivity(intent);
				if(dig != null && dig.isShowing()) {
					ReminderActivity.this.dig.dismiss();
					ReminderActivity.this.showToast();
					ReminderActivity.this.finish();
				}
			}
		});
		Log.d("Reminder Activity", "MorningReset" +registry.getMorningReset());
		Log.d("Reminder Activity", "EveningReset" +registry.getEveningReset());
		Log.d("Reminder Activity", "EODReset" +registry.getEODReset());
		long current_time = Calendar.getInstance().getTimeInMillis();
		
		if (!registry.getMorningReset() && current_time >= reminder1_begin_time && current_time <= reminder1_end_time) {		
			dig.show();
			Log.d("Reminder Activity", "First Reminder"); //!registry.getMorningReset() && 
		}
		else if (!registry.getEveningReset() && current_time >= reminder2_begin_time && current_time <= reminder2_end_time) {
			dig.show();
			Log.d("Reminder Activity", "Second Reminder"); //!registry.getEveningReset() && 
		} 
		else {
			if (dig!=null)dig.dismiss();
			finish();
		}
    }

	private void dismissDialog() {
		Log.d("ReminderActivity", "dismissDialog");
		long current_time = Calendar.getInstance().getTimeInMillis();

		if (current_time > reminder1_end_time && current_time < reminder2_begin_time) {
			if(!registry.getMorningReset()) {
				registry.getLog().completeReminderLog(0, reminderlogID);
				registry.setMorningReset(true);
				registry.setEveningReset(false);
				registry.setEODReset(false);
				Log.d("dismissDialog", "first if");
			}		
			dig.dismiss();
			finish();
		}
//		else if (reminder_type == 1 && current_time > reminder1_end_time) {
//			if(!registry.getMorningReset()) {
//				registry.getLog().completeReminderLog(0);
//				registry.setMorningReset(true);
//				registry.setEveningReset(false);
//				registry.setEODReset(false);
//				Log.d("dismissDialog", "second if");
//			}		
//			dig.dismiss();
//			finish();
//		}
		else if (current_time > reminder2_end_time && current_time < eod_reminder) {
			if(!registry.getEveningReset()) {
				registry.getLog().completeReminderLog(0, reminderlogID);
				registry.setMorningReset(false);
				registry.setEveningReset(true);
				registry.setEODReset(false);
				Log.d("dismissDialog", "third if");
			}	
			dig.dismiss();
			finish();
		}
//		else if (reminder_type == 2 && current_time > reminder2_end_time) {
//			if(!registry.getEveningReset()) {
//				registry.getLog().completeReminderLog(0);
//				registry.setMorningReset(false);
//				registry.setEveningReset(true);
//				registry.setEODReset(false);
//				Log.d("dismissDialog", "fourth if");
//			}	
//			dig.dismiss();
//			finish();
//		}
		else if(!registry.getMorningReset()) {// && !dig.isShowing()) {
			reminder_type = 1;
			dig.show();
		}
		else if(!registry.getEveningReset()) {//&& !dig.isShowing()) {
			reminder_type = 2;
			dig.show();
		} 
		else {
			dig.dismiss();
			finish();
		}
		Log.d("dismissDialog", "MorningReset" +registry.getMorningReset());
		Log.d("dismissDialog", "EveningReset" +registry.getEveningReset());
	}

	
	@Override
	public void onBackPressed() {
		 System.out.println("KEYCODE_BACK");
	}
	 
//	
//	@Override
//	protected void onPause() {
//    	super.onPause();
//    	if(dig != null)
//    		displayPopup();
//	}
//	
	@Override
	protected void onStop() {
    	super.onStop();
    	if(registry.getMorningReset() || registry.getEveningReset()) {
    		System.out.println("KEYCODE_HOME55finish");
    		if (dig!=null) dig.dismiss();
    		registry.setReminder(false);
    		finish();			
		} else {
			dig.show();
			//if (dig!=null) dig.dismiss();
			registry.setReminder(true);
			registry.getLog().completeReminderLog(0, reminderlogID);
			System.out.println("KEYCODE_HOME55");
    		//finish();
		}
		
		System.out.println("KEYCODE_HOME$$");
	}
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {     

        if(keyCode == KeyEvent.KEYCODE_HOME)
        {
           //The Code Want to Perform. 
        }
        return true;
    }
}
