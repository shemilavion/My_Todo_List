package il.ac.shenkar.mobile.todoApp;

import com.google.analytics.tracking.android.Log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderBroadCastReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
	{
		Log.i("Bridcast Reciver Class brodcasting the Notification");
		int taskId = intent.getIntExtra("il.ac.shenkar.mobile.todoApp.taskId", 0);
		Intent activityIntent = new Intent(context ,My_Todo_App.class);
		activityIntent.putExtra("task_id", taskId);
		PendingIntent intent2 = PendingIntent.getActivity(context,0,activityIntent,PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),intent2);
		//debug only - print to screen the task id that received from notification 
	//	Toast.makeText(context, "task id number:" + taskId, Toast.LENGTH_LONG).show();
	}
}