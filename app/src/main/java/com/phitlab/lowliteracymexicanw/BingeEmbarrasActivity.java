package com.phitlab.lowliteracymexicanw;

import com.phitlab.lowliteracymexicanw.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BingeEmbarrasActivity extends BaseActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binge_embarras);
        //Button btn=(Button)findViewById(R.id.nextEmbButton); btn.setEnabled(false);
        
//        String title = this.getIntent().getStringExtra("ScreenTitle");
//		TextView titleTextView = (TextView) findViewById(R.id.titleEmbTextView);
//		titleTextView.setText(title);

//        soundfile = this.getIntent().getStringExtra("SOUND");
		ImageView titleSound = (ImageView) findViewById(R.id.playVoice);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound("embarrass.ogg");
				registry.getLog().logClick(title_sound ++, "title_sound");
			}
			
		});
		registry.getLog().logNewClick(7);
		//playSound("embarrass.ogg");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_binge_embarras, menu);
        return true;
    }

    public void yesButtonClick(View view) {
    	Intent intent = new Intent(this, ContextActivity.class);
		intent.putExtra("TITLE", getResources().getString(R.string.binge_people_title));
		intent.putExtra("ACTIVITY", "eae");
		registry.getLog().logFinalClick("yes_icon", 1);
		registry.getLog().logChoice("embarrass", 1);
		startActivity(intent);		
	}
    
    public void noButtonClick(View view) {
    	Intent intent = new Intent(this, ContextActivity.class);
		intent.putExtra("TITLE", getResources().getString(R.string.binge_people_title));
		intent.putExtra("ACTIVITY", "eae");
		registry.getLog().logFinalClick("no_icon", 1);
		registry.getLog().logChoice("embarrass", -1);
		startActivity(intent);
	}
    
//	@Override
//	protected void onDestroy() {
//    	super.onDestroy();
//    	if(!getNewActivity())
//    		logoutWarning();
//    }
    
}
