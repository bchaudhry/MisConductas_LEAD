package com.phitlab.lowliteracymexicanw.objects;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class HeartBeat extends Service {
 
    @Override
    public void onStart(Intent i, int startId) {
 
        this.beat.run();
        this.stopSelf();
    }
 
    public Runnable beat = new Runnable() {
 
        @Override
		public void run() {
              
        }
    };

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}