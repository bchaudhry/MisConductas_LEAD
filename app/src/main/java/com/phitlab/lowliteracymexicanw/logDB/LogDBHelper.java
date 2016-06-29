package com.phitlab.lowliteracymexicanw.logDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LogDBHelper extends SQLiteOpenHelper{

	private static LogDBHelper dbHelperInstance = null;
	private static final String DATABASE_NAME = "llmw.db";
	private static int DATABASE_VERSION = 1;
	
	private LogDBHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static LogDBHelper getInstance(Context ctx) {

		if (dbHelperInstance == null) {
			dbHelperInstance = new LogDBHelper(ctx);
		}
		return dbHelperInstance;
	}


	@Override
	public void onCreate(SQLiteDatabase database) {

		Log.d("on create dbhelper", "on create called");
		createTables(database);
	}

	private void createTables(SQLiteDatabase database) {
		database.execSQL(SCREEN_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		if(oldVersion<newVersion) {
			Log.w(LogDBHelper.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");

			database.execSQL("DROP TABLE IF EXISTS " + SCREEN_LOG);
			onCreate(database);
			
			LogDBHelper.DATABASE_VERSION = newVersion;
		}
		else {
			Log.w("DB not upgraded", "DB version not greater");
		} 
	}
	
	public static final String SCREEN_LOG = "screen_detail";
	public static final String PRIMARY_KEY = "screen_id";
	public static final String SCREEN_NAME = "screen_name";
	public static final String GRID_1 = "grid1";
	public static final String GRID_2 = "grid2";
	public static final String GRID_3 = "grid3";
	public static final String GRID_4 = "grid4";
	public static final String GRID_5 = "grid5";
	public static final String GRID_6 = "grid6";
	public static final String GRID_7 = "grid7";
	public static final String GRID_8 = "grid8";
	public static final String GRID_9 = "grid9";
	
	// Database creation SQL statement
	private static final String SCREEN_TABLE = "create table if not exists " 
			+ SCREEN_LOG
			+ "(" 
			+ PRIMARY_KEY + " integer primary key, " 
			+ SCREEN_NAME + " text not null, " 
			+ GRID_1 + " text ," 
			+ GRID_2 + " text ," 
			+ GRID_3 + " text ," 
			+ GRID_4 + " text ," 
			+ GRID_5 + " text ," 
			+ GRID_6 + " text ," 
			+ GRID_7 + " text ," 
			+ GRID_8 + " text ," 
			+ GRID_9 + " text "
			+ ");";
}
