package com.phitlab.lowliteracymexicanw.logDB;

//original imports below
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.phitlab.lowliteracymexicanw.objects.Registry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class LogDBAdapter {

	private LogDBHelper logDbHelper;
	private Context context;
	private Registry registry;
	
	public LogDBAdapter(Context context)
	{
		this.context = context;
		registry = Registry.getInstance();
		logDbHelper = new LogDBHelper(context);
		logDbHelper.onUpgrade(this.logDbHelper.getWritableDatabase(), 1, 1);
	}

	public int getMaxID(String table_name) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		int maxID = 0;
		
		Cursor cursor = null;
		try {
			cursor = db.query(table_name, new String [] {"MAX(rec_id)"}, null, null, null, null, null);
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			maxID = cursor.getInt(0);
		}
		cursor.close();
		//db.close();
		Log.d("LogDBAdapter", table_name + " maxID is " + maxID);
		return maxID;
	}

	public void logClick(int value, String column_name)
	{	
		String maxID = Integer.toString(getMaxID("click_log"));
		Log.d("LogDBAdpater", "maxid " + maxID);
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(column_name, value);
		
		try
		{
			db.update("click_log", values, "rec_id = ?", new String[] {maxID});
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		//db.close();
	}

	public void logTime(long value, String column_name, String table_name)
	{
		String time = Integer.toString(getMaxID(table_name));
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(column_name, value);
		
		try
		{
			db.update(table_name, values, "rec_id = ?", new String[] {time});
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		//db.close();
	}

	/* not done */
	
	public void logNewClick(int screen_id)
	{
		int maxID = getMaxID("click_log") + 1;
		SQLiteDatabase db = logDbHelper.getWritableDatabase();		
		ContentValues values = new ContentValues();
		
		Registry reg = Registry.getInstance();
		values.put("rec_id", maxID);
		values.put("user_id", reg.getUserId());
		values.put("logged", 0);
		values.put("title_sound", 0);
		values.put("enter_time", getTime());
		values.put("screen_id", screen_id);
		
		Log.d("logDBAdapter", "logNewClick");
		try
		{
			db.insertOrThrow("click_log", null, values);
			Log.e("logNewClick","New Click logged");
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
			Log.e("logNewClick","Error logging new click");
		}
		//db.close();
		//Log.d("logDBAdapter", "logNewClick" + getMaxID("click_log"));
	}
	
	public ArrayList<String> getReminderTimes() {
  
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		String[] reminder_names = new String[] {"reminder1_begin", "reminder1_end", "reminder2_begin", "reminder2_end", "eod_reminder"};
		Log.d("LogDBAdapter", "getReminderTimes");
		Log.d("LogDBAdapter", ""+Registry.getInstance().getUserId());
		Cursor cursor = null;
		try {
			//cursor = db.query("user", null, "user_id = ?", new String[] {"" +Registry.getInstance().getUserId()}, null, null, null);	
			cursor = db.query("user", null, null, null, null, null, null);	
			
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

		ArrayList<String> timeList = new ArrayList<String>();
		Log.d("LogDBAdapter", "getReminderTimes");
		if (cursor.moveToFirst()) {
			cursor.moveToFirst(); 
			Log.d("LogDBAdapter", "getReminderTimes33" + reminder_names.length);
			//Log.d("LogDBAdapter", "user id is " + cursor.getString(cursor.getColumnIndex("user_id")));
			for (int i=0; i<reminder_names.length; i++){
				timeList.add( cursor.getString( cursor.getColumnIndex(reminder_names[i])));
			}
			Log.d("LogDBAdapter", "getReminderTimes " + timeList.size());		
			cursor.close();
		}
		//db.close();
		return timeList;
		
	}
	
	public ArrayList<Integer> getValue(String table_name, String[] column_name) {
		  
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		
		Cursor cursor = null;
		try
		{
			cursor = db.query(table_name, null, null, column_name, null, null, null, null);	
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

		cursor.moveToFirst();
		ArrayList<Integer> returnValues = new ArrayList<Integer>();
		
		for (int i=0; i<column_name.length; i++){
			returnValues.add( i, cursor.getInt( cursor.getColumnIndex(column_name[i]) ) );
		}

		if (cursor != null)
			cursor.close();
		//db.close();
		
		return returnValues;
	}
	
	public void logged(int[] record_id, String table_name)
	{
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("logged", 1);
		
		try
		{
			for(int i = 0; i<record_id.length; i++)
				db.update(table_name, values, "rec_id = ?",  new String[] {Integer.toString(record_id[i])});
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		//db.close();
	}
	
	public void logIt(String record_id, String table_name)
	{
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("logged", 1);
		
		try {
			db.update(table_name, values, "rec_id = ?",  new String[] {record_id});
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		//db.close();
	}

	public ArrayList<ArrayList<NameValuePair>> getUnloggedClicks()
	{
		SQLiteDatabase db = logDbHelper.getReadableDatabase();
		long enter_time;
		long exit_time;
		long screen_duration;
		
		ArrayList<NameValuePair> clickList = new ArrayList<NameValuePair>();
		ArrayList<ArrayList<NameValuePair>> clicks = new ArrayList<ArrayList<NameValuePair>>();
		Cursor cursor = null;
		try {
			if (db.isOpen()) {
				cursor = db.query("click_log", null,  "logged = ?", new String[] {"0"}, null, null, null, null);	
				//if (cursor.moveToFirst()) {
					//cursor.move(-1);
					//cursor.moveToFirst();	
					while (cursor.moveToNext()) {
						clickList = new ArrayList<NameValuePair>();
						clickList.add(new BasicNameValuePair("rec_id", cursor.getString( cursor.getColumnIndex("rec_id"))));
						clickList.add(new BasicNameValuePair("user_id", cursor.getString( cursor.getColumnIndex("user_id"))));
						clickList.add(new BasicNameValuePair("screen_id",cursor.getString( cursor.getColumnIndex("screen_id"))));
						clickList.add(new BasicNameValuePair("choice_id",cursor.getString( cursor.getColumnIndex("choice_id"))));
						clickList.add(new BasicNameValuePair("title_sound",cursor.getString( cursor.getColumnIndex("title_sound"))));
						clickList.add(new BasicNameValuePair("yes_icon",cursor.getString( cursor.getColumnIndex("yes_icon"))));
						clickList.add(new BasicNameValuePair("no_icon", cursor.getString( cursor.getColumnIndex("no_icon"))));
						clickList.add(new BasicNameValuePair("plus_icon",cursor.getString( cursor.getColumnIndex("plus_icon"))));
						clickList.add(new BasicNameValuePair("minus_icon",cursor.getString( cursor.getColumnIndex("minus_icon"))));
						clickList.add(new BasicNameValuePair("icon1_sound",cursor.getString( cursor.getColumnIndex("icon1_sound"))));
						clickList.add(new BasicNameValuePair("icon2_sound",cursor.getString( cursor.getColumnIndex("icon2_sound"))));
						clickList.add(new BasicNameValuePair("icon3_sound",cursor.getString( cursor.getColumnIndex("icon3_sound"))));
						clickList.add(new BasicNameValuePair("icon4_sound",cursor.getString( cursor.getColumnIndex("icon4_sound"))));
						clickList.add(new BasicNameValuePair("icon5_sound",cursor.getString( cursor.getColumnIndex("icon5_sound"))));
						clickList.add(new BasicNameValuePair("icon6_sound",cursor.getString( cursor.getColumnIndex("icon6_sound"))));
						clickList.add(new BasicNameValuePair("icon7_sound",cursor.getString( cursor.getColumnIndex("icon7_sound"))));
						clickList.add(new BasicNameValuePair("icon8_sound",cursor.getString( cursor.getColumnIndex("icon8_sound"))));
						clickList.add(new BasicNameValuePair("icon9_sound",cursor.getString( cursor.getColumnIndex("icon9_sound"))));
						clickList.add(new BasicNameValuePair("icon10_sound",cursor.getString( cursor.getColumnIndex("icon10_sound"))));
						clickList.add(new BasicNameValuePair("icon11_sound",cursor.getString( cursor.getColumnIndex("icon11_sound"))));
						clickList.add(new BasicNameValuePair("icon12_sound",cursor.getString( cursor.getColumnIndex("icon12_sound"))));
						clickList.add(new BasicNameValuePair("icon1",cursor.getString( cursor.getColumnIndex("icon1"))));
						clickList.add(new BasicNameValuePair("icon2",cursor.getString( cursor.getColumnIndex("icon2"))));
						clickList.add(new BasicNameValuePair("icon3",cursor.getString( cursor.getColumnIndex("icon3"))));
						clickList.add(new BasicNameValuePair("icon4",cursor.getString( cursor.getColumnIndex("icon4"))));
						clickList.add(new BasicNameValuePair("icon5",cursor.getString( cursor.getColumnIndex("icon5"))));
						clickList.add(new BasicNameValuePair("icon6",cursor.getString( cursor.getColumnIndex("icon6"))));
						clickList.add(new BasicNameValuePair("icon7",cursor.getString( cursor.getColumnIndex("icon7"))));
						clickList.add(new BasicNameValuePair("icon8",cursor.getString( cursor.getColumnIndex("icon8"))));
						clickList.add(new BasicNameValuePair("icon9",cursor.getString( cursor.getColumnIndex("icon9"))));
						clickList.add(new BasicNameValuePair("icon10",cursor.getString( cursor.getColumnIndex("icon10"))));
						clickList.add(new BasicNameValuePair("icon11",cursor.getString( cursor.getColumnIndex("icon11"))));
						clickList.add(new BasicNameValuePair("icon12",cursor.getString( cursor.getColumnIndex("icon12"))));
						clickList.add(new BasicNameValuePair("camera_icon",cursor.getString( cursor.getColumnIndex("camera_icon"))));
						clickList.add(new BasicNameValuePair("record_icon",cursor.getString( cursor.getColumnIndex("record_icon"))));
						clickList.add(new BasicNameValuePair("stop_icon",cursor.getString( cursor.getColumnIndex("stop_icon"))));
						clickList.add(new BasicNameValuePair("play_icon",cursor.getString( cursor.getColumnIndex("play_icon"))));
						clickList.add(new BasicNameValuePair("pause_icon",cursor.getString( cursor.getColumnIndex("pause_icon"))));
						clickList.add(new BasicNameValuePair("next_icon",cursor.getString( cursor.getColumnIndex("next_icon"))));
						clickList.add(new BasicNameValuePair("back_key",cursor.getString( cursor.getColumnIndex("back_key"))));
						enter_time = cursor.getLong( cursor.getColumnIndex("enter_time"));
						exit_time = cursor.getLong( cursor.getColumnIndex("exit_time"));
						clickList.add(new BasicNameValuePair("enter_time", getDate(enter_time, "MM/dd/yyyy HH:mm")));
						clickList.add(new BasicNameValuePair("exit_time", getDate(exit_time, "MM/dd/yyyy HH:mm")));
						screen_duration =  exit_time - enter_time;
						clickList.add(new BasicNameValuePair("screen_duration", Long.toString(screen_duration)));
						clicks.add(clickList);
						//cursor.moveToNext();
					}
					cursor.close();
				}
			//}
		} catch (SQLException sqEx){
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
			
		//db.close();
		return clicks;
	}
	
	public ArrayList<ArrayList<NameValuePair>> getUnloggedChoices()
	{
		SQLiteDatabase db = logDbHelper.getReadableDatabase();
		long choice_time;
		ArrayList<NameValuePair> choiceList = new ArrayList<NameValuePair>();
		ArrayList<ArrayList<NameValuePair>> choices = new ArrayList<ArrayList<NameValuePair>>();
		String imageName;
		String audioName;
		String audioFile;
		
		Cursor cursor = null;
		try {
			if (db.isOpen()) {
				//if (cursor.moveToFirst()) {
				cursor = db.query("choice_log", null,  "logged = ?", new String[] {"0"}, null, null, null, null);
					cursor.moveToFirst();	
//					while (!cursor.isAfterLast()) {
//				if(cursor != null) {
//					cursor.move(-1);
					while (!cursor.isAfterLast()) {
						choice_time = cursor.getLong( cursor.getColumnIndex("choice_time"));
						choiceList = new ArrayList<NameValuePair>();
						choiceList.add(new BasicNameValuePair("rec_id",cursor.getString( cursor.getColumnIndex("rec_id"))));
						choiceList.add(new BasicNameValuePair("user_id", cursor.getString( cursor.getColumnIndex("user_id"))));
						choiceList.add(new BasicNameValuePair("choice_time", getDate(choice_time, "MM/dd/yyyy HH:mm")));
						choiceList.add(new BasicNameValuePair("choice_time_label", getDate(choice_time, "HH:mm")));
						choiceList.add(new BasicNameValuePair("behavior_id",cursor.getString( cursor.getColumnIndex("behavior_id"))));
						choiceList.add(new BasicNameValuePair("behavior_duration", cursor.getString( cursor.getColumnIndex("behavior_duration"))));
						choiceList.add(new BasicNameValuePair("cigarette_count",cursor.getString( cursor.getColumnIndex("cigarette_count"))));
//						choiceList.add(new BasicNameValuePair("partial_cigarette",cursor.getString( cursor.getColumnIndex("partial_cigarette"))));
//						choiceList.add(new BasicNameValuePair("full_cigarette",cursor.getString( cursor.getColumnIndex("full_cigarette"))));
						choiceList.add(new BasicNameValuePair("alcohol_count",cursor.getString( cursor.getColumnIndex("alcohol_count"))));
						choiceList.add(new BasicNameValuePair("embarrass",cursor.getString( cursor.getColumnIndex("embarrass"))));
						choiceList.add(new BasicNameValuePair("loss_of_control",cursor.getString( cursor.getColumnIndex("loss_of_control"))));
						choiceList.add(new BasicNameValuePair("context_people",cursor.getString( cursor.getColumnIndex("context_people"))));
						choiceList.add(new BasicNameValuePair("product_chosen",cursor.getString( cursor.getColumnIndex("product_chosen"))));			
						
						imageName = cursor.getString( cursor.getColumnIndex("photo_name"));
						audioName = cursor.getString( cursor.getColumnIndex("voice_recording"));
						audioFile = cursor.getString( cursor.getColumnIndex("voice_mp4"));
						
						choiceList.add(new BasicNameValuePair("photo_name",imageName));			
						choiceList.add(new BasicNameValuePair("voice_recording", audioName));	
						choiceList.add(new BasicNameValuePair("voice_mp4", audioFile));	
						
						if(imageName != null || imageName == "") {
							try {							
								File storedImage = new File(Environment.getExternalStorageDirectory().getPath(), "LEADPhotos/"+imageName);
								Bitmap bm = decodeFile(storedImage);
								 // create a matrix object
						        Matrix matrix = new Matrix();
						        matrix.postRotate(90); // anti-clockwise by 90 degrees
						         
						        // create a new bitmap from the original using the matrix to transform the result
						        Bitmap rotatedBitmap = Bitmap.createBitmap(bm , 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
						        
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								System.out.println("image file name " + storedImage.getAbsolutePath());
								rotatedBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos); 
						        
								//bm.compress(Bitmap.CompressFormat.JPEG,40,baos); 
						        byte [] byteArray = baos.toByteArray();
						        String encodedImage =Base64.encodeToString(byteArray,Base64.DEFAULT);
					            choiceList.add(new BasicNameValuePair("image",encodedImage));
					            rotatedBitmap.recycle();
					            rotatedBitmap = null;
					            //bm.recycle();
					            //bm = null;
					            baos.flush();
								baos.close();
								System.out.println("Uploaded an image name");
							} catch (IOException e) {
								 System.out.println("bytearrayoutputstream error");
								e.printStackTrace();
							}
						} 
						else {
							choiceList.add(new BasicNameValuePair("image",imageName));
						}
						if (audioName != null || audioName == "") {	
							
							ByteArrayOutputStream baos = new ByteArrayOutputStream();						
				            
							try {
								File audioStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), "LEADVoices");	
								System.out.println("The name of the audio file " + audioFile);
								//String pathToFile = audioStorageDir.getAbsolutePath() + File.separator + audiofile;														
								//new Upload().execute(new String[] {pathToFile});
								InputStream is = new FileInputStream(audioStorageDir.getAbsolutePath() + File.separator + audioFile);
							    int bytesAvailable = is.available();
							    int maxBufferSize = 1*1024*1024;
							    byte[] buffer = new byte[bytesAvailable];
							    int bytesRead = is.read(buffer, 0, bytesAvailable);
	
							    while (bytesRead > 0) {
							        baos.write(buffer, 0, bytesAvailable);
							        bytesAvailable = is.available();
							        bytesAvailable = Math.min(bytesAvailable, maxBufferSize);
							        bytesRead = is.read(buffer, 0, bytesAvailable);
							    }
							    byte[] bytes = baos.toByteArray();
							    String encodedAudio = Base64.encodeToString(bytes, Base64.DEFAULT);  
							    choiceList.add(new BasicNameValuePair("audio",encodedAudio));
							    is.close();
							    baos.flush();
							    baos.close();
							    System.out.println("Uploaded an audio file without errors");
				            } catch (FileNotFoundException e) {
								e.printStackTrace();
								System.out.println("bytearrayoutputstream error1" +audioFile);
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println("bytearrayoutputstream error2");
							}							
						}
						else {
							choiceList.add(new BasicNameValuePair("audio",audioName));
						}
						choices.add(choiceList);
						cursor.moveToNext();
					}
					cursor.close();
				}
			//}
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		//db.close();
		return choices;
	}
	 
	private Bitmap decodeFile(File f){
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	        //The new size we want to scale to
	        final int REQUIRED_SIZE=125;

	        //Find the correct scale value. It should be the power of 2.
	        int scale=1;
	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
	            scale*=2;

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;
	}

	public ArrayList<ArrayList<NameValuePair>> getUnloggedReminders() {
		SQLiteDatabase db = logDbHelper.getReadableDatabase();
		String reminder_type;
		long reminder_time;
		long response_time;
		ArrayList<NameValuePair> reminderList = new ArrayList<NameValuePair>();
		ArrayList<ArrayList<NameValuePair>> reminders = new ArrayList<ArrayList<NameValuePair>>();
		
		Cursor cursor = null;

		try
		{
			if (db.isOpen()) {
				cursor = db.query("reminder_log", null,  "logged = ?", new String[] {"0"}, null, null, null, null);	
//				if (cursor.getCount()>0) {
//					cursor.moveToFirst();	
//					while (!cursor.isAfterLast()) {
				//if (cursor.moveToFirst()) {
					//cursor.move(-1);
					while (cursor.moveToNext()) {
						reminderList = new ArrayList<NameValuePair>();
						reminder_time = cursor.getLong( cursor.getColumnIndex("reminder_time"));
						reminder_type = cursor.getString( cursor.getColumnIndex("reminder_type"));
						response_time = cursor.getLong( cursor.getColumnIndex("response_time"));
						reminderList.add(new BasicNameValuePair("rec_id",cursor.getString( cursor.getColumnIndex("rec_id"))));
						reminderList.add(new BasicNameValuePair("reminder_time",getDate(reminder_time, "MM/dd/yyyy HH:mm")));
						reminderList.add(new BasicNameValuePair("response", cursor.getString( cursor.getColumnIndex("response"))));
						reminderList.add(new BasicNameValuePair("reminder_type", reminder_type));
						reminderList.add(new BasicNameValuePair("response_time", getDate(response_time, "MM/dd/yyyy HH:mm")));
						reminderList.add(new BasicNameValuePair("user_id", cursor.getString( cursor.getColumnIndex("user_id"))));
						reminders.add(reminderList);
						//cursor.moveToNext();
					}
					cursor.close();
				//}
			}
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		//if (cursor != null)
		//	cursor.close();

		//db.close();
		return reminders;	
	}
	
	public ArrayList<Integer> getUnlogged(String table_name) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		
		Cursor cursor = null;
		try
		{
			cursor = db.query(table_name, null,  "logged = ?", new String[] {"0"}, null, null, null, null);	
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

		cursor.moveToFirst();
		ArrayList<Integer> returnValues = new ArrayList<Integer>();
		
		while (cursor.moveToNext()) {
			returnValues.add(cursor.getInt( cursor.getColumnIndex("rec_id") ) );
		}

		if (cursor != null)
			cursor.close();
		//db.close();
		
		return returnValues;
	}
	
	public void logRecords() {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		Cursor choiceCursor = null;
		Cursor clickCursor = null;
		Cursor reminderCursor = null;
		values.put("logged", 1);
		
		try {
			choiceCursor = db.query("choice_log", null,  "logged = ?", new String[] {"0"}, null, null, null, null);	
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		while (choiceCursor.moveToNext()) {
			db.update("choice_log", values, "WHERE rec_id = ?", new String[] {choiceCursor.getString(choiceCursor.getColumnIndex("rec_id"))});
		}

		try {
			clickCursor = db.query("click_log", null,  "logged = ?", new String[] {"0"}, null, null, null, null);	
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		while (clickCursor.moveToNext()) {
			db.update("click_log", values, "WHERE rec_id = ?", new String[] {clickCursor.getString(clickCursor.getColumnIndex("rec_id"))});
		}
		
		try {
			reminderCursor = db.query("reminder_log", null,  "logged = ?", new String[] {"0"}, null, null, null, null);	
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		while (reminderCursor.moveToNext()) {
			db.update("reminder_log", values, "WHERE rec_id = ?", new String[] {reminderCursor.getString(reminderCursor.getColumnIndex("rec_id"))});
		}

		//db.close();		
	}
	
	private long getTime() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public static String getDate(long milliSeconds, String dateFormat) {
	    // Create a DateFormatter object for displaying date in specified format.
	    DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    return formatter.format(calendar.getTime());
	}

	public void logUserDetails(UserDetail userDetail) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		
		values.put("user_id", userDetail.getUserID());
		values.put("username", userDetail.getUsername());
		values.put("password", userDetail.getPassword());
		values.put("recruitment_date", userDetail.getRecruitment_Date());
		values.put("reminder1_begin", userDetail.getReminder1_begin());
		values.put("reminder1_end", userDetail.getReminder1_end());
		values.put("reminder2_begin", userDetail.getReminder2_begin());
		values.put("reminder2_end", userDetail.getReminder2_end());
		values.put("eod_reminder", userDetail.getEod_reminder());
		values.put("password_update", 0);
		values.put("update_reminder1", 0);
		values.put("update_reminder2", 0);
		values.put("update_eod", 0);
		values.put("reminder_upload", 0);
		
		Log.d("logUserDetail userid",""+userDetail.getUserID());
		Log.d("logUserDetail username",""+userDetail.getUsername());
		registry.setUserId(userDetail.getUserID());
		try {
			db.insertOrThrow("user", null, values );
			Log.d("LogDBAdapter", "User Detail Successfully inserted");
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "User Detail Could not insert");
		}
		//db.close();
		
	}

	public ArrayList<NameValuePair> getPassword() {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ArrayList<NameValuePair> password = new ArrayList<NameValuePair>(); 
		Cursor cursor = null;
		try {
			cursor = db.query("user", null, "user_id = ?", new String[] {"" +Registry.getInstance().getUserId()}, null, null, null);	
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

		if (cursor.moveToFirst()) {
			String stored_password = cursor.getString( cursor.getColumnIndex("password"));
			String app = "";
			int len = stored_password.length();
			int diff = 6 - len;
			for (int i=0; i<diff; i++)
				app = app +"0";
			String actual_password = app + stored_password;
			//cursor.moveToFirst(); 
			password.add(new BasicNameValuePair("user_id", "" +Registry.getInstance().getUserId()));
			password.add(new BasicNameValuePair("password", ""+actual_password));
			System.out.println("password stored in local database "+ actual_password);
		}	
		cursor.close();
		//db.close();
		
		return password;
	}
	
	public void updatePassword(String password) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		Log.d("LogDBAdapter", "password is in LogDBAdapter " + password);
		values.put("password", password);
		values.put("password_update", 1);
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted"+password);
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.e("LogDBAdapter", "Could not insert"+password);
		}
		//db.close();	
	}
	
	public void updatePasswordStatus() {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		values.put("password_update", 0);
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not insert");
		}
		//db.close();	
	}
	
	public String verifyPassword(String password) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query("user", null,  "user_id = ?", new String[]{""+registry.getUserId()}, null, null, null, null);	
			if (cursor.moveToFirst()) {
				if (password.equals(cursor.getString(cursor.getColumnIndex("password")))) {
					cursor.close();
					return "User Found";
				}
				cursor.close();
			}
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not query");
		}
		//db.close();	
		return "Incorrect Password";
	}
	
	public boolean getPasswordStatus() {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {
			if (db.isOpen()) {
			cursor = db.query("user", null,  "user_id = ?", new String[]{""+registry.getUserId()}, null, null, null, null);	
			if (cursor.moveToFirst()) {
				cursor.moveToFirst();	
				int changed = cursor.getInt(cursor.getColumnIndex("password_update"));
				if (changed == 0) {
					cursor.close();
					return false;
				}
				else {
					cursor.close();
					return true;
				}
			}
			}
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not query");
		}
		return false;
	}
	
	public int getUserID() {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		
		Cursor cursor = null;
		int user_id = 0;
		
		try
		{
			cursor = db.query("user", null,  "username = ?", new String[]{registry.getUserName()}, null, null, null, null);		
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

		cursor.moveToFirst();
		
		//while (cursor.moveToNext()) {
		user_id = cursor.getInt( cursor.getColumnIndex("user_id"));
		//}
		//cursor.moveToNext();
		System.out.println("username " + registry.getUserName());
		if (cursor != null)
			cursor.close();
		//db.close();
		
		return user_id;
	}

	public void logFinalClick(String column_name, int choice) {
		
		int maxID = getMaxID("choice_log");
		String maxClick = Integer.toString(getMaxID("click_log"));
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(column_name, choice);
		values.put("exit_time", getTime());
		values.put("choice_id", maxID);
		
		try
		{
			db.update("click_log", values, "rec_id = ?", new String[] {maxClick});
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		//db.close();
	}
	
	public void logNewChoice(String column_name, int choice) {
		int maxID = getMaxID("choice_log") + 1;
		Log.e("logNewChoice","new choice record id should be " + maxID);
		
		
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("rec_id", maxID);
		values.put("user_id", registry.getUserId());
		values.put("choice_time", getTime());
		values.put("logged", 0);
		values.put(column_name, choice);
		
		try
		{
			db.insertOrThrow("choice_log", null, values);
		} catch (SQLException sqEx)
		{
			Log.e("logNewChoice","Error logging new choice");
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		Log.e("logNewChoice","new choice logged, record id is " + Integer.toString(getMaxID("choice_log")));
	}
	
	public void logChoice(String column_name, int choice) {
		
		String maxID = Integer.toString(getMaxID("choice_log"));
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(column_name, choice);
				
		try
		{
			db.update("choice_log", values, "rec_id = ?", new String[] {maxID});
			Log.e("logChoice", "Choice log has been updated" + maxID);
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
			Log.e("logChoice", "Choice log was not updated" + maxID);
		}	
	}

	public void logChoice(String column_name, String column_value) {
		
		String maxID = Integer.toString(getMaxID("choice_log"));
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(column_name, column_value);
		
		try {
			db.update("choice_log", values, "rec_id = ?", new String[] {maxID});
			Log.e("logChoice", "Choice log has been updated" + maxID);
			Log.e("logChoice", "column value is " + column_value);
			Log.e("logChoice", "column name is " + column_name);
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
			Log.e("logChoice", "Choice log was not updated" + maxID);
		}
	}
	
	public void completeChoice() {
		
		String maxID = Integer.toString(getMaxID("choice_log"));
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("choice_time_label", getTime() );
		
		try {
			db.update("choice_log", values, "rec_id = ?", new String[] {maxID});
			Log.e("logChoice", "Choice log has been updated" + maxID);
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
			Log.e("logChoice", "Choice log was not updated" + maxID);
		}
	}

	public int logNewReminder(String column_name, int value) {
		
		int maxID = getMaxID("reminder_log") + 1;
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("rec_id", maxID);
		values.put("user_id", registry.getUserId());
		values.put("reminder_time", getTime());
		values.put(column_name, value);
		values.put("logged", -1);
		values.put("response", 0);	
		
		try
		{
			db.insertOrThrow("reminder_log", null, values);
		} catch (SQLException sqEx)
		{
			Log.e("logNewReminder","Error logging new reminder");
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

        return maxID;
	}
	
	public void completeReminderLog(int response, int reminder_id) {
		String maxID = Integer.toString(getMaxID("reminder_log"));
		Log.e("CompleteReminderLog", "maxID is " + maxID);
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("response", response);
		values.put("response_time", getTime());	
		values.put("logged", 0);
		
		try
		{
			db.update("reminder_log", values, "rec_id = ?", new String[] {""+reminder_id});
			Log.e("CompleteReminderLog", "record number updated successfully " + maxID);
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
	}

	private class LogDBHelper extends SQLiteOpenHelper {
		private static final String DB_NAME = "llmw_log";
		private static final int DATABASE_VERSION = 1;
	    private final String DB_PATH; 
	    private SQLiteDatabase myDataBase; 
	    private final Context myContext;
		
		public LogDBHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
			this.myContext = context;
			
			if(myContext.getFilesDir() != null) 
				DB_PATH =  myContext.getFilesDir().getPath();  
			else
				DB_PATH = "";
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			myDataBase =db;
			if(!checkDataBase()) {
				createTables(db);
				Log.d("UILogger", "createTables");
			}
		}
	
		private void createTables(SQLiteDatabase db) {
			
//			db.execSQL("DROP table IF EXISTS SCREEN_LOG");
//			db.execSQL("DROP table IF EXISTS choice_log");
//			db.execSQL("DROP table IF EXISTS click_log");
//			db.execSQL("DROP table IF EXISTS reminder_log");
//			db.execSQL("DROP table IF EXISTS user");
			
			String sql = String.format("create table  if not exists %s " + 
					"(%s int primary key, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
					"SCREEN_LOG","PRIMARY_KEY","SCREEN_NAME","GRID_1","GRID_2","GRID_3","GRID_4","GRID_5","GRID_6", "GRID_7","GRID_8","GRID_9");
			db.execSQL(sql);
			
			sql = String.format("create table if not exists %s " + 
					"(%s int primary key, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, " +
					"%s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, " +
					"%s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, %s int, " +
					"%s long, %s long, %s int )" , 
					"click_log", "rec_id", "choice_id", "user_id", "screen_id", "title_sound", "yes_icon", "no_icon", "plus_icon",
					"minus_icon", "icon1_sound", "icon2_sound", "icon3_sound", "icon4_sound", "icon5_sound", "icon6_sound", "icon7_sound", 
					"icon8_sound", "icon9_sound", "icon10_sound", "icon11_sound", "icon12_sound", "icon1", "icon2", "icon3", "icon4", 
					"icon5", "icon6","icon7","icon8","icon9","icon10","icon11","icon12", "camera_icon","record_icon", "stop_icon", "play_icon", 
					"pause_icon", "next_icon", "back_key","enter_time", "exit_time", "logged");
			db.execSQL(sql);
			
//			sql = String.format("create table if not exists %s " + 
//					"(%s int primary key, %s, %s int, %s int, %s int, %s, %s int, %s int, %s int, %s int, %s int, %s int, %s, %s,%s, %s int)",
//					"choice_log", "rec_id", "choice_time", "user_id", "behavior_id", "behavior_duration", "partial_cigarette", "full_cigarette", "alcohol_count", 
//					"embarrass", "loss_of_control", "context_people", "product_chosen", "photo_name", "voice_recording", "voice_mp4","logged");
			sql = String.format("create table if not exists %s " + 
					"(%s int primary key, %s, %s int, %s int, %s int, %s, %s int, %s int, %s int, %s int, %s int, %s, %s,%s, %s int)",
					"choice_log", "rec_id", "choice_time", "user_id", "behavior_id", "behavior_duration", "cigarette_count", "alcohol_count", 
					"embarrass", "loss_of_control", "context_people", "product_chosen", "photo_name", "voice_recording", "voice_mp4","logged");
			db.execSQL(sql);
		
	        sql = String.format("create table if not exists %s " + 
					"(%s int primary key, %s int, %s, %s int, %s int, %s, %s int)",
					"reminder_log", "rec_id", "user_id", "reminder_time", "reminder_type", "response","response_time", "logged");
			db.execSQL(sql);
			
			sql = String.format("create table  if not exists %s " + 
					"(%s int, %s, %s, %s, %s, %s, %s, %s, %s, %s long, %s long, %s long, %s int, %s int, %s int, %s int, %s int)",
					"user", "user_id", "username", "password", "recruitment_date", "reminder1_begin", "reminder1_end", "reminder2_begin", 
					"reminder2_end", "eod_reminder", "reminder_1", "reminder_2", "old_eod_reminder", "password_update",
					"update_reminder1", "update_reminder2", "update_eod", "reminder_upload");
			db.execSQL(sql);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {

			if(oldVersion<newVersion) {
				db.execSQL("DROP table IF EXISTS SCREEN_LOG");
				db.execSQL("DROP table IF EXISTS choice_log");
				db.execSQL("DROP table IF EXISTS click_log");
				db.execSQL("DROP table IF EXISTS reminder_log");
				db.execSQL("DROP table IF EXISTS user");
				createTables(db);
			}
			
		}
	 
	    @Override
		public synchronized void close() {
	    	    if(myDataBase != null)
	    		    myDataBase.close();
	    	    super.close();
		}
	    
	    private boolean checkDataBase() {
	        SQLiteDatabase checkDB = null;
	        try {
	            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
	                    SQLiteDatabase.OPEN_READONLY);
	            checkDB.close();
	            System.out.println("checkDatabase successful");
	        } catch (SQLiteException e) {
	            System.out.println("checkDatabase unsuccessful");
		    }
	        return checkDB != null ? true : false;
	    }

	}

	public void addRegID(String regID) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		Log.d("LogDBAdapter", "password is " + regID);
		values.put("regID", regID);
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not insert");
		}
		//db.close();	
		
	}
	
	private class Upload extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            //Toast.makeText(activity, "Start upload...", Toast.LENGTH_SHORT).show();
        }

        @SuppressWarnings("deprecation")
		@Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String existingFileName = params[0];
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1*1024*1024;
            String urlString = "https://phitlab.soic.indiana.edu/llmw_folder/handle_upload.php";

            try{
                    //------------------ CLIENT REQUEST
                    FileInputStream fileInputStream = new FileInputStream(new File(existingFileName) );
                    // open a URL connection to the Servlet
                    URL url = new URL(urlString);
                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    // Allow Inputs
                    conn.setDoInput(true);
                    // Allow Outputs
                    conn.setDoOutput(true);
                    // Don't use a cached copy.
                    conn.setUseCaches(false);
                    // Use a post method.
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
                    dos = new DataOutputStream( conn.getOutputStream() );
                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    dos.writeBytes("Content-Disposition: form-data; name='uploadedfile'; filename='"
                    		+ existingFileName+ "'" + lineEnd);
                    dos.writeBytes(lineEnd);
                    
                    
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0){
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();
                    System.out.println("server response code " +serverResponseCode );
                    System.out.println("server response message " +serverResponseMessage );
                    
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                    dos = null;
            }
            catch (MalformedURLException ex){
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            catch (IOException ioe){
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }
            //------------------ read the SERVER RESPONSE
            try {
               DataInputStream inStream = new DataInputStream ( conn.getInputStream() );
                String str;
                String response_data = "";
                while (( str = inStream.readLine()) != null){
                    response_data = response_data+str;
                }
                inStream.close();
                System.out.println("datainput stream " +inStream);
                return response_data;
            }
            catch (IOException ioex){
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }

            return "";
        }

        protected void onPostExecute(String result){
              Log.i("result", result);
              if(result.equals("ok")){
            //      Toast.makeText(activity, "Upload succesfull", Toast.LENGTH_LONG).show();
              }else{
            //      Toast.makeText(activity, "Error on upload", Toast.LENGTH_LONG).show();
              }
          } 

    }

	public void updateReminderTime(String message) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		String[] separated = message.split(":");
		String reminderNum = separated[0].trim();
		String[] reminderTime = separated[1].split("-"); 
	
		if(reminderNum.contentEquals("1")) {
			values.put("reminder1_begin", reminderTime[0].trim());
			values.put("reminder1_end", reminderTime[1].trim());
		}
		else if(reminderNum.contentEquals("2")) {
			values.put("reminder2_begin", reminderTime[0].trim());
			values.put("reminder2_end", reminderTime[1].trim());
		}
		Log.d("LogDBAdapter", "message from server " + message);
		
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not insert");
		}
		//db.close();	
		
	}

	public void updateEODReminder(String message) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		//String[] separated = message.split(":");
		// reminderTime = separated[1].trim(); 
	
		//values.put("eod_reminder", reminderTime);
		values.put("eod_reminder", message);
		values.put("reminder_upload", 1);
		
		Log.d("LogDBAdapter", "update End of day reminder " + message);
		
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not insert");
		}
		//db.close();			
	}

	public void updateReminderTime(int reminder_num, String begin_time, String end_time) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		
		if(reminder_num == 1) {
			values.put("reminder1_begin", begin_time.trim());
			values.put("reminder1_end", end_time.trim());
		}
		else if(reminder_num == 2) {
			values.put("reminder2_begin", begin_time.trim());
			values.put("reminder2_end", end_time.trim());
		}
		values.put("reminder_upload", 1);
		
		Log.d("LogDBAdapter", "message from server " + begin_time + " till " + end_time);
		
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not insert");
		}
		//db.close();	
		
		
	}

	public void saveReminderTime(int reminder_num, long millis) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();
		
		if(reminder_num == 0) {
			values.put("reminder_1", millis);
		}
		else if(reminder_num == 2) {
			values.put("reminder_2", millis);
		}
		else if(reminder_num == 4) {
			values.put("old_eod_reminder", millis);
		}
		Log.d("LogDBAdapter", "reminder " + reminder_num + " time " + getDate(millis, "HH:mm"));
		
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.toString());
			Log.d("LogDBAdapter", "Could not insert");
		}
		//db.close();	
		
	}

	public long getReminderTime(int reminder_num) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();
		
		Cursor cursor = null;
		long millis = 0;
		
		try
		{
			cursor = db.query("user", null, null, null, null, null, null);	
		} catch (SQLException sqEx)
		{
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}

		cursor.moveToFirst();
		
		while (cursor.moveToNext()) {
			if(reminder_num == 0) {
				millis = cursor.getLong( cursor.getColumnIndex("reminder_1"));
			}
			else if(reminder_num == 2) {
				millis = cursor.getLong( cursor.getColumnIndex("reminder_2"));
			}
			else if(reminder_num == 4) {
				millis = cursor.getLong( cursor.getColumnIndex("old_eod_reminder"));
			}	
		}

		if (cursor != null)
			cursor.close();
		//db.close();
		
		return millis;
	}

	public boolean getReminderStatus(int reminder_num) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		Cursor cursor = db.query("user", null,  "user_id = ?", new String[]{""+registry.getUserId()}, null, null, null, null);	
		cursor.moveToFirst();
		
		try {
			if(reminder_num == 1) {
				 if (cursor.getInt( cursor.getColumnIndex("update_reminder1")) == 1) 
					 	return true;
			}
			else if(reminder_num == 2) {
				 if (cursor.getInt( cursor.getColumnIndex("update_reminder2")) == 1) 
					 	return true;
			}
			else if(reminder_num == 3) {
				 if (cursor.getInt( cursor.getColumnIndex("update_eod")) == 1) 
					 	return true;
			}
			
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		
		return false;
	}

	public void clearUpdate(int reminder_num) {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();

		if(reminder_num == 1) {
			values.put("update_reminder1", 0); 
		}
		else if(reminder_num == 2) {
			values.put("update_reminder2", 0); 
		}
		else if(reminder_num == 3) {
			values.put("update_eod", 0); 
		}
		
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
			
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}	
	}
	
	public boolean getUploadStatus() {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		Cursor cursor = db.query("user", null,  "user_id = ?", new String[]{""+registry.getUserId()}, null, null, null, null);	
		cursor.moveToFirst();
		
		try {
			if (cursor.getInt( cursor.getColumnIndex("reminder_upload")) == 1)
				return true;
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}
		
		return false;
	}

	public void clearUpload() {
		SQLiteDatabase db = logDbHelper.getWritableDatabase();	
		ContentValues values = new ContentValues();

		values.put("reminder_upload", 0); 		
		try {
			db.update("user", values, String.format("%s = ?", "user_id"), new String[]{""+registry.getUserId()});
			Log.d("LogDBAdapter", "Successfully inserted");
			
		} catch (SQLException sqEx) {
			Log.e("SQL Exception", sqEx.getStackTrace().toString());
		}	
	}

}
