
package com.phitlab.lowliteracymexicanw;

import java.util.ArrayList;

import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.GridObject;
import com.phitlab.lowliteracymexicanw.objects.ImageAdapter;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CountActivity extends BaseActivity {

	View currentView;
	private String activity;
	private GridView gridview;
	private ImageAdapter adapter;
	protected int[] icon_click;
	String title;
	protected int screen_type;
	protected int pos;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter);
		
		activity = this.getIntent().getStringExtra("ACTIVITY");
		title = this.getIntent().getStringExtra("TITLE");
		soundfile = this.getIntent().getStringExtra("SOUND");

		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setText(title);
		icon_click = new int[getGridObjs().size()];
		
		for(int i=0; i<getGridObjs().size();i++)
			icon_click[i]=0;
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.typescreen);
		layout.setBackgroundColor(Color.GRAY);
		
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setBackgroundColor(Color.GRAY);
		adapter = new ImageAdapter(this,getGridObjs());
		adapter.notifyDataSetChanged();
		gridview.setAdapter(adapter);   
		gridview.invalidateViews();
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				pos = position;
				registry.getLog().logClick(adapter.getClicks(pos), "icon"+(pos+1));
			}
		});

		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);	
				registry.getLog().logClick(title_sound, "title_sound");
			}
			
		});

		ImageButton nextButton = (ImageButton) findViewById(R.id.btnNext);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (adapter.getTotal() > 0) {
					
					Intent intent = null;
					if(activity.equals("binge"))
						registry.getLog().logChoice("behavior_duration", adapter.getTotalString());
					else if(activity.equals("exercise"))
						registry.getLog().logChoice("behavior_duration", adapter.getTotalString());
					else {
						registry.getLog().logChoice("cigarette_count", adapter.getTotalString());
//						registry.getLog().logChoice("partial_cigarette", adapter.getTotal(0));
//						registry.getLog().logChoice("full_cigarette", adapter.getTotal(1));
					}
					if(activity.equals("binge")) {
						intent = new Intent(CountActivity.this,BingeLOCActivity.class);
						intent.putExtra("ACTIVITY", activity);
						intent.putExtra("SOUND", "photoeae.ogg");
				    	intent.putExtra("TITLE", getResources().getString(R.string.binge_loc_title));
					}
					else {
						intent = new Intent(CountActivity.this, MainActivity.class);
						intent.putExtra("ACTIVITY", activity);
						intent.putExtra("TITLE", title);
						intent.putExtra("SOUND", "thanks.ogg");
						showToast();
						resetReminders();
						//playSound("thanks.ogg");
					}
					registry.getLog().logFinalClick("next_icon", 1);
					
					
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				else {
					if(activity.equals("binge"))
						showMessageToast(getResources().getString(R.string.time_length), R.drawable.question_mark);
					else if(activity.equals("exercise"))
						showMessageToast(getResources().getString(R.string.time_length_exercise), R.drawable.question_mark);
					else {
						showMessageToast(getResources().getString(R.string.number_cigarette), R.drawable.question_mark);
						playSound(soundfile);	
					}
				}
			}
		});
		registry.getLog().logNewClick(3);	
		//playSound(soundfile);	
	}

	protected ArrayList<GridObject> getGridObjs() {

		Integer[] arr;
		String[]  arrNumber;
		String[] arrSound;
		if(activity.equals("cigarette")) {
			arr = new Integer[2];
			arrNumber = new String[2];
			arrSound = new String[2];
			arr[0] = R.drawable.partial_cigarette;
			arr[1] = R.drawable.full_cigarette;
			arrNumber[0] = "Cigarro Parcial";
			arrNumber[1] = "Cigarro Completo";
			arrSound[0] = "cigarettes_cigarro_parcial.ogg";
			arrSound[1] = "cigarettes_cigarro_completo.ogg";		
			screen_type = 4;
		}
		else {
			arr = new Integer[8];
			arrNumber = arrSound = new String[8];
			arr = new Integer[] {R.drawable.time1, R.drawable.time2, R.drawable.time3, R.drawable.time4, 
					R.drawable.time5, R.drawable.time6, R.drawable.time7, R.drawable.time8};
			arrNumber = new String[] {"Menos de 10 min", "15 min", "Menos de 30 min", "30 min",
				"Menos de 45 min", "45 min", "Menos de una hora","Una hora"};
			arrSound = new String[] {"time_LT15min.ogg", "time_15min.ogg", "time_LT30min.ogg", "time_30min.ogg",
					"time_LT45min.ogg", "time_45min.ogg", "time_LT60min.ogg", "time_60min.ogg"};
			screen_type = 3;
		}
		
		ArrayList<GridObject> gObjList = new ArrayList<GridObject>();
		for(int i=0; i<arr.length; i++) {
			Log.d("getGridObjs", "arr[i] "+arr[i] + " i is " + i);
			GridObject gobj = new GridObject(arr[i],arrNumber[i], arrSound[i]);
			gObjList.add(gobj);
			gobj = null; 
		}	
		
	return gObjList;
	} 
}


