package com.phitlab.lowliteracymexicanw.login;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.phitlab.lowliteracymexicanw.MainActivity;
import com.phitlab.lowliteracymexicanw.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class UpdatePasswordActivityB extends Activity {

	protected static final int PASSWORD = 5;
	private ProgressDialog dialog;
	private DefaultHttpClient httpclient;
	private HttpPost httppost;
	private ArrayList<NameValuePair> nameValuePairs;
	private HttpResponse response;
	private String password;
	private int cnt;
	protected int[] passImg;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_update_b);
        setVariables();
        
        ((ImageButton) findViewById(R.id.ok)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("hello", "password");
				if (password.length() == PASSWORD) {
					dialog = ProgressDialog.show(UpdatePasswordActivityB.this, "", 
	                       "Updating código ...", true);
					 new Thread(new Runnable() {
						    @Override
							public void run() {
						    	updatePassword();	
						    }
						  }).start();	
					 //startActivity(new Intent(UpdatePasswordActivityB.this, LoginActivityB.class ));
					 //finish();
					}
				}
			});
        
        ((ImageButton) findViewById(R.id.remove)).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				startActivity(new Intent(UpdatePasswordActivityB.this, LoginActivityB.class ));
 				UpdatePasswordActivityB.this.finish();
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
					Toast.makeText(UpdatePasswordActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(UpdatePasswordActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(UpdatePasswordActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(UpdatePasswordActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(UpdatePasswordActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(UpdatePasswordActivityB.this,"Cannot add more", Toast.LENGTH_SHORT).show();
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

	private void updatePassword() {
		try{			
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://156.56.95.6/llmw_folder/update_password.php"); // make sure the url is correct.
			nameValuePairs = new ArrayList<NameValuePair>(2);
			
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("user_id","test_user"));  // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("password",password)); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			//Execute HTTP Post Request
			response=httpclient.execute(httppost);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String myResponse = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			
			if (myResponse.equals("Successful")) {
				runOnUiThread(new Runnable() {
				    @Override
					public void run() {
						dialog.dismiss();
					   	Toast.makeText(UpdatePasswordActivityB.this,"Código cambiado. Muchas gracias", Toast.LENGTH_SHORT).show();			 
				    }
				});	
				startActivity(new Intent(UpdatePasswordActivityB.this, MainActivity.class));
				finish();	
			}
			else {
			   	Toast.makeText(UpdatePasswordActivityB.this,"Error de red. Favor de tratar ótravez", Toast.LENGTH_SHORT).show();			 
			}
		}catch(Exception e){
			//dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
	private void setVariables() {
		passImg = new int[] {R.id.img_pass_1, R.id.img_pass_2, R.id.img_pass_3, R.id.img_pass_4, R.id.img_pass_5};
		cnt = 0;	
		password = "";
	}
}
