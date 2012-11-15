package il.ac.shenkar.mobile.todoApp;


import com.example.my_todo_app.R;
import android.view.View;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class My_Todo_App extends Activity 
{
	Dal taskDal = null;
	public final static String EXTRA_MESSAGE = "com.example.my_todo_app.MESSAGE";
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        taskDal = Dal.getDal();
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
	}
	
	public void onCheckedChanged(View v) 
	{
		String taskName = (String)v.getTag();
		Task currTask = taskDal.GetTask(taskName);
		currTask.setIsDoneFlag(!currTask.isDone());
		taskDal.deleteTask(currTask);
		taskDal.addTask(currTask);
		final ListView tasksListView = (ListView) findViewById(R.id.listV_main);
        //set the listView adapter
		((BaseAdapter) tasksListView.getAdapter()).notifyDataSetChanged();
	}
}
