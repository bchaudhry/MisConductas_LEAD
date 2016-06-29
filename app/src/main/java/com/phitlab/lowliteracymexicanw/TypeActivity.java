package com.phitlab.lowliteracymexicanw;

import java.util.ArrayList;
import com.phitlab.lowliteracymexicanw.cameraapp.CameraActivity;
import com.phitlab.lowliteracymexicanw.objects.GridObject;
import com.phitlab.lowliteracymexicanw.objects.ImageAdapterWithButtons;
import com.phitlab.lowliteracymexicanw.sync.ScreenDBController;
import com.phitlab.lowliteracymexicanw.voicerecord.VoiceRecordActivity;
import com.phitlab.lowliteracymexicanw.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TypeActivity extends BaseActivity {

	//private final static String domainName = "http://156.56.95.6/www/";
	//private View prevView, currentView;
	private int behaviorID =-1;
	private int pos;
	private boolean camera = false;
	private GridView gridview;
	private String activity;
//	protected int title_sound=0;
	protected View currentView;
	protected View prevView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type);

		behaviorID = this.getIntent().getIntExtra("BEHAVIOR", -1);
		activity = this.getIntent().getStringExtra("ACTIVITY");
		soundfile = this.getIntent().getStringExtra("SOUND");
//		title_sound   = 0;
		
		String title = this.getIntent().getStringExtra("TITLE");
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setText(title);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.typescreen);
		layout.setBackgroundColor(Color.GRAY);	

		currentView = prevView = null;
		//Create gridview to display images
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setBackgroundColor(Color.GRAY);
		ArrayList<String> imgURLs = getImageURLsFor(behaviorID);
		ArrayList<String> arrLabels = getLabels(behaviorID);
		ArrayList<String> arrSounds = getSounds(behaviorID);
		GridObject gobj;
		ArrayList<GridObject> gObjList = new ArrayList<GridObject>();
		for(int i=0; i<arrLabels.size(); i++) {
			gobj = new GridObject(imgURLs.get(i),arrLabels.get(i),arrSounds.get(i));
			gObjList.add(gobj);
		}	

		gridview.setAdapter(new ImageAdapterWithButtons(TypeActivity.this,gObjList, 1));
		//gridview.setAdapter(new ImageAdapter(this,getImageURLsFor(behaviorID), getLabels(behaviorID)));
		//gridview.setAdapter(new ImageAdapter(this,getImageURLsFor(behaviorID)));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//Toast.makeText(TypeActivity.this, "" + position, Toast.LENGTH_SHORT).show();
				currentView=v;
				pos = position;
				
				if(prevView!=null)
				{	
					prevView.setBackgroundColor(Color.WHITE);
				}	
				currentView.setBackgroundColor(Color.rgb(230, 140, 0));
				prevView=currentView;
				if (!activity.equals("alcohol") && position == gridview.getCount()-1) {
						camera = true;
						registry.getLog().logChoice("product_chosen", 0);
				}
				else {
					camera = false;
					registry.getLog().logFinalClick("icon"+(position+1), 1);
					registry.getLog().logChoice("product_chosen", (position+1));
				}
				
				nextButtonClick(v);
			}
		});
		
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);
				registry.getLog().logClick(title_sound ++, "title_sound");
			}
			
		});
		registry.getLog().logNewClick(2);
		//playSound(soundfile);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("onResume", "Back in resume");
