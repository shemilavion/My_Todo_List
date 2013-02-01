package il.ac.shenkar.mobile.todoApp;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.my_todo_app.R;

public class Notifyer extends BroadcastReceiver
{
	  @SuppressWarnings("deprecation")
	public void onReceive(Context context, Intent incomeIntent)
	  {
		//create the notification
		  final int taskToShowId = incomeIntent.getIntExtra("notified_taskId", -1);
		  Dal taskDal = Dal.getDal(context);
		  Task notifyTask = taskDal.getTaskById(taskToShowId);
		  if(notifyTask != null)
		  {
			  NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			  Notification myNotification = new Notification(R.drawable.ic_launcher,notifyTask.getTaskName(),notifyTask.getDueDate().getTimeInMillis());
			  //initial notification name & description
			  String notificationTitle = notifyTask.getTaskName();
			  String notificationText = notifyTask.getTaskDescription();
			  Intent myIntent = new Intent("il.ac.shenkar.mobile.todoApp.My_Todo_App");
			  myIntent.putExtra("il.ac.shenkar.mobile.todoApp.taskId", (int)taskToShowId);
			  PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
			  myNotification.defaults |= Notification.DEFAULT_SOUND;
			  myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			  myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
			  notificationManager.notify(0, myNotification);			  	  
		  }
			  	  

	}

}
