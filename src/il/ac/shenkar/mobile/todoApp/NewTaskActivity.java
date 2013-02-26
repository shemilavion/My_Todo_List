package il.ac.shenkar.mobile.todoApp;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

@SuppressLint({ "NewApi", "NewApi", "NewApi", "SimpleDateFormat" })
public class NewTaskActivity extends Activity
{
	@Override
	protected void onStart() {
		super.onStart();
		// for google analytic 
		EasyTracker.getInstance().activityStart(this);
	}
	@Override
	protected void onStop() {
		super.onStop();
		// for google analytic 
		EasyTracker.getInstance().activityStop(this);
	}
	private Dal taskDal = null;
	private DialogFragment dateFragment;
	private DialogFragment timeFragment;
	private DialogFragment locationFragment;
	private static final String dateFragmentTag = "datePicker";
	private static final String timeFragmentTag = "timePicker";
	private static final String locationFragmentTag = "locationPicker";
	private Address selectedAddress = null;
	private Geocoder geoc = null;
	int editedId = -1;
	private ProgressDialog progressDialod;
	//5 MINUTS IN MILL's
	private int PRE_NOTIFY_TIME_IN_MILLES = 300000;
	//date picker fragment
	 FragmentManager FragmentManager;
	 private Tracker myTracker;
	private GoogleAnalytics myGaInstance;
		
		
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	 // Get the GoogleAnalytics singleton. Note that the SDK uses
        // the application context to avoid leaking the current context.
        myGaInstance = GoogleAnalytics.getInstance(this);
        // Use the GoogleAnalytics singleton to get a Tracker.
        myTracker = myGaInstance.getTracker(getString(R.string.Google_Analytic_ID));	 // Placeholder tracking ID from Strings XML

    	Log.i((String) getTitle(), "onCreate, new task Activity was create");
    	editedId = -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Spinner mySpinner = (Spinner) findViewById(R.id.priority_spinner);
        dateFragment = new DatePickerFragment();
        timeFragment = new TimePickerFragment();
        // Create an instance of addresses fragment
        locationFragment = new LocationPickerFragment(this);
        mySpinner.setAdapter(new ArrayAdapter<Importancy>(this, android.R.layout.simple_spinner_item, Importancy.values()));
        taskDal = Dal.getDal(this); 
    	Intent incomeIntent = this.getIntent();
        int taskToEditId = incomeIntent.getIntExtra("edit_task_id", -1);
        if(taskToEditId != -1)
        {
        	Log.i((String) getTitle(), "Activity was open in 'Editing' mood, loading task data to relevant fieldss");
        	editedId = taskToEditId;
        	((Button)findViewById(R.id.create_button)).setText(getString (R.string.save_changes));
        	//call method to display task data
        	this.fillTaskForm(taskDal.getTaskById(taskToEditId));
        	closeKeyBoard();
        }
        
