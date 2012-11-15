package il.ac.shenkar.mobile.todoApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * this class is the data access layer for the tasks
 * */
public class Dal implements TasksDal 
{
	private ArrayList<Task> 	taskListItems = null;
	private static Dal 			dalSingleRef = null;
	//single tone private constructor
	@SuppressWarnings("unchecked")
	private Dal()
	{
		taskListItems = GetAllTasks();
		Collections.sort(taskListItems);
	}
	//single tone object getter
	public static Dal getDal()
	{
		if(dalSingleRef == null)
		{
			dalSingleRef = new Dal();
		}
		return dalSingleRef;
	}
	//task getter according to position in db 
	public Task	GetTask(int position)
	{
		return taskListItems.get(position);
	}
	//number of tasks in db getter
	public int GetSize()
	{
		return taskListItems.size();
	}
	//add task to db
    @SuppressWarnings("unchecked")
	public boolean addTask(Task newTask) 
    {
		this.taskListItems.add(newTask);
		Collections.sort(taskListItems);
		return true;
	}
    //delete task from db
	@SuppressWarnings("unchecked")
	public boolean deleteTask(Task task)
	{
		int i = 0;
		for ( Iterator<Task> iterator= taskListItems.iterator(); iterator.hasNext(); ) 
		{
			Task currTask = (Task) iterator.next();
			if( currTask.getTaskName().equals(task.getTaskName()) )
			{
				//once a match found - delete from db, resort & return true to caller
				taskListItems.remove(i);
				Collections.sort(taskListItems);
				return true;
			}
			i++;
		}
		//if no match found - return false to caller
		return false;
	}
	//returns a collection with all tasks 
	public ArrayList<Task> getAllTasks() 
	{
		ArrayList<Task> currArr = new ArrayList<Task>(taskListItems);
		return currArr;
	}
	
	public Task GetTask(String taskName)
	{
		for ( Iterator<Task> iterator= taskListItems.iterator(); iterator.hasNext(); ) 
		{
			Task currTask = (Task) iterator.next();
			if( currTask.getTaskName().equals(taskName) )
			{
				//once a match found - return copy of task to caller
				return new Task(currTask);
			}
		}
		return null;
	}
    //simple task generator - for task list check only
	private ArrayList<Task> GetAllTasks()
	{
    	ArrayList<Task> results = new ArrayList<Task>();
    	//Instantiate several tasks & insret them into the task list 
    	Task taskDetails = new Task("Do homework", "Mobile andriod list view task",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("wash my car", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.NONE, true );
    	results.add(taskDetails);
    	taskDetails = new Task("fix the computer", "install a new operating system",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("Update mobile os version", "Update my iphone operating system version to ios6.1",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("buy new sunglasses", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("call mom", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("call dad", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("buy present for dor", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.HIGH, false );
    	results.add(taskDetails);
    	taskDetails = new Task("tell yifat that i love her", "very important :)",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.HIGH, true );
    	results.add(taskDetails);
    	taskDetails = new Task("update this task list", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.LOW, true );
    	results.add(taskDetails);
    	
    	taskDetails = new Task("go to the beach", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("make myself a cup of coffe", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.LOW, true );
    	results.add(taskDetails);
    	taskDetails = new Task("go to sleep", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("wash my hands", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.HIGH, false );
    	results.add(taskDetails);
    	taskDetails = new Task("eat dinner", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("clean my house", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.LOW, true );
    	results.add(taskDetails);
    	taskDetails = new Task("wash the dishes", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("draw something", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.HIGH, true);
    	results.add(taskDetails);
    	taskDetails = new Task("talk with my friends", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("listen to music", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.HIGH, false );
    	results.add(taskDetails);
    	taskDetails = new Task("wakeup in time", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("dummy assignment", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.NONE, true );
    	results.add(taskDetails);
    	taskDetails = new Task("enter a new assignment", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("update this task list", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.HIGH, false );
    	results.add(taskDetails);
    	taskDetails = new Task("fix the car", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("tune my piano", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.LOW, true );
    	results.add(taskDetails);
    	taskDetails = new Task("replace broken valve", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.NONE, true );
    	results.add(taskDetails);
    	taskDetails = new Task("fastem my seatbelt", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, true );
    	results.add(taskDetails);
    	taskDetails = new Task("turn the tv on", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.MEDIUM, false );
    	results.add(taskDetails);
    	taskDetails = new Task("close this app", "",
    			new GregorianCalendar(2012,11,07,12,30), new GregorianCalendar(2012,11,10,12,30),true , Importancy.NONE, true );
    	results.add(taskDetails);
    	//return task list to caller	
    	return results;
    }
}