//		new Thread(new Runnable() {
//			public void run() {
//				 myHandler.post(updateRunnable);
//			}        
//        }).start();
		
	}
	
	/*
	 * For the behavior selected, get the corresponding image urls from database
	 * */
	public ArrayList<String> getImageURLsFor(int behaviorID) {
		ScreenDBController dbController = ScreenDBController.getInstance(this);
		dbController.open();
		
		ArrayList<String> al = new ArrayList<String>();
		al.clear();

		switch(behaviorID) {
		
		case R.drawable.home_diet:
			al = dbController.getImageURLs("Diet");
			break;
		case R.drawable.home_exercise:
			al = dbController.getImageURLs("Time");
			break;
		case R.drawable.home_pills:
			al = dbController.getImageURLs("Pills");
			break;
		case R.drawable.home_powders:
			al = dbController.getImageURLs("Powders");
			break;
		case R.drawable.home_teas:
			al = dbController.getImageURLs("Teas");
			break;
		case R.drawable.home_drink:
			al = dbController.getImageURLs("Drinks");
			break;
		case R.drawable.home_drops:
			al = dbController.getImageURLs("Drops");
			break;
		case R.drawable.home_alcohol:
			al = dbController.getImageURLs("Alcohol");
			break;

		}
			
		dbController.close();
		return al;
	}	
	
	protected ArrayList<String> getLabels(int behaviorID) {
		ScreenDBController dbController = ScreenDBController.getInstance(this);
		dbController.open();

		ArrayList<String> labels = new ArrayList<String>();
		labels.clear();
		
		switch(behaviorID) {
		
		case R.drawable.home_diet:
			labels = dbController.getLabels("DietLabels");
			break;
		case R.drawable.home_exercise:
			labels = dbController.getLabels("TimeLabels");
			break;
		case R.drawable.home_pills:
			labels = dbController.getLabels("PillLabels");
			break;
		case R.drawable.home_powders:
			labels = dbController.getLabels("PowderLabels");
			break;
		case R.drawable.home_teas:
			labels = dbController.getLabels("TeaLabels");
			break;
		case R.drawable.home_drink:
			labels = dbController.getLabels("DrinkLabels");
			break;
		case R.drawable.home_drops:
			labels = dbController.getLabels("DropLabels");
			break;
		case R.drawable.home_alcohol:
			labels = dbController.getLabels("AlcoholLabels");
			break;

		}

		dbController.close();
		return labels;
	} 	
	
	protected ArrayList<String> getSounds(int behaviorID) {
		ScreenDBController dbController = ScreenDBController.getInstance(this);
		dbController.open();

		ArrayList<String> labels = new ArrayList<String>();
		labels.clear();
		
		switch(behaviorID) {
		
		case R.drawable.home_diet:
			labels = dbController.getLabels("DietSounds");
			break;
		case R.drawable.home_exercise:
			labels = dbController.getLabels("TimeSounds");
			break;
		case R.drawable.home_pills:
			labels = dbController.getLabels("PillSounds");
			break;
		case R.drawable.home_powders:
			labels = dbController.getLabels("PowderSounds");
			break;
		case R.drawable.home_teas:
			labels = dbController.getLabels("TeaSounds");
			break;
		case R.drawable.home_drink:
			labels = dbController.getLabels("DrinkSounds");
			break;
		case R.drawable.home_drops:
			labels = dbController.getLabels("DropSounds");
			break;
		case R.drawable.home_alcohol:
			labels = dbController.getLabels("AlcoholSounds");
			break;

		}

		dbController.close();
		return labels;
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_type, menu);
		return true;
	}

	/*
	 * Depending on behavior screen user is on next screen displayed
	 * */
	public void nextButtonClick(View view) {
		Intent intent = null;

		if (camera) {
			if (activity.equals("diet")) {
				intent = new Intent(this,VoiceRecordActivity.class);
				intent.putExtra("TITLE", getResources().getString(R.string.voice_title));
				intent.putExtra("BEHAVIOR", behaviorID);
				intent.putExtra("ACTIVITY", activity);	
				intent.putExtra("SOUND", "voice_record.ogg");
				registry.getLog().logFinalClick("record_icon", 1);
				startActivity(intent);
			}
			else {
				intent = new Intent(this,CameraActivity.class);
				intent.putExtra("TITLE", getBehavior());
				intent.putExtra("BEHAVIOR", behaviorID);
				intent.putExtra("ACTIVITY", activity);	
				intent.putExtra("SOUND", getSound());
				registry.getLog().logFinalClick("camera_icon", 1);
				startActivity(intent);
			}
		}
		else {
			if(activity.equals("alcohol")) {
				intent = new Intent(this, AlcoholCountActivity.class);
		    	intent.putExtra("TITLE", getResources().getString(R.string.number_alcohol));
		    	intent.putExtra("ACTIVITY", activity);	
		    	intent.putExtra("BEHAVIOR", pos);
		    	intent.putExtra("SOUND", "alcohol_amt.ogg");
		    	Log.d("alcohol", "alcoholType = " + pos);
		        startActivity(intent);
			}
			else if (activity.equals("drinks") && pos == 0) {
				intent = new Intent(this,VoiceRecordActivity.class);
				intent.putExtra("TITLE", getResources().getString(R.string.record_shake));
				intent.putExtra("BEHAVIOR", pos);
				intent.putExtra("ACTIVITY", activity);	
				intent.putExtra("SOUND", "record_shake.ogg");
				registry.getLog().logFinalClick("record_icon", 1);
				startActivity(intent);
			}
//			else if (activity.equals("diet")) {
//				final Dialog dig = new Dialog(TypeActivity.this);
//				dig.setContentView(R.layout.dialog_behavior);
//				runOnUiThread(new Runnable() {
//				    public void run() {
//				    	TextView reminderQuestion = (TextView) dig.findViewById(R.id.reminder_question);
//				    	reminderQuestion.setText(R.string.eod_reminder_question);
//				    }
//				});
//				
//				dig.setTitle(getResources().getString(R.string.menu_settings));
//				dig.setCancelable(false);
//				registry.getLog().logNewClick(12);
//				registry.getLog().logNewReminder("reminder_type", 3);
//				
//				ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
//				titleSound.setOnClickListener(new OnClickListener() {
//
//					public void onClick(View v) {
//						playSound("eod_q.ogg");	
//						registry.getLog().logClick(title_sound++, "title_sound");
//					}
//					
//				});
//				
//				ImageView btnCheckButton = (ImageView) dig.findViewById(R.id.ok);
//				btnCheckButton.setOnClickListener(new OnClickListener() {
//
//					public void onClick(View v) {
//						registry.getLog().logFinalClick("yes_icon", 1);
//						registry.getLog().completeReminderLog(1);
//						Intent intent = new Intent(TypeActivity.this, BehaviorActivity.class);
//		    	    	intent.putExtra("TITLE", getResources().getString(R.string.behavior_title));
//		    			startActivity(intent);
//		    			dig.dismiss();
//					}
//				});
//		  
//				ImageView btnCrossButton = (ImageView) dig.findViewById(R.id.remove);
//				btnCrossButton.setOnClickListener(new OnClickListener() {
//
//					public void onClick(View v) {
//						registry.getLog().logFinalClick("no_icon", 1);
//						registry.getLog().completeReminderLog(-1);
//						showToast();
//						//playSound("thanks.ogg");
//						Intent intent = new Intent(TypeActivity.this, MainActivity.class);
//						intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
//						intent.putExtra("SOUND", "thanks.ogg");
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);
//						dig.dismiss();
//						TypeActivity.this.finish();
//					}
//				});
//				dig.show();
				//playSound("morebehaviors.ogg");
//			}
			else {
				showToast();
				//playSound("thanks.ogg");
				intent = new Intent(this, MainActivity.class);
				intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
				intent.putExtra("SOUND", "thanks.ogg");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				resetReminders();
				startActivity(intent);
				finish();
			}
			currentView = prevView = null;
		}
	}
	
	private String getSound() {
		String mySound = "";
			
			if(activity.equals("pills")) 
				mySound = "pills_photo.ogg";
			else  if (activity.equals("drops"))
				mySound = "drops_photo.ogg";
			else if (activity.equals("powders"))
				mySound = "powders_photo.ogg";
			else if (activity.equals("teas"))
				mySound = "teas_photo.ogg";
			else if (activity.equals("drinks"))
				mySound = "drinks_photo.ogg";
			return mySound;
	}

	private String getBehavior() {
		
		String myactivity = "";
		
		if(activity.equals("pills")) 
			myactivity = getResources().getString(R.string.pills_photo);
		else  if (activity.equals("drops"))
			myactivity = getResources().getString(R.string.drops_photo);
		else if (activity.equals("powders"))
			myactivity = getResources().getString(R.string.powders_photo);
		else if (activity.equals("teas"))
			myactivity = getResources().getString(R.string.teas_photo);
		else if (activity.equals("drinks"))
			myactivity = getResources().getString(R.string.drinks_photo);
		return myactivity;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Log.d("Back", "Back button clicked");
           // moveTaskToBack(true);
           // return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
