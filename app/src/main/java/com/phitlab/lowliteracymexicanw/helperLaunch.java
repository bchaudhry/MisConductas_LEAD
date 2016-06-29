package com.phitlab.lowliteracymexicanw;

import com.phitlab.lowliteracymexicanw.login.LoginActivity;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.reminders.ReminderActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class helperLaunch extends Activity{
	private Registry registry;

	@Override
	public void onCreate(Bundle b){
	    super.onCreate(b);
	    registry = Registry.getInstance();
	    //determine what activity you want
	    if (registry.getReminder()) {			
	    	Intent intent = new Intent(this, ReminderActivity.class);
	    	intent.putExtra("LAUNCHER", 1);	
	    	startActivity(new Intent(this, ReminderActivity.class));
	    }
	    else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
	    }
	    finish();
	}
}
