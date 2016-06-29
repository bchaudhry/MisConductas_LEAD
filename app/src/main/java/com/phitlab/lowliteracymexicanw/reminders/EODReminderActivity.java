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


public class EODReminderActivity<StateHolder> extends BaseActivity {

	private Dialog dig = null;
    protected Registry registry = Registry.getInstance();
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
	Calendar curr_cal;
	Calendar eod_cal = Calendar.getInstance();
	long eod_time;
	long current_time;
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
		
     	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        	    WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		eod_cal.setTimeInMillis(eod_reminder);
		eod_cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		eod_cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		eod_cal.set(Calendar.HOUR_OF_DAY, 23);
		eod_cal.set(Calendar.MINUTE, registry.getDelayTime());

		eod_time = eod_cal.getTimeInMillis();
		current_time = Calendar.getInstance().getTimeInMillis();
		if ( !registry.getThirdSignal() && current_time >= eod_reminder && current_time <= eod_time) {
			reminderlogID = registry.getLog().logNewReminder("reminder_type", 3);
			System.out.println("LOGGED"+"reminder3");
            registry.setFirstSignal(false);
            registry.setSecondSignal(false);
            registry.setThirdSignal(true);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
    	super.onResume();
    	if(dig != null)
        	controlDialog();
      	else 
    		displayPopup();
	}
	
	
	@Override
	protected void onDestroy() {
    	super.onDestroy();
    	 System.out.println("UNLOGGED");
    	dig.dismiss();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_up, menu);
		return true;
	}
	
	/*
	 * Display the reminder to take end-of-day questionnaire
	 * */
	
    public void displayPopup() {
		dig = new Dialog(EODReminderActivity.this);
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
		
		Log.d("REMINDERALARM", "EOD ALARM POPUP ACTIVITY");
		
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
				registry.getLog().logFinalClick("yes_icon", 1);
				registry.getLog().completeReminderLog(1, reminderlogID);
				registry.setMorningReset(false);
				registry.setEveningReset(false);
				registry.setEODReset(true);
				Intent intent = null;
				
				if(registry.getLoginStatus()) {
					intent = new Intent(EODReminderActivity.this, BehaviorActivity.class);
					intent.putExtra("TITLE", getResources().getString(R.string.behavior_title));	
				} else {
					intent = new Intent(EODReminderActivity.this, LoginActivity.class);
					intent.putExtra("REMINDER", 1);	
				}
				startActivity(intent);
				
    	    	if(dig != null && dig.isShowing()) {
    	    		EODReminderActivity.this.finish();
					EODReminderActivity.this.dig.dismiss();
					EODReminderActivity.this.showToast();
    	    	}
    	    	
			}
		});
  
		ImageView btnCrossButton = (ImageView) dig.findViewById(R.id.remove);
		btnCrossButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registry.getLog().logFinalClick("no_icon", 1);				
				playSound("thanks.ogg");
				registry.getLog().completeReminderLog(-1, reminderlogID);
				registry.setMorningReset(false);
				registry.setEveningReset(false);
				registry.setEODReset(true);
				
				if (!registry.getLoginStatus()) {
					Intent intent = new Intent(EODReminderActivity.this, LoginActivity.class);
					intent.putExtra("REMINDER", 1);	
					startActivity(intent);
				}
				
				if(dig != null && dig.isShowing()) {
					EODReminderActivity.this.finish();
					EODReminderActivity.this.dig.dismiss();
					EODReminderActivity.this.showToast();
				}
			}
		});

		Calendar curr_cal = Calendar.getInstance();
		current_time = curr_cal.getTimeInMillis();
		if (!registry.getEODReset() && current_time <= eod_time) {
	        dig.show();
	    } else if (!registry.getEODReset() && current_time > eod_time) {
	    	registry.getLog().completeReminderLog(0, reminderlogID);
	    	registry.setMorningReset(false);
			registry.setEveningReset(false);
			registry.setEODReset(true);
			dig.dismiss();
	    	finish();
	    }
    }

	private void controlDialog() {
		Calendar curr_cal = Calendar.getInstance();
		current_time = curr_cal.getTimeInMillis();
		
		if (current_time > eod_time) {
			if(!registry.getEODReset()) { 
				registry.setMorningReset(false);
				registry.setEveningReset(false);
				registry.setEODReset(true);
				registry.getLog().completeReminderLog(0, reminderlogID);
			}	
			dig.dismiss();
			finish();	
			System.out.println("KEYCODE_HOME22");
		}
		else if(!registry.getEODReset() && !dig.isShowing()) {
			dig.show();
			System.out.println("KEYCODE_HOME44");
		}
		else if(!registry.getEODReset()) {
			dig.show();
			System.out.println("KEYCODE_HOME33");
		}
		System.out.println("KEYCODE_HOME");
	}
	
//	@Override
//	protected void onPause() {
//    	super.onPause();
//    	if(dig != null)
//    		displayPopup();
//	}
	
//	@Override
//	protected void onStop() {
//    	super.onStop();
//    	if(!registry.getEODReset()) {
//			dig.show();
//			registry.getLog().completeReminderLog(0);
//			System.out.println("KEYCODE_HOME55");
//		}
//		System.out.println("KEYCODE_HOME$$");
//	}
//	
	@Override
	protected void onStop() {
    	super.onStop();
    	if(registry.getEODReset()) {
    		System.out.println("KEYCODE_HOME55finish");
    		if (dig!=null) dig.dismiss();
//    		registry.setReminder(false);
    		finish();			
		} else {
			dig.show();
//			registry.setReminder(true);
			registry.getLog().completeReminderLog(0, reminderlogID);
			System.out.println("KEYCODE_HOME55");
		}
		
		System.out.println("KEYCODE_HOME$$");
	}
	
	@Override
	public void onBackPressed() {
		 System.out.println("KEYCODE_BACK");
	}

	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {     

	        if(keyCode == KeyEvent.KEYCODE_HOME)
	        {
	        	 System.out.println("KEYCODE_HOME");
	    	}
	        return true;
	    }
	
}
