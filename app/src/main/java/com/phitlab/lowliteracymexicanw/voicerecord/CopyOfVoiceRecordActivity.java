package com.phitlab.lowliteracymexicanw.voicerecord;

/*
 * The application needs to have the permission to write to external storage
 * if the output file is written to the external storage, and also the
 * permission to record audio. These permissions must be set in the
 * application's AndroidManifest.xml file, with something like:
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.phitlab.lowliteracymexicanw.BaseActivity;
import com.phitlab.lowliteracymexicanw.MainActivity;
import com.phitlab.lowliteracymexicanw.R;

public class CopyOfVoiceRecordActivity extends BaseActivity implements OnClickListener
{
	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FOLDER = "LEADVoices";
	private static final String LOG_TAG = "Voice Recorder";

	private MediaRecorder recorder = null;
	private MediaPlayer mPlayer = null;

	private int currentFormat = 0;
	AlertDialog alertDialog;
	private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,
			MediaRecorder.OutputFormat.THREE_GPP };
	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4,
			AUDIO_RECORDER_FILE_EXT_3GP };
	private boolean recorded;
	private String timeStamp;
	private Long beginTime, endTime;
	private final Handler myHandler = new Handler();
	private final Runnable updateRunnable = new Runnable() {
        @Override
		public void run() {
            //call the activity method that updates the UI
            try {
				Thread.sleep(endTime-beginTime);
				setPlayVisibility(true);
				enableButton(R.id.btnStart, true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    };
    private final Runnable recordRunnable = new Runnable() {
        @Override
		public void run() {
            //call the activity method that updates the UI
            try {
				Thread.sleep(endTime-beginTime);
				recorder = new MediaRecorder();
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(output_formats[currentFormat]);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				recorder.setOutputFile(getFilename());
				recorder.setOnErrorListener(errorListener);
				recorder.setOnInfoListener(infoListener);			
				beginTime = System.currentTimeMillis();			
				try {
					recorder.prepare();
					recorder.start();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voicerecord);

		String title = this.getIntent().getStringExtra("TITLE");
		soundfile = this.getIntent().getStringExtra("SOUND");
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setText(title);

		RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.middleLayout);
		rlayout.setBackgroundColor(Color.WHITE);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.bottomLayout);
		layout.setBackgroundColor(Color.GRAY);		
		
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);	
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});
		
		//time_save = System.currentTimeMillis();
		timeStamp = new SimpleDateFormat("MMdd_HHmm").format(new Date());
	    recorded = false;
		setButtonHandlers();
		initVisibility();
		enableRecord(true);
		registry.getLog().logNewClick(14);
	}

	private void setRecordVisibility(boolean bool) {
		setVisibilityButton(R.id.btnStart, bool);
		setVisibilityButton(R.id.btnStop, !bool);
		setVisibilityView(R.id.txtStart, bool, 1);
		setVisibilityView(R.id.txtStop, !bool, 1);
		setVisibilityView(R.id.soundStart, bool, 0);
		setVisibilityView(R.id.soundStop, !bool, 0);
	}
	
	private void initVisibility() {
		setVisibilityButton(R.id.btnStart, true);
		setVisibilityButton(R.id.btnStop, false);
		setVisibilityButton(R.id.btnPlay, false);
		setVisibilityButton(R.id.btnPause, false);	
		setVisibilityView(R.id.txtStart, true, 1);
		setVisibilityView(R.id.txtStop, false, 1);
		setVisibilityView(R.id.txtPlay, false, 1);
		setVisibilityView(R.id.txtPause, false, 1);
		setVisibilityView(R.id.soundStart, true, 0);
		setVisibilityView(R.id.soundStop, false, 0);
		setVisibilityView(R.id.soundPlay, false, 0);
		setVisibilityView(R.id.soundPause, false, 0 );
	}

	private void setPlayVisibility(boolean bool) {
		setVisibilityButton(R.id.btnPlay, bool);
		setVisibilityButton(R.id.btnPause, !bool);
		setVisibilityView(R.id.txtPlay, bool, 1);
		setVisibilityView(R.id.txtPause, !bool, 1);
		setVisibilityView(R.id.soundPlay, bool, 0);
		setVisibilityView(R.id.soundPause, !bool, 0);
	}
	
	private void setVisibilityView(int id, boolean bool, int type) {
		if (type == 0) {
			if (bool) 
				((ImageView) findViewById(id)).setVisibility(View.VISIBLE);
			else 
				((ImageView) findViewById(id)).setVisibility(View.INVISIBLE);		
		}
		else {
			if (bool) 
				((TextView) findViewById(id)).setVisibility(View.VISIBLE);
			else 
				((TextView) findViewById(id)).setVisibility(View.INVISIBLE);
		}
	}

	private void setVisibilityButton(int id, boolean bool) {
		if (bool) 
			((ImageButton) findViewById(id)).setVisibility(View.VISIBLE);
		else 
			((ImageButton) findViewById(id)).setVisibility(View.INVISIBLE);
	}

	private void setButtonHandlers() {
		((ImageButton) findViewById(R.id.btnStart)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnStop)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnPlay)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnPause)).setOnClickListener(this);
		((ImageView) findViewById(R.id.soundStart)).setOnClickListener(this);
		((ImageView) findViewById(R.id.soundStop)).setOnClickListener(this);
		((ImageView) findViewById(R.id.soundPlay)).setOnClickListener(this);
		((ImageView) findViewById(R.id.soundPause)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.btnNext)).setOnClickListener(this);
	}

	private void enableButton(int id, boolean isEnable) {
		((ImageButton) findViewById(id)).setEnabled(isEnable);
	}

	private void enableRecord(boolean bool) {
		enableButton(R.id.btnStart, bool);
		enableButton(R.id.btnPlay, !bool);
	}

	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);

		if (!file.exists()) {
			file.mkdirs();
		}
		return (file.getAbsolutePath() + "/" + "VOICE_" + timeStamp + file_exts[currentFormat]);
	}

	private void startRecording() {
	
		//showMessageToast(getResources().getString(R.string.stop_instruction), R.drawable.record_icon);
		beginTime = System.currentTimeMillis();
		//playSound("stop_instruction.ogg");
		endTime = System.currentTimeMillis();
		
		enableRecord(true);
		setRecordVisibility(false);
		//recorded = true;
		
  	    new Thread(new Runnable() {
			@Override
			public void run() {
				myHandler.post(recordRunnable);
			}
  	   }).start();		
	}

	private void stopRecording() {
		if (null != recorder) {
			//showMessageToast(getResources().getString(R.string.record_instruction2), R.drawable.stop_speaker);
			recorder.stop();
			recorder.reset();
			recorder.release();
			//playSound("playback_instruction.ogg");
			endTime = System.currentTimeMillis();
			recorder = null;
			recorded = true;
		}
	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Toast.makeText(CopyOfVoiceRecordActivity.this,
					"Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Toast.makeText(CopyOfVoiceRecordActivity.this,
					"Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
					.show();
		}
	};
	private int record = 0;
	private int stop = 0;
	private int play = 0;
	private int pause = 0;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnStart: {
				if (recorded) displayPopup();
				else { 
					startRecording();
					registry.getLog().logClick(record++, "record_icon");
				}
				break;
			}
			case R.id.btnStop: {
				setRecordVisibility(true);
				stopRecording();
				setPlayVisibility(true);
				enableButton(R.id.btnPlay, true);
				registry.getLog().logClick(stop++, "stop_icon");
				break;
			}
			case R.id.btnPlay: {
				setPlayVisibility(false);
				enableRecord(false);
				startPlaying();
				registry.getLog().logClick(play++, "play_icon");
				break;
			}
			case R.id.btnPause: {
				setPlayVisibility(true);
				//enableRecord(true);
				stopPlaying();
				enableButton(R.id.btnStart, true);
				registry.getLog().logClick(pause++, "pause_icon");
				break;
			}
			case R.id.soundStart: {
				playSound("record.ogg");
				break;
			}
			case R.id.soundStop: {
				playSound("stop.ogg");
				break;
			}
			case R.id.soundPlay: {
				playSound("play.ogg");
				break;
			}
			case R.id.soundPause: {
				playSound("pause.ogg");
				break;
			}
			case R.id.btnNext: {
				//registry.getLog().logClick(1, "next_icon");
				nextButtonClick(v);
				break;
			}
		}
	}

	private void stopPlaying() {
		mPlayer.release();
	    mPlayer = null;
	    setPlayVisibility(true);
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(getFilename());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        
        new Thread(new Runnable() {
			@Override
			public void run() {
				 myHandler.post(updateRunnable);
			}        
        }).start();
	}
	
	protected void displayPopup() {

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_voice);
		//dialog.setTitle(getResources().getString(R.string.re_record));
		dialog.setCancelable(false);
		//playSound("over-record_warning.ogg");
		ImageView titleSound = (ImageView) dialog.findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound("over_record.ogg");	
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});
		
		ImageView btnCheckButton = (ImageView) dialog.findViewById(R.id.ok);
		btnCheckButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File f = new File(getFilename());
	            if (f!=null && f.exists())
	            	f.delete();
	      		dialog.dismiss();
	            startRecording();
			}
		});
  
		ImageView btnCrossButton = (ImageView) dialog.findViewById(R.id.remove);
		btnCrossButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//enableRecord(false);
				dialog.dismiss();
				playSound("further_instructions.ogg");
				//showMessageToast(getResources().getString(R.string.finish_message), R.drawable.icon_speaker2);
			}
		});
		dialog.show();
		//playSound(getResources().getString(R.string.re_record));		
	}
	
	public void nextButtonClick(View view) {
		if (recorded) {
			registry.getLog().logFinalClick("next_icon", 1);
//			registry.getLog().logChoice("voice_recording", "VOICE_" + timeStamp + file_exts[currentFormat]);
			registry.getLog().logChoice("voice_recording", "VOICE_" + timeStamp + ".mp3");
			registry.getLog().logChoice("voice_mp4", "VOICE_" + timeStamp + file_exts[currentFormat]);
			showToast();
			
			//DELETE FOLLOWING UPTO COMMENTED BLOCK
//			Intent intent = new Intent(VoiceRecordActivity.this, DemoClient.class);
//			intent.putExtra("VOICE", "VOICE_" + timeStamp);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			finish();
		      
			Intent intent = new Intent(CopyOfVoiceRecordActivity.this, MainActivity.class);
			intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		else {
			  registry.getLog().logClick(1, "next_icon");
			  showMessageToast(this.getResources().getString(R.string.recording_request), R.drawable.icon_speaker2);
			  playSound("recording_request.ogg");
		}
	}
}

//			final Dialog dig = new Dialog(VoiceRecordActivity.this);
//			dig.setContentView(R.layout.dialog_behavior);
//			runOnUiThread(new Runnable() {
//			    public void run() {
//			    	TextView reminderQuestion = (TextView) dig.findViewById(R.id.reminder_question);
//			    	reminderQuestion.setText(R.string.eod_reminder_question);
//			    }
//			});
//			
//			dig.setTitle(getResources().getString(R.string.menu_settings));
//			dig.setCancelable(false);
//			dig.show();
//			//soundfile = getResources().getString(R.string.eod_question);
//			//playSound(soundfile);
//			ImageView titleSound = (ImageView) dig.findViewById(R.id.sound_Icon);
//			titleSound.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View v) {
//					playSound("eod_reminder_question.ogg");	
//					registry.getLog().logClick(title_sound++, "title_sound");
//				}
//				
//			});
//			
//			ImageView btnCheckButton = (ImageView) dig.findViewById(R.id.ok);
//			btnCheckButton.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View v) {
//					Intent intent = new Intent(VoiceRecordActivity.this, BehaviorActivity.class);
//	    	    	intent.putExtra("TITLE", getResources().getString(R.string.behavior_title));
//	    			startActivity(intent);
//	    			dig.dismiss();
//					finish();
//				}
//			});
//	  
//			ImageView btnCrossButton = (ImageView) dig.findViewById(R.id.remove);
//			btnCrossButton.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View v) {
//					
//					dig.dismiss();
//					showToast();
//			        //playSound("thanks.ogg");
//					Intent intent = new Intent(VoiceRecordActivity.this, MainActivity.class);
//					intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//					finish();
//				}
//			});
//			registry.getLog().logFinalClick("next_icon", 1);
//			registry.getLog().logChoice("voice_recording", "VOICE_" + timeStamp + file_exts[currentFormat]);
			
//		}
//		else {
//			  registry.getLog().logClick(1, "next_icon");
//			  showMessageToast(this.getResources().getString(R.string.recording_request), R.drawable.icon_speaker2);
//			  playSound("recording_request.ogg");
//		}
//	}
//}
