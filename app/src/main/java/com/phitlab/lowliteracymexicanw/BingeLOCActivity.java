package com.phitlab.lowliteracymexicanw;

import com.phitlab.lowliteracymexicanw.R;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BingeLOCActivity extends BaseActivity {
//	
//	private MediaPlayer splashSound;
//	protected Registry registry;
//	protected int title_sound=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binge_loc);		
//		splashSound = MediaPlayer.create(this,R.raw.startup);
//		registry = Registry.getInstance();
		
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound("loc.ogg");	
				registry.getLog().logChoice("title_sound", title_sound++);
			}
			
		});
		registry.getLog().logNewClick(6);
		//playSound("loc.ogg");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_binge_loc, menu);
        return true;
    }

    
    public void yesButtonClick(View view) {
    	Intent intent = new Intent(this,BingeEmbarrasActivity.class);
		intent.putExtra("ACTIVITY", "binge");
		registry.getLog().logFinalClick("yes_icon", 1);
		registry.getLog().logChoice("loss_of_control", 1);
		startActivity(intent);
	}
    
    public void noButtonClick(View view) {
    	Intent intent = new Intent(this,BingeEmbarrasActivity.class);
    	intent.putExtra("ACTIVITY", "binge");
		registry.getLog().logFinalClick("no_icon", 1);
		registry.getLog().logChoice("loss_of_control", -1);
		startActivity(intent);
	}
    
//	@Override
//	protected void onStop() {
//    	super.onStop();
//    	stopReminderTimer();
//    }
    
//	@Override
//	protected void onDestroy() {
//    	super.onDestroy();
//    	if(!getNewActivity())
//    		logoutWarning();
//    }
}
