package com.phitlab.lowliteracymexicanw;

import java.util.ArrayList;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.GridObject;
import com.phitlab.lowliteracymexicanw.objects.ImageAdapterWithButtons;
import com.phitlab.lowliteracymexicanw.sync.ScreenDBController;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class ContextActivity extends BaseActivity {

	public Context c;
	private View prevView, currentView;
	public GridView gridview;
	private String activity;
//	private String soundfile;
//	protected Registry registry;
//	private int title_sound = 0;
//	private boolean new_activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_context);

		LinearLayout layout = (LinearLayout) findViewById(R.id.contextscreen);
		layout.setBackgroundColor(Color.GRAY);

		String title = this.getIntent().getStringExtra("TITLE");
		activity = this.getIntent().getStringExtra("ACTIVITY");
//		registry=Registry.getInstance();
		
		TextView titleTextView = (TextView) findViewById(R.id.titleContextTextView);
		titleTextView.setText(title);

        ArrayList<String> arrURLs = getImageURLsFor();
        ArrayList<String> arrLabels =  getImageLabels();
        ArrayList<String> arrSounds =  getAudioFiles();
		
        GridObject gobj;
		ArrayList<GridObject> gObjList = new ArrayList<GridObject>();
		for(int i=0; i<arrURLs.size(); i++) {
			gobj = new GridObject(arrURLs.get(i),arrLabels.get(i),arrSounds.get(i));
			gObjList.add(gobj); 
		}	
		
		gridview = (GridView) findViewById(R.id.gridview);		
        gridview.setBackgroundColor(Color.GRAY);
		gridview.setAdapter(new ImageAdapterWithButtons(ContextActivity.this,gObjList,1));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				currentView=v;
				currentView.setBackgroundColor(Color.rgb(230, 140, 0));
				showToast();
				Intent intent = new Intent(ContextActivity.this, MainActivity.class);
				intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
				intent.putExtra("SOUND", "thanks.ogg");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				resetReminders();
				registry.getLog().logFinalClick("icon"+(position+1), 1);
				registry.getLog().logChoice("context_people", (position+1));
				finish();
			}
		});
		
		soundfile = "context_people.ogg";
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);	
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});
		registry.getLog().logNewClick(8);
		//playSound(soundfile);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_context, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Back", "Contex activity Back button clicked");
			// moveTaskToBack(true);
			// return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected ArrayList<String> getImageURLsFor() {
		ScreenDBController dbController = ScreenDBController.getInstance(this);
		dbController.open();

		ArrayList<String> al = new ArrayList<String>();
		al.clear();
		al = dbController.getImageURLs("People");
		dbController.close();

		return al;
	}   
	
	protected ArrayList<String> getImageLabels() {
		ScreenDBController dbController = ScreenDBController.getInstance(this);
		dbController.open();

		ArrayList<String> labels = new ArrayList<String>();
		labels.clear();
		labels = dbController.getLabels("PeopleTag");
		dbController.close();

		return labels;
	} 

	protected ArrayList<String> getAudioFiles() {
		ScreenDBController dbController = ScreenDBController.getInstance(this);
		dbController.open();
	
		ArrayList<String> sounds = new ArrayList<String>();
		sounds.clear();
		sounds = dbController.getLabels("PeopleSounds");
		dbController.close();
	
		return sounds;
	} 
}
   