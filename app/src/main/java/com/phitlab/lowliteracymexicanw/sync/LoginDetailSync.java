package com.phitlab.lowliteracymexicanw.sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.NameValuePair;
import com.phitlab.lowliteracymexicanw.logDB.UserDetail;
import com.phitlab.lowliteracymexicanw.objects.Registry;

import android.os.AsyncTask;
import android.util.Log;

public class LoginDetailSync extends AsyncTask<Void, Void, String>{

	//private final static String syncURL = "http://156.56.95.6/sync.php?format=json"; 
	private final static String syncURL = "https://phitlab.soic.indiana.edu/llmw_folder/user_info.php?format=json";
	private AsyncTaskCompletedListener listener;	

	public LoginDetailSync(AsyncTaskCompletedListener listener) {
		Log.d("Constructor DBsync","reached here");
		this.listener=listener;
	}


	@Override
	protected void onPostExecute(String results) {
	//Log.d("JSON response ", results);
		if (results!=null) {		
		try {
			//JSONObject jsonResponse = new JSONObject(results);
			//JSONObject userObj = jsonResponse.getJSONObject("users");
			JSONObject userObj = new JSONObject(results);
			String username = userObj.getString("username");
			int userID = Integer.parseInt(userObj.getString("user_id"));
			String password = userObj.getString("password");
			Log.d("LoginSyncDetails", "password from online database" + password);
			
			String regId = userObj.getString("reg_id");
			String reminder1_begin = userObj.getString("reminder1_begin");
			String reminder1_end = userObj.getString("reminder1_end");
			String reminder2_begin = userObj.getString("reminder2_begin");
			String reminder2_end = userObj.getString("reminder2_end");
			String eod_reminder = userObj.getString("eod_reminder");
			String recruitment_date =userObj.getString("recruitment_date");
			
			UserDetail userDetail = new UserDetail(userID, username, password, regId, reminder1_begin,
						reminder1_end, reminder2_begin, reminder2_end, eod_reminder, recruitment_date);
		    Registry.getInstance().getLog().logUserDetails(userDetail);
			//listener.onAsyncTaskCompleted(userDetail);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	listener.onAsyncTaskCompleted();
	System.out.println("Arrived here");
}


	@Override
	protected String doInBackground(Void... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost;
		String text = null;
		HttpResponse httpResponse;
		  
		ArrayList<NameValuePair> userDetail = new ArrayList<NameValuePair>();
		userDetail.add(new BasicNameValuePair("username", Registry.getInstance().getUserName()));
		try {
			httpPost = new HttpPost(syncURL);
			httpPost.setEntity(new UrlEncodedFormEntity(userDetail));
			httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			text = getASCIIContentFromEntity(entity);
			System.out.println("User Detail Updated : " + text);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
//		HttpContext localContext = new BasicHttpContext();
//		Log.d("request url :: ", syncURL);
//		HttpGet httpGet = new HttpGet(syncURL);
//		String text = null;
//		try {
//			HttpResponse response = httpClient.execute(httpGet, localContext);
//			HttpEntity entity = response.getEntity();
//			text = getASCIIContentFromEntity(entity);
//		} catch (Exception e) {
//			return e.getLocalizedMessage();
//		}
		return text;
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


