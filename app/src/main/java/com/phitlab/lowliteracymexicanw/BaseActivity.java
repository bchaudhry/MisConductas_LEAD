package com.phitlab.lowliteracymexicanw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.login.LoginActivity;
import com.phitlab.lowliteracymexicanw.login.LogoutAlarmReceiver;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.objects.ReminderTimes;
import com.phitlab.lowliteracymexicanw.reminders.EODReminderActivity;
import com.phitlab.lowliteracymexicanw.reminders.ReminderAlarmReceiver;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class is extended by all activities
 * */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class BaseActivity extends Activity{

	protected static Context context; 
	//public Intent reminderIntent;
	public AlarmManager am;
	public PendingIntent pendingIntent;
	private MediaPlayer mp;
	public String soundfile;
	public Registry registry;
	protected int title_sound;
	LogoutAlarmReceiver logoutWarning;
	private int id;
	public final static boolean TRAIN = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = getApplicationContext();
        registry = Registry.getInstance();
        title_sound = 0;
        soundfile = this.getIntent().getStringExtra("SOUND");	
        //logoutWarning = new LogoutAlarmReceiver();
        id = registry.getAlarmID();
    }

    public void playSound(String filename) {
    	stopPlaying();
		mp = new MediaPlayer();
		 
		//in this case my sound file is in the application directory...       
		String audioFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Data/AppAudio/"+filename;
		       
		try {
		    File file = new File(audioFilePath);
		    FileInputStream fis = new FileInputStream(file);
		    mp.setDataSource(fis.getFD());
		    mp.prepare();
		    mp.start();    
        } catch(FileNotFoundException e){
			Log.d("ImageAdapterWithButton", "the exception 1" + e.toString());
		} catch (IllegalArgumentException e) {
			Log.d("ImageAdapterWithButton", "the exception 2" + e.toString());
		} catch (IllegalStateException e) {
			Log.d("ImageAdapterWithButton", "the exception 3" + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("ImageAdapterWithButton", "the exception 4" + e.toString());
		}
    }
    
   	public void showToast() {
		View toastView = getLayoutInflater().inflate(R.layout.toast_emotion, 
				(ViewGroup)findViewById(R.id.toastLayout));
	
		ImageView imgView = (ImageView)toastView.findViewById(R.id.emotion_selected);
		imgView.setImageResource(R.drawable.check);
		
		TextView txtView = (TextView)toastView.findViewById(R.id.done_text);
		txtView.setText(getResources().getString(R.string.done));
		
		Toast toast = new Toast(this);
     	toast.setGravity(Gravity.CENTER, 0, 0);
     	toast.setDuration(Toast.LENGTH_SHORT);
     	toast.setView(toastView);
     	toast.show();
	}
   	
	public void showMessageToast(String message, int img) {
		View toastView = getLayoutInflater().inflate(R.layout.toast_emotion, 
				(ViewGroup)findViewById(R.id.toastLayout));
	
		ImageView imgView = (ImageView)toastView.findViewById(R.id.emotion_selected);
		imgView.setImageResource(img);
		
		TextView txtView = (TextView)toastView.findViewById(R.id.done_text);
		txtView.setText(message);
		
		Toast toast = new Toast(this);
     	toast.setGravity(Gravity.CENTER, 0, 0);
     	toast.setDuration(Toast.LENGTH_SHORT);
     	toast.setView(toastView);
     	toast.show();
	}
    
    @Override
    public void onUserInteraction(){
    	CancelAlarm();
	    SetAlarm();
    	Log.d("BaseActivity", "onUserInteraction");
    }

	@Override
	protected void onResume() {
    	super.onResume();
    	Log.d("BaseActivity", "onResume");
    	//checkReminder();
    }
	
	@Override
	protected void onPause() {
    	super.onPause();
    	//stopPlaying();
    	Log.d("BaseActivity", "onPause");
    }
	
	@Override
	protected void onStop() {
    	super.onStop();
    	stopPlaying();
    	Log.d("BaseActivity", "onStop");
    }
	
	@Override
	protected void onDestroy() {
    	super.onDestroy();
    	//logoutWarning.CancelAlarm(context, id);
    	Log.d("BaseActivity", "onDestroy");
    }
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }
	
	@Override
	public void onBackPressed() {
	     super.onBackPressed(); 
	     CancelAlarm();
	     SetAlarm();
	     registry.getLog().logFinalClick("back_key", 1);	
	}

	public void SetAlarm() {
		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 1200);
		Intent logoutIntent = new Intent(context, LoginActivity.class);
    	logoutIntent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
    	logoutIntent.putExtra("startDialog", true);
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(LoginActivity.class);
		stackBuilder.addNextIntent(logoutIntent);
		
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(56789, PendingIntent.FLAG_CANCEL_CURRENT);      
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
	}
	
	public void CancelAlarm() {
		 Intent intent = new Intent(context, LoginActivity.class);
	     PendingIntent sender = PendingIntent.getBroadcast(context, 56789, intent, 0);
	     AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	     alarmManager.cancel(sender);
	}
	
	public void resetReminders() {
		
    	ReminderTimes reminderTimes = new ReminderTimes(); 

     	Calendar cal = Calendar.getInstance();
     	cal.setTimeInMillis(reminderTimes.getLimitMillis(0));
     	cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)-2);
     	cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
     	cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
     	
     	Calendar cal2 = Calendar.getInstance();
     	cal2.setTimeInMillis(reminderTimes.getLimitMillis(2));
     	Log.e("BaseActivity", "second reminder reset time of the day" + (cal2.get(Calendar.HOUR_OF_DAY)));
     	cal2.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY)-2);
     	cal2.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
     	cal2.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
     	
        ReminderAlarmReceiver reminderAlarm = new ReminderAlarmReceiver();
        long current_time = Calendar.getInstance().getTimeInMillis();

        Log.e("BaseActivity", "first reminder reset time of the day" + (cal.get(Calendar.HOUR_OF_DAY)));
        Log.e("BaseActivity", "second reminder reset time of the day" + (cal2.get(Calendar.HOUR_OF_DAY)));
        
        if (current_time >= cal.getTimeInMillis() && current_time < reminderTimes.getLimitMillis(0)) {
        	if (!registry.getMorningReset()) {
	        	reminderAlarm.cancelAlarm(context, 0);
	        	reminderAlarm.setAlarm(context, reminderTimes.getNextReminderMillis(0), 0);
				int signalID = registry.getLog().logNewReminder("reminder_type", 1);
				registry.setMorningReset(true);
				registry.setEveningReset(false);
				registry.setEODReset(false);
				registry.setFirstAlarm(true);
				registry.setSecondAlarm(false);
				registry.setThirdAlarm(false);
				registry.getLog().completeReminderLog(-2, signalID);
			}
			Log.e("BaseActivity", "alarm has been reset first reminder");
		}
        else {
            if (current_time >= cal2.getTimeInMillis() && current_time < reminderTimes.getLimitMillis(2)) {
                if (!registry.getEveningReset()) {
                    reminderAlarm.cancelAlarm(context, 1);
                    reminderAlarm.setAlarm(context, reminderTimes.getNextReminderMillis(2), 1);
                    int signalID = registry.getLog().logNewReminder("reminder_type", 2);
                    registry.setMorningReset(false);
                    registry.setEveningReset(true);
                    registry.setEODReset(false);
                    registry.setFirstAlarm(false);
                    registry.setSecondAlarm(true);
                    registry.setThirdAlarm(false);
                    registry.getLog().completeReminderLog(-2, signalID);
                }
                Log.e("BaseActivity", "alarm has been reset second reminder");
            }
        }
		
		Log.d("BaseActivity", "alarm has been reset");
	}
	
   @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            windowCloseHandler.postDelayed(windowCloserRunnable, 250);
        }
    }



    private void toggleRecents() {
        Intent closeRecents = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
        closeRecents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        ComponentName recents = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
        closeRecents.setComponent(recents);
        this.startActivity(closeRecents);
    }

    private Handler windowCloseHandler = new Handler();
    private Runnable windowCloserRunnable = new Runnable() {
        @Override
		public void run() {
            ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

            if (cn != null && cn.getClassName().equals("com.android.systemui.recent.RecentsActivity")) {
                toggleRecents();
            }
        }
    };
}
