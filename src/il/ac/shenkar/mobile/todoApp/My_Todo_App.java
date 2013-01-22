package il.ac.shenkar.mobile.todoApp;


import java.text.SimpleDateFormat;
import java.util.Locale;
import com.example.my_todo_app.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class My_Todo_App extends Activity 
{
	Dal taskDal = null;
	public final static String EXTRA_MESSAGE = "com.example.my_todo_app.MESSAGE";
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        taskDal = Dal.getDal(this);
        setContentView(R.layout.activity_my__todo__app);
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
    }
    
    @TargetApi(16)
	public void addTask(View view) 		//open "add task" activity in response to button
    {
    	Intent intent = new Intent(this, NewTaskActivity.class);
    	startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_my__todo__app, menu);
        return true;
    }

	@Override
	protected void onResume()
	{
		final ListView tasksListView = (ListView) findViewById(R.id.listV_main);
        //set the listView adapter
		((BaseAdapter) tasksListView.getAdapter()).notifyDataSetChanged();
		super.onResume();
		Intent myIntent = this.getIntent();
		final int taskToShowId = myIntent.getIntExtra("task_id", -1);
        //get the wakeup intent, get the task to show id(if existed) & show the selected task
		if(taskToShowId != -1)
		{
			//put dummy data inside the intent for next time
			myIntent.putExtra("task_id", -1);
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
		final int taskId = (Integer)v.getTag();
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
		{	
		    public void onClick(DialogInterface dialog, int which)
		    {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	//yes button pressed
		        	//delete the task
		    		Task currTask = taskDal.getTaskById(taskId);
		    		taskDal.deleteTask(currTask);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
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
		final ListView tasksListView = (ListView) findViewById(R.id.listV_main);
        //set the listView adapter
		((BaseAdapter) tasksListView.getAdapter()).notifyDataSetChanged();
	}	

	public void showTask(int taskId)
	{
		final Task selectedTask = taskDal.getTaskById(taskId);
		//if task doesnt exist - return to caller
		if(selectedTask == null)
		{
			return;
		}
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);  
	    View popupView = layoutInflater.inflate(R.layout.task_popup, null);  
	    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,  
	                     LayoutParams.WRAP_CONTENT);
	    //done button
	    Button btnDismiss = (Button)popupView.findViewById(R.id.popup_done);
	    btnDismiss.setOnClickListener(new Button.OnClickListener()
	    {
		     public void onClick(View v) 
		     {
		    	 popupWindow.dismiss();
		     }
		 });
	    //edit button
	    Button btnEdit = (Button)popupView.findViewById(R.id.edit_task);
	    btnEdit.setOnClickListener(new Button.OnClickListener()
	    {
		     public void onClick(View v) 
		     {
		//     	Intent intent = new Intent(this, NewTaskActivity.class);
		  //  	startActivity(intent);
		    	 Toast.makeText(My_Todo_App.this, "edit", Toast.LENGTH_LONG).show();
		    	 //TODO - go to mission edit
		     }
		 });
	    //delete button
	    Button btnDelete = (Button)popupView.findViewById(R.id.delete_task);
	    btnDelete.setOnClickListener(new Button.OnClickListener()
	    {
		     public void onClick(View v) 
		     {
		 		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
				{	
				    public void onClick(DialogInterface dialog, int which)
				    {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	//yes button pressed
				        	//delete task from dal
				        	taskDal.deleteTask(selectedTask);
				        	popupWindow.dismiss();
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMMM,yyyy - hh:mm",Locale.CANADA);
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
			 ((TextView)popupView.findViewById(R.id.pop_notify)).setText("Notifing uppon due date"); 			
		}
		else
		{
			 ((TextView)popupView.findViewById(R.id.pop_notify)).setText("Not Notifing uppon due date");
		}
	    popupWindow.setOutsideTouchable(false);
	    popupWindow.showAsDropDown(this.findViewById(R.id.horizontal_line), 20, -80);
	}
}

