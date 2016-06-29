package com.phitlab.lowliteracymexicanw.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.phitlab.lowliteracymexicanw.MainActivity;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.objects.Registry;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePasswordActivity extends Activity {

	protected static final int PASSWORD = 6;
	private int[] passImg;
	private int cnt;
	private String password;
	private Registry registry;
	private int title_sound;
	private String soundfile;
	private MediaPlayer mp;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setVariables();
        registry.getLog().logNewClick(13);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        soundfile = this.getIntent().getStringExtra("SOUND");
        //playSound(soundfile);

        this.getWindow().setFlags(
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        	    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        	    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        	    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_pass_update);
        
    	ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});
		
        ((ImageButton) findViewById(R.id.ok)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("hello", "password");
				if (!password.equals("") && cnt == PASSWORD) {	
					registry.getLog().updatePassword(password);
					//registry.getLog().verifyPassword(password);
					registry.getLog().logFinalClick("yes_icon", 1);
					playSound("password_changed.ogg");
					new uploadPassword().execute();
					startActivity(new Intent(UpdatePasswordActivity.this, MainActivity.class));
					showToast(R.string.password_changed, R.drawable.check);
					finish();
					}
				else {
					showToast(R.string.password_length, R.drawable.notify);
					playSound("password_length.ogg");
				}
			}
		});
        
        ((ImageButton) findViewById(R.id.remove)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(UpdatePasswordActivity.this,"You exited without changing your password", Toast.LENGTH_SHORT).show();
				//playSound("exit_unchange.ogg");
				//registry.getLog().updatePassword(password);
				registry.getLog().logFinalClick("no_icon", 1);
				startActivity(new Intent(UpdatePasswordActivity.this, MainActivity.class));
				showToast(R.string.password_not_changed, R.drawable.check);
				playSound("password_not_changed.ogg");
				finish();
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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
						    	if (password == "") {
						    		password = "0";
						    		System.out.println("password is " + password);
						    	}
						    	else 
						    		password += "0";
						    	System.out.println("password 0 was added" + password);
						    }
					 });
				 }
					else {
						showToast(R.string.password_maxlength, R.drawable.notify);
						UpdatePasswordActivity.this.playSound("password_maxlength.ogg");
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

	private void setVariables() {
		passImg = new int[] {R.id.img_pass_1, R.id.img_pass_2, R.id.img_pass_3, R.id.img_pass_4, R.id.img_pass_5, R.id.img_pass_6};
		cnt = 0;
		password ="";		
		registry = Registry.getInstance();
		title_sound = 0;
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
	    
	    class uploadPassword extends AsyncTask<String,String,String>{

			@Override
			protected String doInBackground(String... arg0) {
				String msg = "";
				sendToServer();
	            return msg;
	        }
			
			@Override
	        protected void onPostExecute(String msg) {
	            //mDisplay.append(msg + "\n");    
				System.out.println(msg);
				
	        }
			
			protected void sendToServer() {
				Log.d("UpdatePasswordActivity", "Send new password to server");
				HttpClient httpclient = new DefaultHttpClient();
				ArrayList<NameValuePair> new_password = registry.getLog().getPassword();
				HttpPost httppost = new HttpPost("https://phitlab.soic.indiana.edu/llmw_folder/update_password.php?format=json");
				String phpResponse = "";
				try {
					httppost.setEntity(new UrlEncodedFormEntity(new_password));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					
					phpResponse = httpclient.execute(httppost, responseHandler);
				} catch (ClientProtocolException e) {
					Log.e("UpdatePasswordActivity", "Error Uploading Password");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("UpdatePasswordActivity", "Error Uploading Password");
					e.printStackTrace();
				} 
				System.out.println("Update password : " + phpResponse); 
				
			}

	    }
	    
}
