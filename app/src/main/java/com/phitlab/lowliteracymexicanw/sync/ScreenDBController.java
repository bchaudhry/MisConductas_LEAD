package com.phitlab.lowliteracymexicanw.sync;

import java.util.ArrayList;

import com.phitlab.lowliteracymexicanw.logDB.LogDBHelper;
import com.phitlab.lowliteracymexicanw.logDB.ScreenDetail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ScreenDBController {


	private final static String domainName = "https://phitlab.soic.indiana.edu/www/";
	private static ScreenDBController dbControllerInstance = null;
	private SQLiteDatabase database;
	private LogDBHelper dbHelper;
	private String[] allColumns = {   
			LogDBHelper.PRIMARY_KEY,
			LogDBHelper.SCREEN_NAME,
			LogDBHelper.GRID_1,
			LogDBHelper.GRID_2,
			LogDBHelper.GRID_3,
			LogDBHelper.GRID_4,
			LogDBHelper.GRID_5,
			LogDBHelper.GRID_6,
			LogDBHelper.GRID_7,
			LogDBHelper.GRID_8,
			LogDBHelper.GRID_9,
	};

	
	private ScreenDBController(Context context) {
		dbHelper = LogDBHelper.getInstance(context);
		this.open();
		initializeDB();
	}

	
	public static ScreenDBController getInstance(Context ctx) {

		if (dbControllerInstance == null) {
			dbControllerInstance = new ScreenDBController(ctx);
		}
		return dbControllerInstance;
	}

	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	void initializeDB() {
		ArrayList<String> al = new ArrayList<String>();
		al.add("");
		al.add("");
		al.add("");
		al.add("");
		al.add("");
		al.add("");
		al.add("");
		al.add("");
		al.add("");

		String[] screenNames = {"Diet", "Teas","Powders","Pills","Drops","Drinks", "People", 
				"DietLabels", "TeaLabels", "PowderLabels", "PillLabels", "DropLabels", "DrinkLabels",
				"PeopleTag", "Alcohol", "AlcoholLabels", "PillSounds", "PowderSounds", "DrinkSounds",
				"AlcoholSounds", "TeaSounds", "DropSounds", "DietSounds", "PeopleSounds"};
		ScreenDetail screenDetail = null;
		for(int i=0; i<screenNames.length; i++) {
			screenDetail = new ScreenDetail(i+1, screenNames[i], al);
			long val = this.insertScreen(screenDetail);
			Log.d("initializeDB", "screenName "+screenNames[i]);
			screenDetail = null;
			if(val == -1) {
				Log.d("ScreenDBController", "Table already exists");
				return;
			}
		}
	}


	public long insertScreen(ScreenDetail screenDetail) {

		ContentValues values = new ContentValues();
		values.put(LogDBHelper.PRIMARY_KEY, screenDetail.getScreenID());
		values.put(LogDBHelper.SCREEN_NAME, screenDetail.getScreenName()); // Screen Name

		System.out.println("inserting screen");
		for(int i=0; i<screenDetail.getUrlAL().size(); i++) {
			//allColumns contains grid columns from 2-10
			values.put(allColumns[i+2], screenDetail.getUrlAL().get(i));
			System.out.println("inserting column is "+screenDetail.getUrlAL().get(i));
		}

		// Inserting Row
		long insertVal = database.insert(LogDBHelper.SCREEN_LOG, null, values);
//		if(insertVal == -1) {
//			Log.w("DB insert Error for row", screenDetail.getScreenName());
//		}

		return insertVal;
	}


	// Updating single screen
	public int update(ScreenDetail screenDetail) {

		int updatedRow = -1;
		ContentValues values = new ContentValues();
		values.put(LogDBHelper.SCREEN_NAME, screenDetail.getScreenName()); // Screen Name

		for(int i=0; i<screenDetail.getUrlAL().size(); i++) {
			values.put(allColumns[i+2], screenDetail.getUrlAL().get(i));
		}

		// updating row
		updatedRow = database.update(LogDBHelper.SCREEN_LOG, values, LogDBHelper.PRIMARY_KEY + " = ?",
				new String[] { String.valueOf(screenDetail.getScreenID()) });

		if(updatedRow == -1) {
			Log.w("DB update Error for row", screenDetail.getScreenName());
		}

		return updatedRow;
	}


//	public ArrayList<String> getImageURLs(String screenName) {
//
//		ArrayList<String> imageURLAL = new ArrayList<String>();
//
//		String selectQuery = "SELECT * FROM " + LogDBHelper.SCREEN_LOG+" where screen_name='"+screenName+"'";
//		Cursor cursor = database.rawQuery(selectQuery, null);
//		cursor.moveToFirst();
//
//		Integer cnt = cursor.getColumnCount();
//		Log.w("Column count", cnt.toString());
//		for(int i=2; i<cursor.getColumnCount(); i++) {
//			String colName = cursor.getColumnName(i);
//			String url = cursor.getString(cursor.getColumnIndex(colName));
//			Log.d("url", ""+url);
//			if(url != null && !url.equalsIgnoreCase("empty")) {
//				url = domainName+url;
//				imageURLAL.add(url);
//			}
//			cursor.moveToFirst();
//		}
//		return imageURLAL;
//	}

	public ArrayList<String> getImageURLs(String screenName) {

		ArrayList<String> imageURLAL  = new ArrayList<String>();

		String selectQuery = "SELECT * FROM " + LogDBHelper.SCREEN_LOG+" where screen_name='"+screenName+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		Integer cnt = cursor.getColumnCount();
		Log.w("Column count", screenName);
		for(int i=2; i<cnt; i++) {
			String colName = cursor.getColumnName(i);
			Log.w("Column count", ""+cursor.getColumnIndex(colName));
			String url = cursor.getString(cursor.getColumnIndex(colName));
			Log.d("imageURL", ""+domainName.concat(url));
			if(url != null && !url.equalsIgnoreCase("empty")) {
				imageURLAL.add(domainName.concat(url));
			}
			cursor.moveToFirst();                                                                                                                                                                                                                                                                            
		}
		return imageURLAL;
	}
	
	public ArrayList<String> getLabels(String screenName) {

		ArrayList<String> entries = new ArrayList<String>();

		String selectQuery = "SELECT * FROM " + LogDBHelper.SCREEN_LOG+" where screen_name='"+screenName+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		Integer cnt = cursor.getColumnCount();
		Log.w("Column count", screenName);
		for(int i=2; i<cnt; i++) {
			String colName = cursor.getColumnName(i);
			Log.w("Column count", ""+cursor.getColumnIndex(colName));
			String url = cursor.getString(cursor.getColumnIndex(colName));
			if(url != null && !url.equalsIgnoreCase("empty")) {
				entries.add(url);
			}
			cursor.moveToFirst();
		}
		return entries;
	}

}
