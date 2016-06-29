package com.phitlab.lowliteracymexicanw.login;

import java.util.Calendar;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "NewApi", "HandlerLeak" })
public class LogoutActivity_BAK extends Activity {
	private static final int TENS = 20;
	//private MediaPlayer mMediaPlayer;
    private Dialog dialog;
	private Registry registry;
	private int record_id;
	protected int title_sound;
	private PendingIntent pendingIntent;
	private Intent logoutIntent;
	private AlarmManager am;
	private PowerManager.WakeLock wl;
	private TaskStackBuilder stackBuilder;
	protected boolean WINDOW_EXISTS;
	public static Handler h;
	
	@Override
	protected void onStop() {
	    super.onStop();
	   // wl.release();
	    if(dialog!= null)
	    	dialog.dismiss();
	    //logoutWarning();
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if(wl.isHeld())
	    	wl.release();
	    if(dialog!= null)
	    	dialog.dismiss();
	    //logoutWarning();
	}
	
	@Override
	public void onPause() {
	    super.onPause();
//	    if(wl.isHeld())
//	    	wl.release();
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE); 
	    wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
	            PowerManager.ACQUIRE_CAUSES_WAKEUP
	            | PowerManager.ON_AFTER_RELEASE, "BusSnoozeAlarm"); 
        wl.acquire();
        
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        	    WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        h = new Handler() {
            @Override
			public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                case 0:
                    LogoutActivity_BAK.this.finish();
                    break;
                }
            }
        };

        
        final Bundle b =getIntent().getExtras();
        Log.d("LogoutActivity_BAK", "reminder received" + b.getBoolean("LOGOUT"));
        
        registry = Registry.getInstance();
		//registry.getLog().logNewClick();
	}
    
    @Override
    public void onStart() {
    	super.onStart();
    	
    	final Bundle b =getIntent().getExtras();
    	if (b.getBoolean("LOGOUT")) {
			 int loop=0;
			//playAlarm(this, getAlarmUri());
    		while (loop < TENS) {
    			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
    			r.play(); 
    			loop ++;
    		}
    		displayPopup(); 
			 //wl.release();
		 }
		 else 
			 finish();
    }
 
    
    private void logoutTimer() {
    	//final Handler handler = new Handler();
    	Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.SECOND, 300);
       // registry.getLog().completeReminderLog(record_id, 1);
        Log.d("logoutTimer", ""+cal.getTimeInMillis());
        
		//dialog.dismiss();
    	logoutIntent = new Intent(LogoutActivity_BAK.this, LoginActivity.class);
    	logoutIntent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
    	logoutIntent.putExtra("startDialog", true);
    	stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(LoginActivity.class);
		stackBuilder.addNextIntent(logoutIntent);
		
        pendingIntent = stackBuilder.getPendingIntent(56789, PendingIntent.FLAG_CANCEL_CURRENT);      
		am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_up, menu);
		return true;
	}

	private void displayPopup() {
    	//dialog = new Dialog(this,R.style.FullHeightDialog);
		dialog = new Dialog(this);
		dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_login_message);
		//dialog.setTitle(getResources().getString(R.string.logout_reminder));
		dialog.setCancelable(false);
	    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
	        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
	        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
	        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
	        	    WindowManager.LayoutParams.FLAG_FULLSCREEN |
	        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
	        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
	        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); 
		
	    ((ImageView)dialog.findViewById(R.id.confirmation)).setImageResource(R.drawable.lock);
		((TextView)dialog.findViewById(R.id.message)).setText(getResources().getString(R.string.logout_reminder));
		dialog.setCancelable(false);
		
		((ImageView) dialog.findViewById(R.id.sound_Icon)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//playSound("logout_reminder.ogg");
			//	registry.getLog().logAction(title_sound++, "title_sound", "click_log");
			}
			
		});
		
	//	record_id = registry.getLog().logReminder("eod_reminder");
		((ImageView) dialog.findViewById(R.id.ok)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PendingIntent.getActivity(LogoutActivity_BAK.this,  56789,logoutIntent, 
							   PendingIntent.FLAG_UPDATE_CURRENT).cancel();
				am.cancel(pendingIntent);
					   
				//registry.getLog().logAction(1, "yes_icon", "click_log");
			//	registry.getLog().logFinalClick(11, 0);
				//registry.getLog().logChoice("behavior", 9);
				//registry.getLog().completeReminderLog(record_id, 1);
    			dialog.dismiss();
    			//resetReminderTimer();
    			finish();
    			//logoutWarning();
			}
		});
		this.dialog.show();	
		logoutTimer();
    }
    
}