        // this listener is meant to close the keybord on any "Enter" pressed 
        TextView.OnEditorActionListener enterPresedListemer = new TextView.OnEditorActionListener() {
        	// anonymus inner class of the listener
        	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
			{
				 if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
				 {
					 closeKeyBoard();
					 return true;
				 }
				return false;
			}
		};
		// here i attache the listener to both of my text views
		TextView textview = (TextView) findViewById(R.id.new_task_name);
		textview.setOnEditorActionListener(enterPresedListemer);
		textview = (TextView) findViewById(R.id.new_task_description);
		textview.setOnEditorActionListener(enterPresedListemer);
    }

    @Override
	protected void onResume() 
    {
		super.onResume();
		Log.i((String) getTitle(), "onResume, new task Activity was resumed");
		if(editedId == -1)
		{
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis(System.currentTimeMillis() + 1800000);
			//set due date field
			SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMMM,yyyy");
			((Button)findViewById(R.id.new_task_date)).setText(sdf.format(cal.getTime()));
			//set due time field
			sdf = new SimpleDateFormat("HH:mm");
			((Button)findViewById(R.id.new_task_time)).setText(sdf.format(cal.getTimeInMillis()));
		}
		closeKeyBoard();
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
    	// send data to google analytic 
    	myTracker.sendEvent("task_action", "task_created", "new_task_created", null);
    	finish();
    }
    public void pickDate(View v)
    {
    	// send data to google analytic 
    	myTracker.sendEvent("ui_action", "button_press", "pick_date_button", null);
    	Log.i("New Task Activity", "pickdate button chose");
    	FragmentManager = getFragmentManager();
        //create & show the date picker
    	dateFragment.show(FragmentManager, dateFragmentTag); 
    }
    public void pickTime(View v)
    {
    	// send data to google analytic 
    	myTracker.sendEvent("ui_action", "button_press", "pick_time_button", null);
    	Log.i("New Task Activity", "pickdate button chose");
    	FragmentManager = getFragmentManager();
        //create & show the time picker
    	timeFragment.show(FragmentManager, timeFragmentTag);
    }
    public void getRandomTask(View v)
    {
    	// send data to google analytic 
    	myTracker.sendEvent("ui_action", "button_press", "random_task_button", null);
    	Log.i((String) getTitle(), "bringing random task from server");
    	//create url
 		URL tasksServerUrl = null;
 		//check network state
 		// first we will check if we got internet connection
    	ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	
    	// if we have no internet connection
    	if (activeNetwork == null)
    	{
    		// i need to display error massage to the user
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    		// set title
    		alertDialogBuilder.setTitle(getString(R.string.net_err_head));  
    		// set dialog message
    		alertDialogBuilder.setMessage(getString(R.string.net_err_msg))
    						  .setCancelable(false)
    						  .setNegativeButton("Turn On WIFI", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									//this button will navigate you to the WIFI setting menu
									// so you could easy turn on the WIFI
									 startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
								}
							})
    						  .setPositiveButton("OK",new DialogInterface.OnClickListener() {
    							public void onClick(DialogInterface dialog,int id) {
    								// if this button is clicked, close the dilaog
    								// send data to google analytic 
    						 		myTracker.sendEvent("ui_action", "button_press", "no_internet_connection_ok_button", null);
    								dialog.cancel();
    								return;
    							}
    						  });
    		// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
    	}
    	else			// start to fetch the Task
    	{
    		
    		//starting the spinner
    		myProgressDialogStart(getString(R.string.downloading));
			try 
			{
				tasksServerUrl = new URL(getString(R.string.urlString));
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
	 		//create the async task
			
	 		new AsyncTaskWebServer(this).execute(tasksServerUrl);
    	}		
    }
    //pick a location method - called when location button picked on new task activity 
    public void pickLocation(View view)
	{
    	// send data to google analytic 
    	myTracker.sendEvent("ui_action", "button_press", "pick_lication_button", null);
    	Log.i((String) getTitle(), "Pick location fragment initialising...");
    	FragmentManager = getFragmentManager();
        //create & show the location picker
    	locationFragment.show(FragmentManager, locationFragmentTag);
	}
    public void updateLocation(Address add)
    {
    	selectedAddress = add;
 		//create geo-coder
 		if(geoc == null)
 		{
 			geoc = new Geocoder(this);
 		}
 		((Button)findViewById(R.id.location_button)).setText(selectedAddress.getAddressLine(0) + ", " + selectedAddress.getAddressLine(1));
    }
    public void createTask(View view)
    {
    	// send data to google analytic 
    	myTracker.sendEvent("ui_action", "button_press", "create_task_button", null);
    	Log.i((String) getTitle(), "creating new task...");
    	//case of editing mode - delete old task first
    	if(editedId != -1)
    	{
    		taskDal.deleteTask(taskDal.getTaskById(editedId));
    	}
    	Task newTask = new Task();
    	Log.i((String) getTitle(), "start geting task data from GUI...");
    	//get task name from gui
    	EditText editText = (EditText) findViewById(R.id.new_task_name);
    	newTask.setTaskName(editText.getText().toString());
    	//get task description
    	editText = (EditText) findViewById(R.id.new_task_description);
    	newTask.setTaskDescription(editText.getText().toString());
    	//get date from fragment
    	newTask.setDueDate( ((DatePickerFragment)dateFragment).getDate());
    	GregorianCalendar selectedTime = ((TimePickerFragment)timeFragment).getTime();
    	GregorianCalendar selectedDate = newTask.getDueDate();
    	selectedDate.set(Calendar.HOUR_OF_DAY,selectedTime.get(Calendar.HOUR_OF_DAY));
    	selectedDate.set(Calendar.MINUTE,selectedTime.get(Calendar.MINUTE));
    	selectedDate.set(Calendar.AM_PM,selectedTime.get(Calendar.AM_PM));
    	newTask.setDueDate(selectedDate);
    	//get location

		//set location string
    	if(selectedAddress != null)
    	{
    		newTask.setTaskLat(selectedAddress.getLatitude());
    		newTask.setTaskLong(selectedAddress.getLongitude());
    	}
    	//get task priority
    	Spinner spinner = (Spinner) findViewById(R.id.priority_spinner);
    	newTask.setImportancy((Importancy)spinner.getSelectedItem());
    	//get notify flag
    	CheckBox checkBox = (CheckBox) findViewById(R.id.notify_c_box);
    	newTask.setNotifyFlag(checkBox.isChecked());
    	
    	Log.i((String) getTitle(), "finish getting all the data from GUI");
    	Log.i((String) getTitle(), "validating Data...");
    	if(! newTask.validateTask() )
    	{
    		Toast.makeText(getApplicationContext(), "Fill all required Fields", Toast.LENGTH_LONG).show();
    		if (newTask.getTaskName().equals(""))
    			{
    				editText = (EditText) findViewById(R.id.new_task_name);
    				editText.setHintTextColor(Color.RED);
    			}
    				
    		Log.i((String) getTitle(), "new task data isn't validate...");
    		return;
    	}
    	Log.i((String) getTitle(), "new task data is validate...");
    	//insert new task into db
    	long newTaskId = taskDal.addTask(newTask);
    	
    	//set a notification if needed
    	if(newTask.getNotifyFlag())
    	{
    		Log.i((String) getTitle(), "Setting notification...");
	    	//////////////////////////////////////////////////////////////////////////////////////////////
	    	//	for time debugging																		//
	    	//	System.out.println(newTask.getDueDate().toString());									//
	    	//	System.out.println("local time:" + new GregorianCalendar().getTimeInMillis());			//
	    	//	System.out.println("notification time:" + newTask.getDueDate().getTimeInMillis());		//
	    	//////////////////////////////////////////////////////////////////////////////////////////////
    		Context context = getApplicationContext();
	    	Intent activityIntent = new Intent(context ,Notifyer.class);
	    	activityIntent.putExtra("notified_taskId", (int)newTaskId);
	    	PendingIntent penIntent =  PendingIntent.getBroadcast(this, (int)newTaskId,activityIntent,PendingIntent.FLAG_ONE_SHOT);
	    	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    	alarmManager.set(AlarmManager.RTC_WAKEUP
	    	,newTask.getDueDate().getTimeInMillis() - PRE_NOTIFY_TIME_IN_MILLES
	    	,penIntent);
    	}
    	editedId = -1;
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
 	
 	//this method receives a task & fill all new task activity fields according to the task details.  
 	@SuppressLint("SimpleDateFormat")
	public void fillTaskForm(Task inputTask)
 	{
 		//nullness check
 		if(inputTask == null)
 		{
 			return;
 		}
 		//create geo-coder
 		if(geoc == null)
 		{
 			geoc = new Geocoder(this);
 		}
 		//set task name field
 		((EditText)findViewById(R.id.new_task_name)).setText(inputTask.getTaskName());
 		//set task description field
 		((EditText)findViewById(R.id.new_task_description)).setText(inputTask.getTaskDescription());
 		//set due date field
 		SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMMM,yyyy");
 		GregorianCalendar cal = inputTask.getDueDate();
 			//update time to new task time object
 			((DatePickerFragment)dateFragment).setDate(cal);
 		((Button)findViewById(R.id.new_task_date)).setText(sdf.format(cal.getTime()));
 		//set due time field
 		sdf = new SimpleDateFormat("HH:mm");
			//update time to new task time object
			((TimePickerFragment)timeFragment).setTime(cal);
 		((Button)findViewById(R.id.new_task_time)).setText(sdf.format(cal.getTime()));
 		//set location string
 		List<Address> addList = null;
		try 
		{
			addList = geoc.getFromLocation(inputTask.getTaskLat(), inputTask.getTaskLong(), 1);
		} 
		catch (IOException e) 
		{
			((Button)findViewById(R.id.location_button)).setText("No Location");
		}
		if(addList != null && addList.size() != 0)
		{
			selectedAddress = addList.get(0);
			String add = selectedAddress.getAddressLine(0) + ", " + selectedAddress.getAddressLine(1);
			((Button)findViewById(R.id.location_button)).setText(add);
		}
		else
		{
			((Button)findViewById(R.id.location_button)).setText(getString(R.string.location_nullness));
		}
 		//set task priority
 		((Spinner)findViewById(R.id.priority_spinner)).setSelection(inputTask.getImportancy().getValue());
 		//set notify check box
 		((CheckBox)findViewById(R.id.notify_c_box)).setChecked(inputTask.getNotifyFlag());
 	}

 	  // creating the progress dialog
    public void myProgressDialogStart(String msg)
     {
    	Log.i((String) getTitle(), "Starting progress dialog");
     	//building the progress dialog
         this.progressDialod = new ProgressDialog(this);
         progressDialod.setProgressStyle(ProgressDialog.STYLE_SPINNER);
         progressDialod.setMessage(msg);
         progressDialod.setIndeterminate(true);
         progressDialod.setCancelable(false);
 		progressDialod.show();
     }
    
    //canceling the progress dialog
    public void myProgressDialogStop() 
    {
    	Log.i((String) getTitle(), "stoping progress dialog");
    	this.progressDialod.cancel();
    }
 	public void closeKeyBoard()
 	{
 		//hide the soft keyboard
	    final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.new_task_name)).getWindowToken(), 0);
 	}
}

