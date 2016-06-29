package com.phitlab.lowliteracymexicanw.gcm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.SSLException;
import com.phitlab.lowliteracymexicanw.R;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ReceiveActivity extends Activity implements OnClickListener {
	  
	private DownloadManager dm;
	protected String TAG = "ReceiveActivity";
	private long enqueue;
	BroadcastReceiver onComplete;
	private String message;
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_notification);
		Intent intent = getIntent();
		
		ImageView notification_icon = (ImageView) findViewById(R.id.img_view); 
		TextView notification_title = (TextView) findViewById(R.id.notify_title);
		TextView notification_message = (TextView) findViewById(R.id.notify_message); 
		
		title = intent.getExtras().getString("title");
		message = intent.getExtras().getString("notification").trim();
		int icon = intent.getExtras().getInt("ICON");
		notification_title.setText(title);
		notification_message.setText(message);	
		notification_icon.setBackgroundResource(icon);
		
		((ImageView) findViewById(R.id.btnOkay)).setOnClickListener (this);
	
		//BroadcastReceiver receiver;
		if(title.contentEquals("Downloading ...")) {
			dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
			
			Request request = new Request(Uri.parse(message));			
			request.setDescription("Description for the DownloadManager Bar");
            request.setTitle("MisConductas.apk");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            // save the file in the "Downloads" folder of SDCARD
            request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory() + "/download/", "MisConductas.apk");
            
            enqueue = dm.enqueue(request);
			onComplete=new BroadcastReceiver() {
				@Override
				public void onReceive(Context ctxt, Intent intent) {
					String action = intent.getAction();
				    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
				    	install("MisConductas.apk");
				    	Log.i(TAG , "download complete ");
				    	if(!ReceiveActivity.this.isDestroyed())
				    		finish();
				    }
				}
			}; 
		    registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		}	
	}

	protected void install(String fileName) {
        Intent install= new Intent(android.content.Intent.ACTION_VIEW);	
        File downloadDir = new File(Environment.getExternalStorageDirectory() + "/download/", fileName);	
        install.setDataAndType(Uri.fromFile(downloadDir), "application/vnd.android.package-archive");
	    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(install);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_receive_notification, menu);
		return true;
	}

	@Override
	protected void onDestroy()
	{
		if(title.contentEquals("Downloading ...")) {
			unregisterReceiver(onComplete);
		}
	    super.onDestroy();
	}
	@Override
	public void onClick(View arg0) {
		finish();	
	}

}

