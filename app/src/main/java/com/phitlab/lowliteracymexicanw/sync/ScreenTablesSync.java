package com.phitlab.lowliteracymexicanw.sync;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.phitlab.lowliteracymexicanw.logDB.ScreenDetail;



import android.os.AsyncTask;
import android.util.Log;

public class ScreenTablesSync extends AsyncTask<Void, Void, String>{

	//private final static String syncURL = "http://156.56.95.6/sync.php?format=json"; 
	private final static String syncURL = "https://phitlab.soic.indiana.edu/llmw_folder/sync_mine.php?format=json";
	private AsyncTaskCompletedListener listener;	

	public ScreenTablesSync(AsyncTaskCompletedListener listener) {
		Log.d("Constructor DBsync","reached here");
		this.listener=listener;
	}


	@Override
	protected void onPostExecute(String results) {
	//Log.d("JSON response ", results);
		if (results!=null) {		
		try {
			JSONObject jsonResponse = new JSONObject(results);
			JSONArray array = jsonResponse.getJSONArray("screens");
			//JSONObject obj = json.getJSONObject("screen");
			Log.d("JSON screen", array.toString());
			Log.d("single arr", array.get(0).toString());

			ArrayList<ScreenDetail> screenDetailAL = new ArrayList<ScreenDetail>();
			for(int i=0; i<array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				JSONObject screenObj = obj.getJSONObject("screen");

				ArrayList<String> gridArray = new ArrayList<String>();
				for(int j=1; j<=9; j++) {
					String gridname = "grid"+j;
					gridArray.add(screenObj.getString(gridname));
				}

				String screenName = screenObj.getString("screen_name");
				int screenID = Integer.parseInt(screenObj.getString("screen_id"));

				Log.d("screen name", screenName);

				ScreenDetail screenDetail = new ScreenDetail(screenID, screenName, gridArray);
				screenDetailAL.add(screenDetail);

			}
			 listener.onAsyncTaskCompleted(screenDetailAL);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


	@Override
	protected String doInBackground(Void... params) {	
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		Log.d("request url :: ", syncURL);
		HttpGet httpGet = new HttpGet(syncURL);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
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
