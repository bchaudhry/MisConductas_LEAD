package com.phitlab.lowliteracymexicanw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.phitlab.lowliteracymexicanw.gcm.GcmBroadcastReceiver;
import com.phitlab.lowliteracymexicanw.logDB.LogDBAdapter;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.sync.DataUploadService;
import com.phitlab.lowliteracymexicanw.sync.DatabaseService;
import com.phitlab.lowliteracymexicanw.sync.LoginDetailSync;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyBehaviors extends Application {
    public static final String PROPERTY_REG_ID = "registration_id";
    static final String DISPLAY_MESSAGE_ACTION = "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String TAG = "misConductas";
    public static final String PROJECT_NUMBER = "1010732824006";    
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L * 4L; // 4 Weeks
    private final GcmBroadcastReceiver gcmReceiver = new GcmBroadcastReceiver();
	private Registry registry;
	GoogleCloudMessaging gcm;
    Context context;
    String regId;

    public MyBehaviors() {
    	registry = Registry.getInstance();
        Log.i("main", "Constructor fired");
    }

    @Override
    public void onCreate() {
        super.onCreate();   
        registry.setUserName("LEAD158");
        initDatabase(this);
        setDatabaseSyncAlarm(this);
        setReminderAlerts();
        //registerDevice();
        context = getApplicationContext();

        if(checkPlayServices()){
        	registerReceiver(gcmReceiver,
                     new IntentFilter(DISPLAY_MESSAGE_ACTION));
			gcm = GoogleCloudMessaging.getInstance(this);
			regId = getRegistrationId(context);
			//mDisplay.setText(regid);
			if(regId.isEmpty()){
				new RegisterBackground().execute();
			}
        }
        Log.i("main", "onCreate fired"); 
    }
       
    private void setReminderAlerts() {
		registry.setFirstAlarm(false);
		registry.setSecondAlarm(false);
		registry.setThirdAlarm(false);
		registry.setMorningReset(false);
		registry.setEveningReset(false);
        registry.setFirstSignal(false);
        registry.setSecondSignal(false);
        registry.setThirdSignal(false);
		registry.setEODReset(false);
		registry.setReminder(false);
	}

	public void initDatabase(Context context) {
    	registry.setLog(new LogDBAdapter(this));  	
    	Intent intent = new Intent(this, DatabaseService.class);
    	startService(intent);
    	Log.d("main menu", "initializeDB");
    }
    
    void setDatabaseSyncAlarm(Context ctx) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeZone(TimeZone.getDefault());
    	cal.set(Calendar.HOUR_OF_DAY, 9);
    	cal.set(Calendar.AM_PM, Calendar.AM);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.add(Calendar.DAY_OF_MONTH, 1);
        
    	Log.d("mainmenu","databasesynch");
    	PendingIntent pi = PendingIntent.getService(ctx, 56800, new Intent(ctx, DataUploadService.class),
    												PendingIntent.FLAG_UPDATE_CURRENT);
    	AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    	am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    	Log.d("mainmenu","databasesynch2");    	
    }
    
	class RegisterBackground extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String msg = "";
			try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regId = gcm.register(PROJECT_NUMBER);
                msg = "Device registered, registration ID=" + regId;
                Log.d("111", msg);
                sendRegistrationIdToBackend();
                // Persist the regID - no need to register again.
                storeRegistrationId(context, regId);
                
              
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }
		
		@Override
        protected void onPostExecute(String msg) {
            //mDisplay.append(msg + "\n");    
			System.out.println(msg);
			
        }
		
		private void sendRegistrationIdToBackend() {
			Log.e("MyBehavior", "Send registration id to server");
			String url = "https://phitlab.soic.indiana.edu/llmw_folder/get_device.php";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", registry.getUserName()));
            params.add(new BasicNameValuePair("regId", regId));
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(url);
            try {
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            try {
				HttpResponse httpResponse = httpClient.execute(httpPost);
				System.out.println("response to sending registration id" + httpResponse.toString());
			} catch (ClientProtocolException e) {
				System.out.println("error sending regid");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("error sending regid");
				e.printStackTrace();
			}     			
		}
		
		private void storeRegistrationId(Context context, String regId) {
		    final SharedPreferences prefs = getGCMPreferences(context);
		    int appVersion = getAppVersion(context);
		    Log.i(TAG, "Saving regId on app version " + appVersion);
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putString(PROPERTY_REG_ID, regId);
		    editor.putInt(PROPERTY_APP_VERSION, appVersion);
		    editor.commit();
		}
	}
		
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	          //  GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	          //          PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            //finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
	    
	    return getSharedPreferences(MainActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
}

