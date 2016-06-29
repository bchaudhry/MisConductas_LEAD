package com.phitlab.lowliteracymexicanw.sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.phitlab.lowliteracymexicanw.objects.Registry;
import android.os.AsyncTask;
import android.util.Log;

public class DatabaseSync extends AsyncTask<Void, Void, String>{

	private AsyncTaskCompletedListener listener;	
	Registry reg = Registry.getInstance();
	
	public DatabaseSync(AsyncTaskCompletedListener listener) {
		Log.d("Constructor DBsync","reached here");
		this.listener=listener;
	}


	@Override
	protected void onPostExecute(String results) {
		super.onPostExecute(results);
	}


	@Override
	protected String doInBackground(Void... params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost;
		String phpResponse = "";
		
		if (Registry.getInstance().getLog() != null) {
			ArrayList<ArrayList<NameValuePair>> clicks = reg.getLog().getUnloggedClicks();
			ArrayList<ArrayList<NameValuePair>> choices = reg.getLog().getUnloggedChoices();
			ArrayList<ArrayList<NameValuePair>> reminders = reg.getLog().getUnloggedReminders();
			
			try {
				for (int i=0;i<clicks.size();i++) {
					httppost = new HttpPost("https://phitlab.soic.indiana.edu/llmw_folder/sync_clicks.php");
					httppost.setEntity(new UrlEncodedFormEntity(clicks.get(i)));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					phpResponse = httpclient.execute(httppost, responseHandler);
					System.out.println("Click upload Response : " + phpResponse);
					if (phpResponse.equals("Success")) 
						reg.getLog().logIt(clicks.get(i).get(0).getValue(), "click_log");
				}
				for (int i=0;i<choices.size();i++) {
					httppost = new HttpPost("https://phitlab.soic.indiana.edu/llmw_folder/sync_choices.php?format=json");
					httppost.setEntity(new UrlEncodedFormEntity(choices.get(i)));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					phpResponse = httpclient.execute(httppost, responseHandler);
					System.out.println("Choices upload Response : " + phpResponse); 
					if (phpResponse.equals("Success")) 
						reg.getLog().logIt(choices.get(i).get(0).getValue(), "choice_log");
					else {
						System.out.println("Output of choices is " + choices.get(i).get(0).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(1).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(2).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(3).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(4).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(5).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(6).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(7).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(8).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(9).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(10).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(11).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(12).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(13).getValue());
						System.out.println("Output of choices is " + choices.get(i).get(14).getValue());
					}
				}
				for (int i=0;i<reminders.size();i++) {
					httppost = new HttpPost("https://phitlab.soic.indiana.edu/llmw_folder/sync_reminders.php?format=json");
					httppost.setEntity(new UrlEncodedFormEntity(reminders.get(i)));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					phpResponse = httpclient.execute(httppost, responseHandler);
					System.out.println("Reminder upload Response : " + phpResponse); 
					System.out.println("reminder id just sent" + reminders.get(i).get(0).getValue());
					if (phpResponse.equals("Success")) {
						reg.getLog().logIt(reminders.get(i).get(0).getValue(), "reminder_log");
						System.out.println("reminder id just uploaded" + reminders.get(i).get(0).getValue());
					}
					else {
						System.out.println("Output of reminders is " + reminders.get(i).get(0).getValue());
						System.out.println("Output of reminders is " + reminders.get(i).get(1).getValue());
						System.out.println("Output of reminders is " + reminders.get(i).get(2).getValue());
						System.out.println("Output of reminders is " + reminders.get(i).get(3).getValue());
						System.out.println("Output of reminders is " + reminders.get(i).get(4).getValue());
					}
				}
				
				if (reg.getLog().getPasswordStatus()) {
					ArrayList<NameValuePair> password = reg.getLog().getPassword();
					httppost = new HttpPost("https://phitlab.soic.indiana.edu/llmw_folder/update_password.php?format=json");
					httppost.setEntity(new UrlEncodedFormEntity(password));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					phpResponse = httpclient.execute(httppost, responseHandler);
					System.out.println("Update password : " + phpResponse); 
					if(phpResponse.equals("Successful")) {
						reg.getLog().updatePasswordStatus();
					}
				}
				
				if (reg.getLog().getUploadStatus()) {
					ArrayList<String> reminderTimes = reg.getLog().getReminderTimes();
					ArrayList<NameValuePair> reminderList = new ArrayList<NameValuePair>();
					reminderList.add(new BasicNameValuePair("user_id",String.valueOf(reg.getUserId())));
					reminderList.add(new BasicNameValuePair("reminder1_begin",String.valueOf(reminderTimes.get(0))));
					reminderList.add(new BasicNameValuePair("reminder1_end",String.valueOf(reminderTimes.get(1))));
					reminderList.add(new BasicNameValuePair("reminder2_begin",String.valueOf(reminderTimes.get(2))));
					reminderList.add(new BasicNameValuePair("reminder2_end",String.valueOf(reminderTimes.get(3))));
					reminderList.add(new BasicNameValuePair("reminder_eod",String.valueOf(reminderTimes.get(4))));
					httppost = new HttpPost("https://phitlab.soic.indiana.edu/llmw_folder/update_reminders.php?format=json");
					httppost.setEntity(new UrlEncodedFormEntity(reminderList));
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					phpResponse = httpclient.execute(httppost, responseHandler);
					System.out.println("Update reminders : " + phpResponse); 
					if (phpResponse.equals("Success")) { 
						reg.getLog().clearUpload();
						
					}
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		return phpResponse;
	}

	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);
			if (n>0) out.append(new String(b, 0, n));
		}
		return out.toString();
	}

}
