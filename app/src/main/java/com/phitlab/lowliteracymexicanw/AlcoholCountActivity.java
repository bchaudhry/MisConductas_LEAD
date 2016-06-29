package com.phitlab.lowliteracymexicanw;

import java.util.ArrayList;
import com.phitlab.lowliteracymexicanw.objects.GridObject;
import com.phitlab.lowliteracymexicanw.objects.ImageAdapterWithButtons;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;


public class AlcoholCountActivity extends BaseActivity implements OnItemClickListener {

	View currentView;
	Integer[] arr = new Integer[4];
	String[]  arrNumber = new String[4];
	String[]  arrSound = new String[4];
	
//	private String soundfile;
	private String activity;
	private int alcoholType;
//	protected int title_sound=0;
	String title;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		
		activity = this.getIntent().getStringExtra("ACTIVITY");
		alcoholType = this.getIntent().getIntExtra("BEHAVIOR", -1);
		title = this.getIntent().getStringExtra("TITLE");
		
		Log.d("typeActivity", "activity="+activity+", title="+getResources().getString(R.string.number_alcohol));
		Log.d("alcoholType=1", ""+alcoholType);
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setText(title);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.typescreen);
		layout.setBackgroundColor(Color.GRAY);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setBackgroundColor(Color.GRAY);
		gridview.setAdapter(new ImageAdapterWithButtons(this,getGridObjs()));
		gridview.setOnItemClickListener(this);
		
		soundfile = this.getIntent().getStringExtra("SOUND");
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);	
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});
		registry.getLog().logNewClick(4);
		Log.d("alcoholType=2", ""+alcoholType);
		//playSound(soundfile);
	}

	protected ArrayList<GridObject> getGridObjs() {	
	
		Log.d("alcohol", "alcohol type " + alcoholType);
		if(activity.equals("cigarette")) {
			arr[0] = R.drawable.one_cigarette_wide;
			arr[1] = R.drawable.two_cigarettes;
			arr[2] = R.drawable.three_cigarettes;
			arr[3] = R.drawable.four_cigarettes;
			arrNumber[0] = "Un Cigarro";
			arrNumber[1] = "Dos Cigarros";
			arrNumber[2] = "Tres Cigarros";
			arrNumber[3] = "Cuatro Cigarros";
			arrSound[0] = "onecig.ogg";
			arrSound[1] = "twocig.ogg";
			arrSound[2] = "threecig.ogg";
			arrSound[3] = "fourcig.ogg";
		}
		else {
			//int type_alcohol = Integer.getInteger(alcoholType);
			if (alcoholType == 0) {
				arr[0] = R.drawable.one_shot;
				arr[1] = R.drawable.two_shots;
				arr[2] = R.drawable.three_shots;
				arr[3] = R.drawable.four_shots;
				arrNumber[0] = "Un trago";
				arrNumber[1] = "Dos tragos ";
				arrNumber[2] = "Tres tragos";
				arrNumber[3] = "Cuatro tragos";
				arrSound[0] = "alcohol_one_shot.ogg";
				arrSound[1] = "alcohol_two_shots.ogg";
				arrSound[2] = "alcohol_three_shots.ogg";
				arrSound[3] = "alcohol_four_shots.ogg";
			}
			else if(alcoholType == 1) {
				arr[0] = R.drawable.one_glass;
				arr[1] = R.drawable.two_glasses;
				arr[2] = R.drawable.three_glasses;
				arr[3] = R.drawable.four_glasses;
				arrNumber[0] = "Una copa";
				arrNumber[1] = "Dos copas";
				arrNumber[2] = "Tres copas";
				arrNumber[3] = "Cuatro copas";
				arrSound[0] = "alcohol_one_glass.ogg";
				arrSound[1] = "alcohol_two_glasses.ogg";
				arrSound[2] = "alcohol_three_glasses.ogg";
				arrSound[3] = "alcohol_four_glasses.ogg";
			}
			else if(alcoholType == 2) {
				arr[0] = R.drawable.one_bottle;
				arr[1] = R.drawable.two_bottles;
				arr[2] = R.drawable.three_bottles;
				arr[3] = R.drawable.four_bottles;
				arrNumber[0] = "Una botella";
				arrNumber[1] = "Dos botellas";
				arrNumber[2] = "Tres botellas";
				arrNumber[3] = "Cuatro botellas";
				arrSound[0] = "alcohol_one_bottle.ogg";
				arrSound[1] = "alcohol_two_bottles.ogg";
				arrSound[2] = "alcohol_three_bottles.ogg";
				arrSound[3] = "alcohol_four_bottles.ogg";
			}
		}
		
		ArrayList<GridObject> gObjList = new ArrayList<GridObject>();
		for(int i=0; i<arr.length; i++) {
			GridObject gobj = new GridObject(arr[i],arrNumber[i],arrSound[i]);
			gObjList.add(gobj);
			gobj = null; 
		}	
		
	return gObjList;
} 
	

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		currentView=v;
		currentView.setBackgroundColor(Color.rgb(230, 140, 0));	
		showToast();
		//playSound("thanks.ogg");
		Intent intent = new Intent(AlcoholCountActivity.this, MainActivity.class);
		intent.putExtra("TITLE", title);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("SOUND", "thanks.ogg");
		registry.getLog().logFinalClick("icon"+(position+1), 1);
		registry.getLog().logChoice("alcohol_count", position+1);
		startActivity(intent);
		resetReminders();
		finish();
	}
}