package il.ac.shenkar.mobile.todoApp;



import com.example.my_todo_app.R;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	private int ID;
	private DialogFragment newFragment;
	private static final String fragmentTag = "datePicker";
	//date picker fragment
	 FragmentManager FragmentManager;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Spinner mySpinner = (Spinner) findViewById(R.id.priority_spinner);
        newFragment = new DatePickerFragment();
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
        newFragment.show(FragmentManager, fragmentTag);
        ID= newFragment.getId();
        FragmentTransaction transaction = FragmentManager.beginTransaction();      
        transaction.add(newFragment, fragmentTag);
        transaction.commit();
        FragmentManager.executePendingTransactions();
        
    }
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
    	FragmentManager = getFragmentManager();
        DatePickerFragment fragment = (DatePickerFragment) FragmentManager.findFragmentById(ID);
        if(fragment != null)
        {
        	newTask.setDueDate( fragment.getDate());
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
    	taskDal.addTask(newTask);
    	finish();
    }
    

}
