package com.phitlab.lowliteracymexicanw;

import java.util.ArrayList;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.GridObject;
import com.phitlab.lowliteracymexicanw.objects.ImageAdapterWithButtons;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class TimeActivity extends BaseActivity {

	View currentView;
	Integer[] arr = {R.drawable.time1, R.drawable.time2, R.drawable.time4, 
			R.drawable.time6, R.drawable.time8,
			R.drawable.time10, R.drawable.time12, 
			R.drawable.time14, R.drawable.time16, R.drawable.time19};
	String[] arrTime = {"menos de 10 min", "15 min", "30 min",
			"45 min", "una hora", "una hora y 15 min", "una hora y 30 min",
			"una hora y 45 min", "dos horas", "más de dos horas"};                                                                                                                                                                     
	String[] arrSound = new String[] {"time_LT15min.ogg", "time_15min.ogg", "time_30min.ogg",
			"time_45min.ogg", "time_60min.ogg",  "time_1hr15min.ogg",  
			"time_1hr30min.ogg","time_1hr45min.ogg", "time_2hrs.ogg", "time_GT2hrs.ogg"};
	private String activity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		
		String title = this.getIntent().getStringExtra("TITLE");
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setText(title);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.typescreen);
		layout.setBackgroundColor(Color.GRAY);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setBackgroundColor(Color.GRAY);
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapterWithButtons(this,getGridObjs()));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				currentView=v;	
				currentView.setBackgroundColor(Color.rgb(230, 140, 0));
				registry.getLog().logFinalClick("icon"+(position+1), 1);
				registry.getLog().logChoice("behavior_duration", (position+1));
				onClick();
			}
		});
		
		activity = this.getIntent().getStringExtra("ACTIVITY");
		soundfile = this.getIntent().getStringExtra("SOUND");
//		
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);	
				registry.getLog().logClick(1, "title_sound");
			}
			
		});
		registry.getLog().logNewClick(3);	
		//playSound(soundfile);
	}

	protected ArrayList<GridObject> getGridObjs() {

		ArrayList<GridObject> gObjList = new ArrayList<GridObject>();

		for(int i=0; i<arr.length; i++) {
			GridObject gobj = new GridObject(arr[i],arrTime[i],arrSound[i]);
			gObjList.add(gobj);
			gobj = null; 
		}	
		
	return gObjList;
} 
	
	public void onClick() {
		Log.d("activity string", "activity string " + activity);
		if (currentView != null) {
			if (activity.equals("binge")) {		
				Intent intent = new Intent(this,BingeLOCActivity.class);
				intent.putExtra("ACTIVITY", activity);
				intent.putExtra("SOUND", "photoeae.ogg");
		    	intent.putExtra("TITLE", getResources().getString(R.string.binge_loc_title));
				startActivity(intent);
			}
			else if (activity.equals("exercise")) {
				showToast();
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				resetReminders();
				intent.putExtra("SOUND", "thanks.ogg");
				startActivity(intent);
				finish();
			}
		}
		else {
			  playSound("time_request.ogg");
			  Toast.makeText(this, getResources().getString(R.string.time_request), Toast.LENGTH_SHORT).show();		
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_binge, menu);
		return true;
	}

}
