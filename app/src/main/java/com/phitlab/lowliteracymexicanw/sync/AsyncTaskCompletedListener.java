package com.phitlab.lowliteracymexicanw.sync;

import java.util.ArrayList;

import com.phitlab.lowliteracymexicanw.logDB.ScreenDetail;
import com.phitlab.lowliteracymexicanw.logDB.UserDetail;



public interface AsyncTaskCompletedListener {
	
	    void onAsyncTaskCompleted(ArrayList<ScreenDetail> al);

		//void onAsyncTaskCompleted(UserDetail userDetail);
		
		void onAsyncTaskCompleted();

}
