package com.phitlab.lowliteracymexicanw;

import java.util.ArrayList;

import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.GridObject;
import com.phitlab.lowliteracymexicanw.objects.ImageAdapterWithButtons;
import com.phitlab.lowliteracymexicanw.objects.Registry;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.GridView;

public class CigaretteCountActivity extends BaseActivity {

	View currentView;
	ArrayList<GridObject> gObjList = new ArrayList<GridObject>();
	private String soundfile;
	//private String activity;
	private GridView gridview;
	protected Registry registry=Registry.getInstance();
	protected int minus=0;
	protected int plus=0;
	protected int title_sound=0;
	String title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cigarette_counter);

		//activity = this.getIntent().getStringExtra("ACTIVITY");
		title = this.getIntent().getStringExtra("TITLE");
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setText(title);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.typescreen);
		layout.setBackgroundColor(Color.GRAY);
		
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setBackgroundColor(Color.GRAY);
		gridview.setAdapter(new ImageAdapterWithButtons(this,gObjList));
//		 
//		soundfile = this.getIntent().getStringExtra("SOUND");
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);	
			//	registry.getLog().logAction(title_sound ++, "title_sound", "click_log");
			}
			
		});
		
		Button minusButton = (Button) findViewById(R.id.minus_button);
		minusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(gridview.getCount()>0) {
					gObjList.remove(gridview.getCount()-1);
					gridview.setAdapter(new ImageAdapterWithButtons(CigaretteCountActivity.this,gObjList));
					gridview.postInvalidate();
				//	registry.getLog().logAction(minus++, "minus_icon", "click_log");
				}
			}	
		});
		
		Button nextButton = (Button) findViewById(R.id.next_button);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(gridview.getCount()>0) {
					showToast();
					//playSound("thanks.ogg");
					//Intent intent = new Intent(CigaretteCountActivity.this, MainMenu.class);
					//intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
					Intent intent = new Intent(CigaretteCountActivity.this, MainActivity.class);
					intent.putExtra("SOUND", "thanks.ogg");
					intent.putExtra("TITLE", title);
					intent.putExtra("ACTIVITY", "cigarette");
					intent.putExtra("BEHAVIOR", "-1");
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					registry.getLog().logFinalClick("icon_"position, 1);
//					registry.getLog().logChoice("number_items", plus-minus);
					resetReminders();
					startActivity(intent);
					finish();
				}
				else {
					showMessageToast(getResources().getString(R.string.number_cigarette), R.drawable.question_mark);
					playSound(soundfile);	
				}
			}
			
		});
		
		Button plusButton = (Button) findViewById(R.id.plus_button);
		plusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GridObject gobj = new GridObject(R.drawable.one_cigarette,"");
				gObjList.add(gobj);
				gridview.setAdapter(new ImageAdapterWithButtons(CigaretteCountActivity.this,gObjList));
				gridview.postInvalidate();
				//registry.getLog().logAction(plus++, "plus_icon", "click_log");
			}
		});
//		
		registry.getLog().logNewClick(4);
	}

	protected void getGridObjs() {
		
		GridObject gobj = new GridObject(R.drawable.one_cigarette,"");
		gObjList.add(gobj);	
	} 
	
//	@Override
//	protected void onStop() {
//    	super.onStop();
//    	if(!new_activity)
//    		stopReminderTimer();
//    }
	
	@Override
	protected void onDestroy() {
    	super.onDestroy();
//    	if(!getNewActivity())
//    		logoutWarning();
    }
}
