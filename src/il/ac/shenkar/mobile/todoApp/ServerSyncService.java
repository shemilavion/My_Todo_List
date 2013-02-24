package il.ac.shenkar.mobile.todoApp;

import java.net.MalformedURLException;
import java.net.URL;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/*
 * this service mission is to background handle the daily grabbing of a new task from server*/
public class ServerSyncService extends IntentService
{
	public ServerSyncService()
	{
		super("ServerSyncService");
	}
	@Override
	protected void onHandleIntent(Intent intent)
	{
		//create url
		URL tasksServerUrl;
		try
		{
			tasksServerUrl = new URL(getString(R.string.urlString));
		} catch (MalformedURLException e) 
		{
			//TO-DO - WRITE ERROR TO LOG
			return;
		}
 		//check network state
 		// first we will check if we got internet connection
    	ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	// if we have no internet connection
    	if (activeNetwork == null)
    	{
    		return;				//better luck next time :)
    	}
    	else					// start to fetch the Task from server & insert it to db
    	{
    		AsyncTaskSyncServer taskSyncObj = new AsyncTaskSyncServer(getApplicationContext());
    		taskSyncObj.execute(tasksServerUrl);
    	}		
	}
}