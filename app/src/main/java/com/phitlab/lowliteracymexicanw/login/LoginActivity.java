package com.phitlab.lowliteracymexicanw.login;

import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import com.phitlab.lowliteracymexicanw.BehaviorActivity;
import com.phitlab.lowliteracymexicanw.MainActivity;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.TypeActivity;
import com.phitlab.lowliteracymexicanw.logDB.LogDBAdapter;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import com.phitlab.lowliteracymexicanw.objects.ReminderTimes;
import com.phitlab.lowliteracymexicanw.reminders.EODAlarmReceiver;
import com.phitlab.lowliteracymexicanw.reminders.ReminderAlarmReceiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	protected static final int PASSWORD = 6;
	ImageButton b;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog;
	protected int[] passImg;
	String user;
	int cnt;
	String password;
	protected Registry registry;
	protected int title_sound;
	private int reminder;
	private MediaPlayer mp;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_a);
        setVariables();
//        
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
//        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
//        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
//        	    WindowManager.LayoutParams.FLAG_FULLSCREEN |
//        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
//        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        
        Log.d("LoginActivity", "startDialog");
        if (getIntent().hasExtra("startDialog")) {
        	//LogoutActivity.h.sendEmptyMessage(0);
        	Log.d("LoginActivity", "startDialog");
        	//stopReminderTimer();
        }
        reminder = 0;
        if (getIntent().hasExtra("REMINDER")) {
        	 reminder = this.getIntent().getIntExtra("REMINDER", -1);
        	 Log.d("LoginActivity", "REMINDER " + reminder);
        }
        //playSound("enter_password.ogg");
        
    	ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound("enter_password.ogg");
				//registry.getLog().logAction(title_sound++, "title_sound", "click_log");
			}
			
		});
		
        b = (ImageButton)findViewById(R.id.btn_login);  
        b.setOnTouchListener(new OnTouchListener() {

            @Override
			public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && cnt>0) {
                	b.setImageResource(R.drawable.unlock);

                }
                return false;
            }
        });

        
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cnt>0) {
//					dialog = ProgressDialog.show(LoginActivity.this, "", 
//	                        "Validación de código ...", true);
					login(0);
					//playSound("wait.ogg");			
					}
				}
			});
        
        ((Button)findViewById(R.id.btn_one)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_one);
					password += "1";
				 }
				else {
					showToast(R.string.password_maxlength, R.drawable.notify);
					playSound("password_maxlength.ogg");
				}
			}
        });

        ((Button)findViewById(R.id.btn_two)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_two);
						    	 password += "2";
						    }
					 });
					 Log.d("Password", "Password is " + password);
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((Button)findViewById(R.id.btn_three)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_three);
						    	password += "3";
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((Button)findViewById(R.id.btn_four)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_four);
						    	password += "4";
						    }
					 });
				 }    
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((Button)findViewById(R.id.btn_five)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_five);
						    	password += "5";
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((Button)findViewById(R.id.btn_six)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_six);
						    	password += "6";
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        
        ((Button)findViewById(R.id.btn_seven)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_seven);
						    	password += "7";
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((Button)findViewById(R.id.btn_eight)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_eight);
						    	password += "8";
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((Button)findViewById(R.id.btn_nine)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_nine);
						    	password += "9";
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((Button)findViewById(R.id.btn_zero)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.digit_zero);
						    	password += "0";
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						playSound("password_maxlength.ogg");
					}
			}
        });
        
        ((ImageButton)findViewById(R.id.btn_delete)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cnt > 0) {
					runOnUiThread(new Runnable() {
					    @Override
						public void run() {
					    	((ImageView)findViewById(passImg[--cnt])).setBackgroundResource(R.drawable.gray);					    
					    	password = password.substring(0,cnt);
					    	Log.d("Password", "Password is" + password);
					    }
					});		
				}
			}
        	
        });
    }
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("LoginActivity", "onResume");
	}
	protected void playSound(String filename) {
		mp = new MediaPlayer();

	    try {
            mp.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Data/AppAudio/"+filename);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

		private void setVariables() {
			passImg = new int[] {R.id.img_pass_1, R.id.img_pass_2, R.id.img_pass_3, R.id.img_pass_4, R.id.img_pass_5, R.id.img_pass_6};
			cnt = 0;
			password ="";
			registry = Registry.getInstance();
			user = registry.getUserName();		
			dialog = null;
			title_sound = 0;
			registry.setLoginStatus(false);
	}

	void login(int id){
		try{			
			Log.d("LoginActivitybegin", "password is " + password);
			LogDBAdapter log = Registry.getInstance().getLog();
			String response = log.verifyPassword(password);
			Log.d("LoginActivity", "password is " + password);
			Log.d("LoginActivity", "response " + response);
//			runOnUiThread(new Runnable() {
//			    public void run() {
//					dialog.dismiss();
//			    }
//			});
			if(response.equalsIgnoreCase("User Found")){
				Log.d("LoginActivity", "reminder = " + reminder + ", id = " + id);
				if (id == 0) {
					if (reminder == 0) 
						startActivity((new Intent(LoginActivity.this, MainActivity.class)).putExtra("SOUND", "main.ogg"));
					else if (reminder == 1) {
						Intent intent = new Intent(LoginActivity.this, BehaviorActivity.class);
						intent.putExtra("SOUND", "behavior.ogg");
						intent.putExtra("TITLE", getResources().getString(R.string.behavior_title));	
						startActivity(intent);
					}
					else if (reminder == 2) {
						Intent intent = new Intent(LoginActivity.this, TypeActivity.class);	
						intent.putExtra("BEHAVIOR", R.drawable.home_diet);
						intent.putExtra("ACTIVITY", "diet");
						intent.putExtra("SOUND", "diet_type.ogg");
						intent.putExtra("TITLE", getResources().getString(R.string.diet_type));
						startActivity(intent);
					}
					registry.setLoginStatus(true);
				} else if (id == 1) {
					startActivity(new Intent(LoginActivity.this, UpdatePasswordActivity.class));
					Log.d("hello", "what is going on");
				}
				finish();
			}else{
				if(id == 0) {
					runOnUiThread(new Runnable() {
					    @Override
						public void run() {
					    	Log.d("incorrect pass", "runnable thread");
					    	b.setImageResource(R.drawable.lock);
					    }
					});
				}
				playSound("password_wrong.ogg");
				//showAlert();
				showToast(R.string.incorrect_password, R.drawable.notify);
			}
			
		}catch(Exception e){
			dialog.dismiss();
			Log.d("LoginActivity", "password is " + password);
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		stopPlaying();
		
	}
	 	
	@Override
	public void onBackPressed() {
	    moveTaskToBack(true);
	}
 
	protected void showToast(int message, int img_resource) {
		View toastView = getLayoutInflater().inflate(R.layout.toast_emotion, 
				(ViewGroup)findViewById(R.id.toastLayout));
	
		ImageView imgView = (ImageView)toastView.findViewById(R.id.emotion_selected);
		imgView.setImageResource(img_resource);
		
		TextView txtView = (TextView)toastView.findViewById(R.id.done_text);
		txtView.setText(getResources().getString(message));
		
		Toast toast = new Toast(this);
     	toast.setGravity(Gravity.CENTER, 0, 0);
     	toast.setDuration(Toast.LENGTH_SHORT);
     	toast.setView(toastView);
     	toast.show();
	}
	
	private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
       }
    }
	
	@Override
	public void onStop() {
		super.onStop();
		stopPlaying();
	}
	
    
	   @Override
	    public void onWindowFocusChanged(boolean hasFocus) {
	        super.onWindowFocusChanged(hasFocus);

	        if (!hasFocus) {
	            windowCloseHandler.postDelayed(windowCloserRunnable, 250);
	        }
	        Log.d("Hello", "LoginActivity");
	    }

	    private void toggleRecents() {
	        Intent closeRecents = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
	        closeRecents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
	        ComponentName recents = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
	        closeRecents.setComponent(recents);
	        this.startActivity(closeRecents);
	    }

	    private Handler windowCloseHandler = new Handler();
	    private Runnable windowCloserRunnable = new Runnable() {
	        @Override
			public void run() {
	            ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
	            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

	            if (cn != null && cn.getClassName().equals("com.android.systemui.recent.RecentsActivity")) {
	                toggleRecents();
	            }
	        }
	    };
}

