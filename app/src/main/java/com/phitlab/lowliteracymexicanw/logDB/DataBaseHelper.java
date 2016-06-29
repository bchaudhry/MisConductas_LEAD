package com.phitlab.lowliteracymexicanw.logDB;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* Example from http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/ 
 * */

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH; 
    private String dbName;
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     * @param dbName
     */
    public DataBaseHelper(Context context, String dbName) {
    	 
    	super(context, dbName, null, 1);
    	this.dbName = dbName;
        this.myContext = context;
        DB_PATH = myContext.getFilesDir().getPath();
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		Log.d("createDataBase", "database already exists");
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
    			Log.d("createDataBase", "just created a copy of my database");
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
 //   	File dbFile = new File(DB_PATH + dbName);
 //   	return dbFile.exists();
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + dbName;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
    		Log.d("checkdatabases", "Database does not exist yet");	
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(dbName);
    	
    	Log.d("copyDataBase", "Want to copy database7777");
    	
    	// Path to the just created empty db
    	String outFileName = DB_PATH + dbName;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	
    	Log.d("copyDataBase", "Want to copy database");
    	
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}

    	Log.d("copyDataBase", "Want to copy database22");
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + dbName;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("Oncreate", "I am in oncreate method");
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		
	}
	
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
	public Cursor rawQuery(String sql)
	{
		return myDataBase.rawQuery(sql, null);
	}
	
	public SQLiteDatabase getDB()
	{
		return myDataBase;
	}
	public Context getContext()
	{
		return myContext;
	}
}