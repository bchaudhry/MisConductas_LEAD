package com.phitlab.lowliteracymexicanw.login;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.phitlab.lowliteracymexicanw.R;
import com.phitlab.lowliteracymexicanw.MainActivity;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivityB extends Activity {
	
	protected static final int PASSWORD = 5;
	private ImageButton b;
	private int cnt;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	protected int[] passImg;
	String password;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_b);
        
        setVariables();
        b = (ImageButton)findViewById(R.id.btn_login);  
        b.setOnTouchListener(new OnTouchListener() {

            @Override
			public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && cnt> 0) {
                	b.setImageResource(R.drawable.unlock);
                }
                return false;
            }
        });

        
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cnt > 0) {
					dialog = ProgressDialog.show(LoginActivityB.this, "", 
	                        "Validación de código ...", true);
					 new Thread(new Runnable() {
						    @Override
							public void run() {
						    	login(0);	
						    }
						  }).start();				
					}
				}
			});
        
        ((ImageButton)findViewById(R.id.btn_reset)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if  (cnt == PASSWORD) {
					dialog = ProgressDialog.show(LoginActivityB.this, "", 
	                        "Validación de código ...", true);
					 new Thread(new Runnable() {
						    @Override
							public void run() {
						    	login(1);	
						    }
						  }).start();				
					}				
			}	
        });
        
        ((ImageButton)findViewById(R.id.btn_heart)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.shape_heart);
					password += "1";
				 }
				else 
					Toast.makeText(LoginActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
			}
        });

        ((ImageButton)findViewById(R.id.btn_diamond)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.shape_diamond);
						    	 password += "2";
						    }
					 });
					 Log.d("Password", "Password is " + password);
				 }
				 else 
					Toast.makeText(LoginActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
			}
        });
        
        ((ImageButton)findViewById(R.id.btn_circle)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.shape_circle);
						    	password += "3";
						    }
					 });
				 }
				 else 
					Toast.makeText(LoginActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
			}
        });
        
        ((ImageButton)findViewById(R.id.btn_star)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.shape_star);
						    	password += "4";
						    }
					 });
				 }    
				 else 
					Toast.makeText(LoginActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
			}
        });
        
        ((ImageButton)findViewById(R.id.btn_triangle)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.shape_triangle);
						    	password += "5";
						    }
					 });
				 }
				 else 
					Toast.makeText(LoginActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
			}
        });
        
        ((ImageButton)findViewById(R.id.btn_octagon)).setOnClickListener (new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 if (cnt < PASSWORD) {
					 runOnUiThread(new Runnable() {
						    @Override
							public void run() {
						    	((ImageView)findViewById(passImg[cnt++])).setBackgroundResource(R.drawable.shape_octagon);
						    	password += "6";
						    }
					 });
				 }
				 else 
					Toast.makeText(LoginActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
			}
        });
        
        
        ((ImageButton)findViewById(R.id.btn_delete)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cnt > 0) {
					runOnUiThread(new Runnable() {
					    @Override
						public void run() {
					    	((ImageView)findViewById(passImg[--cnt])).setBackgroundResource(R.drawable.blank);					    
					    	password = password.substring(0,cnt);
					    	Log.d("Password", "Password is" + password);
					    }
					});		
				}
			}
        	
        });
    }
	
	private void setVariables() {
		passImg = new int[] {R.id.img_pass_1, R.id.img_pass_2, R.id.img_pass_3, R.id.img_pass_4, R.id.img_pass_5};
		cnt = 0;
		password ="";
		
	}

	void login(int id){
		try{			
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://156.56.95.6/llmw_folder/check.php"); // make sure the url is correct.
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("user_id","beta_user0"));  // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("password", password)); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//Execute HTTP Post Request
			response=httpclient.execute(httppost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
			    @Override
				public void run() {
					dialog.dismiss();
			    }
			});
			
			if(response.equalsIgnoreCase("User Found")){
				runOnUiThread(new Runnable() {
				    @Override
					public void run() {
				    	Toast.makeText(LoginActivityB.this,"Código aceptado", Toast.LENGTH_SHORT).show();
				    	//pass.setText("");
				    }
				});
				if (id == 0)
					startActivity(new Intent(LoginActivityB.this, MainActivity.class));
				else if (id == 1) {
					startActivity(new Intent(LoginActivityB.this, UpdatePasswordActivityB.class));
					Log.d("hello", "what is going on");
				}
				finish();
			}else{
				//showAlert();
				if(id == 0) {
					runOnUiThread(new Runnable() {
					    @Override
						public void run() {
					    	Log.d("incorrect pass", "runnable thread");
					    	b.setImageResource(R.drawable.lock);
					    }
					});
				}
				showAlert();
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}
	public void showAlert(){
		LoginActivityB.this.runOnUiThread(new Runnable() {
		    @Override
			public void run() {
		     	final Dialog incorrect_dialog = new Dialog(LoginActivityB.this);
				incorrect_dialog.setContentView(R.layout.dialog_login_message);
				incorrect_dialog.setTitle(getResources().getString(R.string.incorrect_password));
				incorrect_dialog.setCancelable(false);
				
				ImageView btnCheckButton = (ImageView) incorrect_dialog.findViewById(R.id.ok);
				btnCheckButton.setOnClickListener(new OnClickListener() {
		
					@Override
					public void onClick(View v) {
						incorrect_dialog.dismiss();
					}
				});
				incorrect_dialog.show();
		    }
		});   	  
	}
}

