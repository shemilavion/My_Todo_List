package il.ac.shenkar.mobile.todoApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * this class is the data access layer for the tasks
 * */
public class Dal extends SQLiteOpenHelper implements TasksDal 
{
	Context mainListActivityContext;
	//Static variables
    ArrayList<Task> tasksList = null;
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "TasksList.db";
 
    // Tasks table name
    private static final String TABLE_TASKS = "tasks";
 
    // Contacts Table Columns names
    private static final String KEY_ID 		= 	"id"; 
    private static final String KEY_NAME 	= 	"name";
    private static final String KEY_DESC 	= 	"description";
    private static final String KEY_ST_YR   = 	"start_year";
    private static final String KEY_ST_MON  = 	"start_month";
    private static final String KEY_ST_DAY  = 	"start_day";
    private static final String KEY_ST_HOUR = 	"start_hour";
    private static final String KEY_ST_MIN  = 	"start_min";
    private static final String KEY_DUE_YR  = 	"due_year";
    private static final String KEY_DUE_MON = 	"due_month";
    private static final String KEY_DUE_DAY = 	"due_day";
    private static final String KEY_DUE_HOUR= 	"due_hour";
    private static final String KEY_DUE_MIN = 	"due_min";
    private static final String KEY_NOT 	= 	"notify";
    private static final String KEY_IMP 	= 	"importancy";
    private static final String KEY_LAT 	= 	"task_lat";
    private static final String KEY_LON 	= 	"task_long";
    private static final String KEY_DONE 	= 	"done_flag";
    //the static singleton object
	private static Dal 			dalSingleRef = null;
	//single tone private constructor
	private Dal(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mainListActivityContext = context;
		syncDal();
	}
	//single tone object getter
	public static Dal getDal(Context cont)
	{
		if(dalSingleRef == null)
		{
			dalSingleRef = new Dal(cont);
		}
		return dalSingleRef;
	}
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) 
    {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
        		+ KEY_ID   	     + " INTEGER PRIMARY KEY AUTOINCREMENT,"	//the id of each task must be unique - so its defined as AUTOINCREMENT
        		+ KEY_NAME   	 + " TEXT," 
        		+ KEY_DESC   	 + " TEXT,"
        		+ KEY_ST_YR    	 + " INTEGER,"
        		+ KEY_ST_MON     + " INTEGER,"
        		+ KEY_ST_DAY     + " INTEGER,"
        		+ KEY_ST_HOUR    + " INTEGER,"
        		+ KEY_ST_MIN     + " INTEGER,"
        		+ KEY_DUE_YR     + " INTEGER,"
        		+ KEY_DUE_MON    + " INTEGER,"
        		+ KEY_DUE_DAY    + " INTEGER,"
        		+ KEY_DUE_HOUR   + " INTEGER,"
        		+ KEY_DUE_MIN    + " INTEGER,"
                + KEY_NOT   	 + " INTEGER,"
                + KEY_IMP   	 + " TEXT,"
                + KEY_LAT   	 + " REAL,"
                + KEY_LON  		 + " REAL,"
                + KEY_DONE  	 + " INTEGER" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        // Create tables again
        onCreate(db);
    }
	//task getter according to position in db 
	public Task	getTask(int position)
	{
		/*
		//get the database object
	    SQLiteDatabase db = this.getReadableDatabase();
	    //perform the query
	    Cursor cursor = db.query(TABLE_TASKS, new String[] {KEY_ID, KEY_NAME, 
	    		KEY_DESC,KEY_ST_YR, KEY_ST_MON, KEY_ST_DAY, KEY_ST_HOUR, KEY_ST_MIN,
	    		KEY_DUE_YR, KEY_DUE_MON, KEY_DUE_DAY, KEY_DUE_HOUR, KEY_DUE_MIN,
	    		KEY_NOT,KEY_IMP, KEY_LAT, 
	    		KEY_LON, KEY_DONE }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	    {
	        cursor.moveToFirst();
	    }
	    //call auxiliary method to parse the row into a task
	    Task task = parseTaskFromRow(cursor);
	    db.close();
	    */
	    // return task
	    return tasksList.get(position);
	}
	//get the task by id
	public Task getTaskById(int id)
	{
		for ( Iterator<Task> iterator= tasksList.iterator(); iterator.hasNext(); ) 
		{
			Task currTask = (Task) iterator.next();
			if( currTask.getTaskId() == id )
			{
				//once a match found - return to caller
				return currTask;
			}
		}
		return null;
	}
	//number of tasks in database getter
	public int getTasksCount()
	{
        /*String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();*/
        // return count
        return tasksList.size();
	}
	
	//add task to db & return the task id
	public long addTask(Task newTask) 
    {
    	//get the database object
        SQLiteDatabase db = this.getWritableDatabase();
        //create content values object
        ContentValues values = new ContentValues();
        //Build the row
        //values.put(KEY_ID, newTask.getTaskId());	//task id - mark as remark because the id is auto generated 
        values.put(KEY_NAME, newTask.getTaskName()); 								// task Name
        values.put(KEY_DESC, newTask.getTaskDescription()); 						// task description
        values.put(KEY_ST_YR, newTask.getStartDate().get(Calendar.YEAR)); 			// task start year
        values.put(KEY_ST_MON, newTask.getStartDate().get(Calendar.MONTH)); 		// task start month
        values.put(KEY_ST_DAY, newTask.getStartDate().get(Calendar.DAY_OF_MONTH));	// task start day
        values.put(KEY_ST_HOUR, newTask.getStartDate().get(Calendar.HOUR)); 		// task start hour
        values.put(KEY_ST_MIN, newTask.getStartDate().get(Calendar.MINUTE));		// task start minutes
        values.put(KEY_DUE_YR, newTask.getStartDate().get(Calendar.YEAR));			// task end year
        values.put(KEY_DUE_MON, newTask.getStartDate().get(Calendar.MONTH));		// task end year
        values.put(KEY_DUE_DAY, newTask.getStartDate().get(Calendar.DAY_OF_MONTH));	// task end year
        values.put(KEY_DUE_HOUR, newTask.getStartDate().get(Calendar.HOUR));		// task end year
        values.put(KEY_DUE_MIN, newTask.getStartDate().get(Calendar.MINUTE));		// task end year
        values.put(KEY_NOT, newTask.getNotifyFlag()); 								// Contact Name
        values.put(KEY_IMP, newTask.getImportancy().toString()); 					// Contact Name
        values.put(KEY_LAT, newTask.getTaskLat());									// Contact Name
        values.put(KEY_LON, newTask.getTaskLong()); 								// Contact Name
        values.put(KEY_DONE, newTask.isDone()); 									// Contact Name
        // Insert the Row
        long id = db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
        //after task added to db - update tasks list
        syncDal();
        
		return id;
	}
    
    //delete single task from db
	@SuppressWarnings("unchecked")
	public void deleteTask(Task task)
	{
		//delete task from db
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_TASKS, KEY_ID + " = ?",
	            new String[] { String.valueOf(task.getTaskId()) });
	    db.close();
	    //delete task from local list
	    int i = 0;
	    //create mirror list for safe iterating & deleting - on the fly - deep copy
	    ArrayList<Task> tmpTasksList = new ArrayList<Task>(tasksList);
		for ( Iterator<Task> iterator= tasksList.iterator(); iterator.hasNext(); ) 
		{
			Task currTask = (Task) iterator.next();
			if( currTask.getTaskId() == task.getTaskId() )
			{
				//once a match found - delete from db, resort & return true to caller
				tmpTasksList.remove(i);
			}
			i++;
		}
		Collections.sort(tmpTasksList);
		tasksList = tmpTasksList;
		((My_Todo_App)mainListActivityContext).onResume();
	}
	
	
	//sync all tasks from db to local array
	public void syncDal()
	{
		ArrayList<Task> tasksList = new ArrayList<Task>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do 
			{
				// Adding task to list
				tasksList.add(parseTaskFromRow(cursor));
			}
			while (cursor.moveToNext());
		}
		db.close();
		// update local contact list
		this.tasksList = tasksList;
	}

	
	private Task parseTaskFromRow(Cursor cursor)
	{
		 //create start calendar object
	    GregorianCalendar start = new GregorianCalendar(Integer.parseInt(cursor.getString(3)),
	    		Integer.parseInt(cursor.getString(4)),
	    		Integer.parseInt(cursor.getString(5)),
	    		Integer.parseInt(cursor.getString(6)),
	    		Integer.parseInt(cursor.getString(7)),
	    		0); 
	    GregorianCalendar due = new GregorianCalendar(Integer.parseInt(cursor.getString(8)),
	    		Integer.parseInt(cursor.getString(9)),
	    		Integer.parseInt(cursor.getString(10)),
	    		Integer.parseInt(cursor.getString(11)),
	    		Integer.parseInt(cursor.getString(12)),
	    		0); 
	    Task task = new Task(
	    		Integer.parseInt(cursor.getString(0)),		//id
	            cursor.getString(1),						//name
	            cursor.getString(2),						//description
	            start,										//start calendar
	            due,										//due calendar
	            (cursor.getInt(13) == 1),	//notify flag
	            Importancy.valueOf(cursor.getString(14)),	//Importance enum
	            Double.parseDouble(cursor.getString(15)),	//lat
	            Double.parseDouble(cursor.getString(16)),    //long
	            (cursor.getInt(17) == 1)	//done flag
	            );
	    return task;
	}
}
