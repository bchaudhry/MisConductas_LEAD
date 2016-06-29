package com.phitlab.lowliteracymexicanw.sync;

import java.util.ArrayList;

import com.phitlab.lowliteracymexicanw.logDB.ScreenDetail;
import com.phitlab.lowliteracymexicanw.logDB.UserDetail;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DataUploadService extends IntentService implements AsyncTaskCompletedListener{
	
//	private static ScreenDBController dbController;
//	private static LogDBAdapter dbAdpater;

    public DataUploadService() {
        super("MyServiceName");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MyService", "About to execute MyTask");   
        //Execute database sync 
     //   new LoginDetailSync(this).execute();
     //   new ScreenTablesSync(this).execute();
        Log.d("MyService", "SynchTables");
        Log.d("MyService", "SynchLogin");
        new DatabaseSync(this).execute();
    }
    

	@Override
	public void onAsyncTaskCompleted() {

		System.out.println("finished synching data");
//		
//		dbController.open();
//		for(int i=0; i<al.size(); i++) {
//			dbController.update(al.get(i));
//		}
//		dbController.close();
		
	}

	@Override
	public void onAsyncTaskCompleted(ArrayList<ScreenDetail> al) {
		
	}

//	@Override
//	public void onAsyncTaskCompleted(UserDetail userDetail) {
//		
//	}

}
