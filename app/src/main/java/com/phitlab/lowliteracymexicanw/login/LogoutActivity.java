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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
public class LogoutActivity extends Activity {
    private Dialog dialog;
	private Registry registry;
	private int record_id;
	protected int title_sound;
	private PendingIntent pendingIntent;
	private Intent logoutIntent;
	private AlarmManager am;
	private TaskStackBuilder stackBuilder;
	protected boolean WINDOW_EXISTS;
	private MediaPlayer mp;
	public static Handler h;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        	    WindowManager.LayoutParams.FLAG_FULLSCREEN |
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
           
        registry = Registry.getInstance();
	}
    
    @Override
    public void onStart() {
    	super.onStart();
    	displayPopup(); 
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pop_up, menu);
		return true;
	}

	private void displayPopup() {
		dialog = new Dialog(this);
		dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_login_message);
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
				playSound("logout_reminder.ogg");
				registry.getLog().logClick(title_sound++, "title_sound");
			}			
		});
		
		((ImageView) dialog.findViewById(R.id.ok)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PendingIntent.getActivity(LogoutActivity.this,56789,logoutIntent, 
							   PendingIntent.FLAG_UPDATE_CURRENT).cancel();
				am.cancel(pendingIntent);
    			dialog.dismiss();
    			finish();
			}
		});
		this.dialog.show();	
		logoutTimer();
    }
 
    
    protected void playSound(String filename) {
    	mp = new MediaPlayer();
        try {
            mp.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Data/AppAudio/"+filename);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }	
	}

	private void logoutTimer() {
    	Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 180);
        Log.d("logoutTimer", ""+cal.getTimeInMillis());

    	logoutIntent = new Intent(LogoutActivity.this, LoginActivity.class);
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
	protected void onStop() {
	    super.onStop();
	    stopPlaying();
	    if(dialog!= null)
	    	dialog.dismiss();
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if(dialog!= null)
	    	dialog.dismiss();
	}
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }
}
