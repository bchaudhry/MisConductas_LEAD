package com.phitlab.lowliteracymexicanw;

import java.util.ArrayList;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.GridObject;
import com.phitlab.lowliteracymexicanw.objects.ImageAdapterWithButtons;
import com.phitlab.lowliteracymexicanw.objects.Registry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;
/* 
 * This activity represents the behavior activity
 * Displays all the various behaviors user can select from
 * */

public class BehaviorActivity extends BaseActivity {

	private View prevView, currentView;
	int behaviorSelected = -1;
	private GridView gridview;
	private Registry registry;
	protected int title_sound=0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_behavior);

		String screenTitle = this.getIntent().getStringExtra("TITLE");		
		TextView titleTextView = (TextView) findViewById(R.id.titleBehaviorTextView);
		titleTextView.setText(screenTitle);

		registry = Registry.getInstance();
		currentView = prevView = null;
		LinearLayout layout = (LinearLayout) findViewById(R.id.behaviorscreen);
		layout.setBackgroundColor(Color.GRAY);

		gridview = (GridView) findViewById(R.id.gridviewBehavior);
		gridview.setBackgroundColor(Color.GRAY);
		gridview.setAdapter(new ImageAdapterWithButtons(this,getGridAL()));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {				
				behaviorSelected = v.getId();
				currentView=v;

				if(prevView!=null)
				{	
					prevView.setBackgroundColor(Color.WHITE);
				}	
				currentView.setBackgroundColor(Color.rgb(240, 130, 0));
				prevView=currentView;
				nextButtonClick(currentView);
				registry.getLog().logNewChoice("behavior_id", (position+1));
				registry.getLog().logFinalClick("icon"+(position+1), 1);
			}
		});
		
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound("behavior.ogg");
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});
		registry.getLog().logNewClick(1);
		//playSound("behavior.ogg");
	}



	/*
	 * Returns the grid objects containing behavior image id and name to be displayed on label*/
	
	ArrayList<GridObject> getGridAL() {

		ArrayList<GridObject> al = new ArrayList<GridObject>();

		Integer[] behaviorThumbs = new Integer[]{R.drawable.home_diet, R.drawable.home_exercise, R.drawable.home_binge, R.drawable.home_pills, R.drawable.home_powders, 
												 R.drawable.home_teas, R.drawable.home_drink, R.drawable.home_drops, R.drawable.home_vomit,
												 R.drawable.home_smoking, R.drawable.home_alcohol};	
		String[] behNames = {"Hacer Dieta", "Hacer Ejercicio", "Comer y comer", "Pastillas para Adelgazar", "Polvos de Dieta", "Té de Dieta", "Bebidas de Dieta", 
							"Gotas de Dieta", "Vomitar","Cigarros", "Alcohol"};
		String[] mediaFiles = {"diet.ogg","exercise.ogg", "eae.ogg", "pills.ogg", "powders.ogg", "teas.ogg", "drinks.ogg", 
							"drops.ogg", "vomit.ogg","cigarette.ogg", "alcohol.ogg"};


		for(int i=0; i<behaviorThumbs.length; i++) {
			GridObject gobj = new GridObject(behaviorThumbs[i],behNames[i],mediaFiles[i]);
			al.add(gobj);
			gobj = null; 
		}	

		return al;
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_behavior, menu);
		return true;
	}

	
	
/*
 * Depending on the behavior selected appropriate images shown on next screen
 * */
	public void nextButtonClick(View view) {
		Log.d("behaviorselected :",""+behaviorSelected);	

		Intent intent = null;
		String screenTitle = "";

		switch(behaviorSelected) {
			
		case R.drawable.home_vomit:
			showToast();
			screenTitle = getResources().getString(R.string.mainmenu_title);	
			intent = new Intent(this, MainActivity.class);
			resetReminders();
			intent.putExtra("SOUND", "thanks.ogg");
			intent.putExtra("TITLE", screenTitle);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;	
		case R.drawable.home_exercise:
			screenTitle = getResources().getString(R.string.time_length_exercise);	
			intent = new Intent(this,TimeActivity.class);
			intent.putExtra("ACTIVITY","exercise");
			intent.putExtra("SOUND", "exercise_time.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;
		case R.drawable.home_binge: 
			screenTitle = getResources().getString(R.string.time_length);	
			intent = new Intent(this, TimeActivity.class);
			intent.putExtra("ACTIVITY", "binge");
			intent.putExtra("SOUND", "binge_time.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;
		
		case R.drawable.home_smoking:
			screenTitle = getResources().getString(R.string.number_cigarette);	
			intent = new Intent(this,CountActivity.class);
			intent.putExtra("ACTIVITY", "cigarette");
			intent.putExtra("SOUND", "cigarette_num.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;

		case R.drawable.home_alcohol:
			screenTitle = getResources().getString(R.string.alcohol_question);
			intent = new Intent(this, TypeActivity.class);
			intent.putExtra("ACTIVITY", "alcohol");
			intent.putExtra("SOUND", "alcohol_type.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;
			
		case R.drawable.home_pills:
			screenTitle = getResources().getString(R.string.pills_title);
			intent = new Intent(this, TypeActivity.class);
			intent.putExtra("ACTIVITY", "pills");
			intent.putExtra("SOUND", "pills_type.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;
		
		case R.drawable.home_powders:
			screenTitle = getResources().getString(R.string.powders_title);
			intent = new Intent(this, TypeActivity.class);
			intent.putExtra("ACTIVITY", "powders");
			intent.putExtra("SOUND", "powders_type.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;
		
		case R.drawable.home_teas:
			screenTitle = getResources().getString(R.string.teas_title);
			intent = new Intent(this, TypeActivity.class);
			intent.putExtra("ACTIVITY", "teas");
			intent.putExtra("SOUND", "teas_type.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;

		case R.drawable.home_drink:
			screenTitle = getResources().getString(R.string.drinks_title);
			intent = new Intent(this, TypeActivity.class);
			intent.putExtra("ACTIVITY", "drinks");
			intent.putExtra("SOUND", "drinks_type.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;
			
		case R.drawable.home_drops:
			screenTitle = getResources().getString(R.string.drops_title);
			intent = new Intent(this, TypeActivity.class);
			intent.putExtra("ACTIVITY", "drops");
			intent.putExtra("SOUND", "drops_type.ogg");
			intent.putExtra("TITLE", screenTitle);
			break;	
			
		case R.drawable.home_diet:
			screenTitle = getResources().getString(R.string.diet_type);
			intent = new Intent(this, TypeActivity.class);
			intent.putExtra("TITLE", screenTitle);
			intent.putExtra("BEHAVIOR", R.drawable.home_diet);
			intent.putExtra("ACTIVITY", "diet");
			intent.putExtra("SOUND", "diet_type.ogg");
			break;	
		}

		if(intent!=null) {
			intent.putExtra("BEHAVIOR", behaviorSelected);
			startActivity(intent);
			currentView = prevView = null;
		}
	}

}
