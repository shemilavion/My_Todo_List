package il.ac.shenkar.mobile.todoApp;



import java.util.Calendar;
import java.util.GregorianCalendar;
import com.example.my_todo_app.R;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

@SuppressLint({ "NewApi", "NewApi", "NewApi" })
public class NewTaskActivity extends Activity
{
	private Dal taskDal = null;
	private DialogFragment dateFragment;
	private DialogFragment timeFragment;
	private static final String dateFragmentTag = "datePicker";
	private static final String timeFragmentTag = "timePicker";
	private NotificationManager notificationManager;
	private Notification myNotification;
	//5 MINUTS IN MILL's
	private int PRE_NOTIFY_TIME_IN_MILLES = 300000;
	//date picker fragment
	 FragmentManager FragmentManager;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Spinner mySpinner = (Spinner) findViewById(R.id.priority_spinner);
        dateFragment = new DatePickerFragment();
        timeFragment = new TimePickerFragment();
        mySpinner.setAdapter(new ArrayAdapter<Importancy>(this, android.R.layout.simple_spinner_item, Importancy.values()));
        taskDal = Dal.getDal(this); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_new_task, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void backToMainScreen(View view)
    {
    	finish();
    }
    public void pickDate(View v)
    {
    	FragmentManager = getFragmentManager();
        //create & show the date picker
    	dateFragment.show(FragmentManager, dateFragmentTag); 
    }
    public void pickTime(View v)
    {
    	FragmentManager = getFragmentManager();
        //create & show the time picker
    	timeFragment.show(FragmentManager, timeFragmentTag);
    }
    
    @SuppressWarnings("deprecation")
	public void createTask(View view)
    {
    	Task newTask = new Task();
    	//get task name from gui
    	EditText editText = (EditText) findViewById(R.id.new_task_name);
    	newTask.setTaskName(editText.getText().toString());
    	//get task description
    	editText = (EditText) findViewById(R.id.new_task_description);
    	newTask.setTaskDescription(editText.getText().toString());
    	//get date from fragment
        if(((DatePickerFragment)dateFragment).getDate() != null)
        {
        	newTask.setDueDate( ((DatePickerFragment)dateFragment).getDate());
        }
        else
        {
    		Toast.makeText(getApplicationContext(), "pick a due date", Toast.LENGTH_LONG).show();
    		return;        	
        }
        //get time from fragment
        if(((TimePickerFragment)timeFragment).getTime() != null)
        {
        	GregorianCalendar selectedTime = ((TimePickerFragment)timeFragment).getTime();
        	GregorianCalendar selectedDate = newTask.getDueDate();
        	selectedDate.set(Calendar.HOUR,selectedTime.get(Calendar.HOUR));
        	selectedDate.set(Calendar.MINUTE,selectedTime.get(Calendar.MINUTE));
        	selectedDate.set(Calendar.AM_PM,selectedTime.get(Calendar.AM_PM));
        	newTask.setDueDate(selectedDate);
        }
        else
        {
    		Toast.makeText(getApplicationContext(), "pick a due date", Toast.LENGTH_LONG).show();
    		return;        	
        }
    	//get task priority
    	Spinner spinner = (Spinner) findViewById(R.id.priority_spinner);
    	newTask.setImportancy((Importancy)spinner.getSelectedItem());
    	//get notify flag
    	CheckBox checkBox = (CheckBox) findViewById(R.id.notify_c_box);
    	newTask.setNotifyFlag(checkBox.isChecked());

    	if(! newTask.validateTask() )
    	{
    		Toast.makeText(getApplicationContext(), "Fill all required Fields", Toast.LENGTH_LONG).show();
    		return;
    	}
    	//insert new task into db
    	long newTaskId = taskDal.addTask(newTask);
    	
    	//set a notification if needed
    	if(newTask.getNotifyFlag())
    	{
	    		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	    	//////////////////////////////////////////////////////////////////////////////////////////////
	    	//	for time debugging																		//
	    	//	System.out.println(newTask.getDueDate().toString());									//
	    	//	System.out.println("local time:" + new GregorianCalendar().getTimeInMillis());			//
	    	//	System.out.println("notification time:" + newTask.getDueDate().getTimeInMillis());		//
	    	//////////////////////////////////////////////////////////////////////////////////////////////
	    		//create the notification
			Context context = getApplicationContext();
			myNotification = new Notification(R.drawable.ic_launcher,newTask.getTaskName()//,50);
					,newTask.getDueDate().getTimeInMillis() - PRE_NOTIFY_TIME_IN_MILLES);
			//initial notification name & description
			String notificationTitle = newTask.getTaskName();
			String notificationText = newTask.getTaskDescription();
			Intent myIntent = new Intent("il.ac.shenkar.mobile.todoApp.My_Todo_App");
			myIntent.putExtra("il.ac.shenkar.mobile.todoApp.taskId", (int)newTaskId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
			myNotification.defaults |= Notification.DEFAULT_SOUND;
			myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
		    notificationManager.notify(0, myNotification);
    	}
    	finish();
    }

 // this code is for hiding The soft keyboard when a touch is done anywhere outside the EditText 
 	@Override
 	public boolean dispatchTouchEvent(MotionEvent event)
 	{

 	    View v = getCurrentFocus();
 	    boolean ret = super.dispatchTouchEvent(event);

 	    if (v instanceof EditText) {
 	        View w = getCurrentFocus();
 	        int scrcoords[] = new int[2];
 	        w.getLocationOnScreen(scrcoords);
 	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
 	        float y = event.getRawY() + w.getTop() - scrcoords[1];

 	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

 	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
 	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
 	        }
 	    }
 	    return ret;
 	}
}
