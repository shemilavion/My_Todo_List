package il.ac.shenkar.mobile.todoApp;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.view.Menu;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class My_Todo_App extends Activity implements OnItemSelectedListener
{
	private Dal taskDal = null;
	private final long miliSecsInDay = 24*3600*1000;
	public final static String EXTRA_MESSAGE = "com.example.my_todo_app.MESSAGE";
	//initial sorting algorithm to sort by due date
	public SortingMannor sortingMannor = SortingMannor.BY_DUE_DATE;
	//back button disable flag - if set to false - no disable will be applied, used for popup ...
	private boolean backDisable = false;
	private Tracker myTracker;
	private GoogleAnalytics myGaInstance;

	
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	@Override
	public void onBackPressed() 
	{
	  if(backDisable != true)
	  {
		  super.onBackPressed();
	  }
	}
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        // Get the GoogleAnalytics singleton. Note that the SDK uses
        // the application context to avoid leaking the current context.
        myGaInstance = GoogleAnalytics.getInstance(this);
        // Use the GoogleAnalytics singleton to get a Tracker.
        myTracker = myGaInstance.getTracker(getString(R.string.Google_Analytic_ID));	 // Placeholder tracking ID from Strings XML
        
        taskDal = Dal.getDal(this);
        setContentView(R.layout.activity_my__todo__app);
        Log.i((String) getTitle(),"onCreste(), Main Activity Created" );
        final ListView tasksListView = (ListView) findViewById(R.id.listV_main);
        //set the listView adapter
        tasksListView.setAdapter(new TaskListBaseAdapter(this));
        //item click listener
        tasksListView.setOnItemClickListener(new OnItemClickListener() 
		{
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{ 
        		Object o = tasksListView.getItemAtPosition(position);
            	Task obj_itemDetails = (Task)o;
        		Toast.makeText(My_Todo_App.this, "You have chosen : " + " " + obj_itemDetails.getTaskName(), Toast.LENGTH_LONG).show();
        	}
        });
        //initial the sorting spinner
        Spinner sortingSpinner = (Spinner) findViewById(R.id.sorting_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorting_mannor, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sortingSpinner.setAdapter(adapter);
        sortingSpinner.setOnItemSelectedListener(this);
        //create daily alarm to update task from server
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ServerSyncService.class);
        PendingIntent pIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
        //create todays calendar & zerosize the time - to midnight
        GregorianCalendar now =  new GregorianCalendar();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,now.getTimeInMillis() , miliSecsInDay, pIntent);
        // DEBUG ONLY - IMMIDIATE CALL THE INTENT
        //startService(intent);
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() 
        {
            public void onLocationChanged(Location location)
            {
              // Called when a new location is found by the network location provider.
            }
            public void onStatusChanged(String provider, int status, Bundle extras) 
            {}
            public void onProviderEnabled(String provider) 
            {}
            public void onProviderDisabled(String provider) 
            {}
          };
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
	@TargetApi(16)
	public void addTask(View view) 		//open "add task" activity in response to button
    {
		Log.i((String) getTitle(),"addTask(), openning new Activity" );
		// send data to google analytic 
		 myTracker.sendEvent("ui_action", "button_press", "new_task_button", null);
		Intent intent = new Intent(this, NewTaskActivity.class);
    	startActivity(intent);
    }
    @Override
    public void onNewIntent(Intent intent)
    {
		final int taskToShowId = intent.getIntExtra("task_id", -1);
		//put dummy data inside the intent for next time
		intent.putExtra("task_id", -1);
		//call method to show pop-up
		showPopup(taskToShowId);
    }
	@Override
	protected void onResume()
	{
		Log.i((String) getTitle(),"onResume(), Main Activity was resumed" );
		final ListView tasksListView = (ListView) findViewById(R.id.listV_main);
        //set the listView adapter
		taskDal.sortTasks(sortingMannor);
		((BaseAdapter) tasksListView.getAdapter()).notifyDataSetChanged();
		super.onResume();
		Intent myIntent = this.getIntent();
		final int taskToShowId = myIntent.getIntExtra("task_id", -1);
		//put dummy data inside the intent for next time
		myIntent.putExtra("task_id", -1);
		//call method to show pop-up
		showPopup(taskToShowId);
	}
	//the method called when short - pressing a task
	public void onTaskPicked(View v)
	{
		int taskId = (Integer)v.getTag();
		//call method to show the task
	    this.showTask(taskId);
	}
	//this method activates when the delete button pressed in the main list view 
	public void deleteTask(View v)
	{
		Log.i((String) getTitle(), "'Delete' button pressed, openning confermation dialog");
		final int taskId = (Integer)v.getTag();
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		{	
		    public void onClick(DialogInterface dialog, int which)
		    {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	// send data to google analytic 
			 		myTracker.sendEvent("ui_action", "button_press", "confirm_delete_yes_button", null);
		        	Log.i((String) getTitle(), "'Yes' button pressed, delete task option chose");
		        	//yes button pressed
		        	//delete the task
		    		Task currTask = taskDal.getTaskById(taskId);
		    		taskDal.deleteTask(currTask);
		    		// send data to google analytic 
		        	myTracker.sendEvent("task_action", "task_deleted", "task_deleted", null);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	// send data to google analytic 
			 		myTracker.sendEvent("ui_action", "button_press", "confirm_delete_no_button", null);
		        	Log.i((String) getTitle(), "'No' button pressed, Dismissing confermation dialog");
		            //No button clicked
		            break;
		        }
		    }
		};
		//create the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setMessage("Delete Task?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();	
	}
	//this method activates when the done checkbox presseed in the main list view
	public void onCheckedChanged(View v) 
	{
		Log.i((String) getTitle(), "done ChackBox Pressed");
		int taskId = (Integer)v.getTag();
		Task currTask = taskDal.getTaskById(taskId);
		//if task doesnt exist - return to caller
		if(currTask == null)
		{
			return;
		}
		currTask.setIsDoneFlag(!currTask.isDone());
		taskDal.deleteTask(currTask);
		taskDal.addTask(currTask);
		Log.i((String) getTitle(), "Updating data on main list");
		final ListView tasksListView = (ListView) findViewById(R.id.listV_main);
        //set the listView adapter
		((BaseAdapter) tasksListView.getAdapter()).notifyDataSetChanged();
		// send data to google analytic 
    	myTracker.sendEvent("task_action", "task_done", "task_is_done", null);
	}	

	public void showTask(int taskId)
	{
		final Task selectedTask = taskDal.getTaskById(taskId);
		// send data to google analytic 
		myTracker.sendEvent("ui_action", "button_press", "task_item_list_button", null);
		//if task doesnt exist - return to caller
		if(selectedTask == null)
		{
			return;
		}
		Log.i((String) getTitle(), "Showing Task Details in popup window, begin to build popup configuration");
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);  
	    View popupView = layoutInflater.inflate(R.layout.task_popup, null);  
	    Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    int width = size.x;
	    int height = size.y;
	    final SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMMM,yyyy - HH:mm",Locale.CANADA);
	    //debug only - write to popup size to log
	    //Log.e("pw","height: "+String.valueOf(height)+", width: "+String.valueOf(width));
	    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
	    //done button
	    Button btnDismiss = (Button)popupView.findViewById(R.id.popup_done);
	    btnDismiss.setOnClickListener(new Button.OnClickListener()
	    {
		     public void onClick(View v) 
		     {
		    	 // send data to google analytic 
		 		 myTracker.sendEvent("ui_action", "button_press", "popup_back_button", null);
		    	 //update to back button state
		    	 backDisable = false;
		    	 popupWindow.dismiss();
		    	 Log.i((String) getTitle(), "'Done' button pressed, dismissing popup window");
		     }
		 });
	    //edit button
	    Button btnEdit = (Button)popupView.findViewById(R.id.edit_task);
	    btnEdit.setOnClickListener(new Button.OnClickListener()
	    {
		     public void onClick(View v) 
		     {
		    	// send data to google analytic 
		 		myTracker.sendEvent("ui_action", "button_press", "popup_edit_button", null); 
		    	Context context = getApplicationContext();
		     	Intent intent = new Intent(context, NewTaskActivity.class);
		     	intent.putExtra("edit_task_id", selectedTask.getTaskId());
		    	startActivity(intent);
		    	//update the back button state
		    	backDisable = false;
		    	popupWindow.dismiss();
		    	Log.i((String) getTitle(), "'Edit' button pressed, dismissing popup window ");
		     }
		 });
	    //delete button
	    Button btnDelete = (Button)popupView.findViewById(R.id.delete_task);
	    btnDelete.setOnClickListener(new Button.OnClickListener()
	    {
		     public void onClick(View v) 
		     {
		    	// send data to google analytic 
		 		myTracker.sendEvent("ui_action", "button_press", "popup_delete_button", null);
		    	Log.i((String) getTitle(), "'Delete' button pressed, openning confermation dialog");
		 		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
				{	
				    public void onClick(DialogInterface dialog, int which)
				    {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	//yes button pressed
				        	//delete task from dal
				        	// send data to google analytic 
					 		myTracker.sendEvent("ui_action", "button_press", "popup_confirm_delete_yes_button", null);
				        	Log.i((String) getTitle(), "'Yes' button pressed, delete task option chose");
				        	taskDal.deleteTask(selectedTask);
					    	//update the back button state
					    	backDisable = false;
				        	popupWindow.dismiss();
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				        	// send data to google analytic 
					 		myTracker.sendEvent("ui_action", "button_press", "popup_confirm_delete_no_button", null);
				        	Log.i((String) getTitle(), "'No' button pressed, Dismissing confermation dialog");
				            //No button clicked
				            break;
				        }
				    }
				};
				//create the dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage("Delete Task?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
		    		 
		     }
		 });
	    //Share button
	    Button btnsShare = (Button) popupView.findViewById(R.id.share_task);
	    btnsShare.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// send data to google analytic 
		 		myTracker.sendEvent("ui_action", "button_press", "popup_Share_button", null);
				// Creating the massage i want to Share"
				String msgToShare = "Task Title: "+selectedTask.getTaskName() + "\n" +"Task Description: "+ selectedTask.getTaskDescription() +"\n" +
								"Task Due Date: "+sdf.format(selectedTask.getDueDate().getTime());
				// Creating the Intent responsible for Sharing
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, msgToShare);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				Log.i((String) getTitle(), "'Share' button pressed, Sharing task details acording to user input");
			}
		});
	    //get the selected task 
	    //update name field
	    ((TextView)popupView.findViewById(R.id.pop_task_name)).setText(selectedTask.getTaskName());
	    //update description field
	    ((TextView)popupView.findViewById(R.id.pop_task_desc)).setText(selectedTask.getTaskDescription());
	    //update start date field
	    ((TextView)popupView.findViewById(R.id.pop_start)).setText(sdf.format(selectedTask.getStartDate().getTime()));
	    //update due date field
	    ((TextView)popupView.findViewById(R.id.pop_end)).setText(sdf.format(selectedTask.getDueDate().getTime()));
	    //update priority
	    ((TextView)popupView.findViewById(R.id.pop_priority)).setText(selectedTask.getImportancy().toString());
	    //update notify flag	   
		if(selectedTask.getNotifyFlag())
		{
			 ((TextView)popupView.findViewById(R.id.pop_notify)).setText(getString (R.string.notifying_message)); 			
		}
		else
		{
			 ((TextView)popupView.findViewById(R.id.pop_notify)).setText(getString (R.string.not_notifying_message));
		}
		//select task status image
		//case the task is done
		if(selectedTask.isDone())
		{
			 ((ImageView)popupView.findViewById(R.id.status_light)).setImageDrawable(getResources().getDrawable(R.drawable.done));
		}
		//in case the task is late
		else if(selectedTask.getDueDate().getTimeInMillis() < System.currentTimeMillis())
		{
			 ((ImageView)popupView.findViewById(R.id.status_light)).setImageDrawable(getResources().getDrawable(R.drawable.late));
		}
		//in case the task is at work
		else if(selectedTask.getDueDate().getTimeInMillis() < System.currentTimeMillis())
		{
			 ((ImageView)popupView.findViewById(R.id.status_light)).setImageDrawable(getResources().getDrawable(R.drawable.atwork));
		}		
	    popupWindow.setOutsideTouchable(false);
   	 	//update the back button state
	    backDisable = true;
	    popupWindow.showAsDropDown(this.findViewById(R.id.horizontal_line), 20, -80);
	    Log.i((String) getTitle(), "Popup window is display");
	}
	private void showPopup(final int taskToShowId)
	{
		 //get the wakeup intent, get the task to show id(if existed) & show the selected task
		if(taskToShowId != -1)
		{
			//wait until the activity will be loaded & show the pop up 
			new Handler().postDelayed(new Runnable() {
					public void run() 
					{
						if(true)
						{
							showTask(taskToShowId);
						}
					}
		    	}, 100);
		}
	}
	/*
	 * sorting spinner selection methods
	 * */
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	{
		
		String spnrSelTxt = (String)(parent.getItemAtPosition(pos));
		if(spnrSelTxt.equals(getString(R.string.date_sorting_text)))
		{
			// send data to google analytic 
	 		myTracker.sendEvent("ui_action", "button_press", "sort_item_by_due_date_button", null);
			sortingMannor = SortingMannor.BY_DUE_DATE;
		}
		else if(spnrSelTxt.equals(getString(R.string.importancy_sorting_text)))
		{
			// send data to google analytic 
	 		myTracker.sendEvent("ui_action", "button_press", "sort_item_By_praiority_button", null);
			sortingMannor = SortingMannor.BY_HIGHER_IMPORTANCY;
		}
		else if(spnrSelTxt.equals(getString(R.string.location_sorting_text)))
		{
			// send data to google analytic 
	 		myTracker.sendEvent("ui_action", "button_press", "sort_item_by_location_button", null);
			sortingMannor = SortingMannor.BY_NEAREST_LOCATION;
		}
		//call method to resort tasks dal & refresh list according to new sorting algorithm
		taskDal.sortTasks(sortingMannor);
		final ListView tasksListView = (ListView) findViewById(R.id.listV_main);
        //set the listView adapter
		((BaseAdapter) tasksListView.getAdapter()).notifyDataSetChanged();
	}
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		//do nothing 
		return;
	}
}

