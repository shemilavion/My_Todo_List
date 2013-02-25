package il.ac.shenkar.mobile.todoApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * this class is the data access layer for the tasks
 * */
public class Dal extends SQLiteOpenHelper implements TasksDal 
{
	private final int fixUpdateConst = 30000;
	private final int topDistnace = 999999999;
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
    private static final String KEY_SERVER_ID= 	"server_id";
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
    	Log.i("DAL", "Creating DataBase");
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
        		+ KEY_ID   	     + " INTEGER PRIMARY KEY AUTOINCREMENT,"	//the id of each task must be unique - so its defined as AUTOINCREMENT
        		+KEY_SERVER_ID	 + " TEXT,"
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
        Log.i("DAL", "DataBase was created");
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    	Log.i("DAL", "upgrading DataBase");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        // Create tables again
        onCreate(db);
        Log.i("DAL", "DataBase was upgrade");
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
	//get the task by server id
	public Task getTaskByServerId(String serverId)
	{
		for ( Iterator<Task> iterator= tasksList.iterator(); iterator.hasNext(); ) 
		{
			Task currTask = (Task) iterator.next();
			if( currTask.getServerId().equals(serverId) )
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
		Log.i("DAL", "Adding new task to DB");
    	//get the database object
        SQLiteDatabase db = this.getWritableDatabase();
        //create content values object
        ContentValues values = new ContentValues();
        //Build the row
        //values.put(KEY_ID, newTask.getTaskId());	//task id - mark as remark because the id is auto generated 
        values.put(KEY_SERVER_ID, newTask.getServerId()); 							// task Server id
        values.put(KEY_NAME, newTask.getTaskName()); 								// task Name
        values.put(KEY_DESC, newTask.getTaskDescription()); 						// task description
        values.put(KEY_ST_YR, newTask.getStartDate().get(Calendar.YEAR)); 			// task start year
        values.put(KEY_ST_MON, newTask.getStartDate().get(Calendar.MONTH)); 		// task start month
        values.put(KEY_ST_DAY, newTask.getStartDate().get(Calendar.DAY_OF_MONTH));	// task start day
        values.put(KEY_ST_HOUR, newTask.getStartDate().get(Calendar.HOUR_OF_DAY)); 		// task start hour
        values.put(KEY_ST_MIN, newTask.getStartDate().get(Calendar.MINUTE));		// task start minutes
        values.put(KEY_DUE_YR, newTask.getDueDate().get(Calendar.YEAR));			// task end year
        values.put(KEY_DUE_MON, newTask.getDueDate().get(Calendar.MONTH));			// task end year
        values.put(KEY_DUE_DAY, newTask.getDueDate().get(Calendar.DAY_OF_MONTH));	// task end year
        values.put(KEY_DUE_HOUR, newTask.getDueDate().get(Calendar.HOUR_OF_DAY));			// task end year
        values.put(KEY_DUE_MIN, newTask.getDueDate().get(Calendar.MINUTE));			// task end year
        values.put(KEY_NOT, newTask.getNotifyFlag()); 								// Contact Name
        values.put(KEY_IMP, newTask.getImportancy().toString()); 					// Contact Name
        values.put(KEY_LAT, newTask.getTaskLat());									// Contact Name
        values.put(KEY_LON, newTask.getTaskLong()); 								// Contact Name
        values.put(KEY_DONE, newTask.isDone()); 									// Contact Name
        // Insert the Row
        long id = db.insert(TABLE_TASKS, null, values);
        Log.i("DAL", "nwe task was added to DB");
        db.close(); // Closing database connection
        //after task added to db - update tasks list
        syncDal();
        ((My_Todo_App)mainListActivityContext).onResume();
		return id;
	}
    
    //delete single task from db
	public void deleteTask(Task task)
	{
		Log.i("DAL", "Deleting task "+ task.getTaskName());
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
		Log.i("DAL", "Task: "+task.getTaskName()+" was deleted");
		this.sortTasks(((My_Todo_App)mainListActivityContext).sortingMannor);
		tasksList = tmpTasksList;
		((My_Todo_App)mainListActivityContext).onResume();
	}
	
	
	//sync all tasks from db to local array
	public void syncDal()
	{
		Log.i("DAL", "sync all tasks from db to local array");
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
		Log.i("DAL", "sync is done");
	}

	
	private Task parseTaskFromRow(Cursor cursor)
	{
		 //create start calendar object
	    GregorianCalendar start = new GregorianCalendar(Integer.parseInt(cursor.getString(4)),
	    		Integer.parseInt(cursor.getString(5)),
	    		Integer.parseInt(cursor.getString(6)),
	    		Integer.parseInt(cursor.getString(7)),
	    		Integer.parseInt(cursor.getString(8)),
	    		0); 
	    GregorianCalendar due = new GregorianCalendar(Integer.parseInt(cursor.getString(9)),
	    		Integer.parseInt(cursor.getString(10)),
	    		Integer.parseInt(cursor.getString(11)),
	    		Integer.parseInt(cursor.getString(12)),
	    		Integer.parseInt(cursor.getString(13)),
	    		0); 
	    Task task = new Task(
	    		Integer.parseInt(cursor.getString(0)),		//id
	    		cursor.getString(1),						//server id
	            cursor.getString(2),						//name
	            cursor.getString(3),						//description
	            start,										//start calendar
	            due,										//due calendar
	            (cursor.getInt(14) == 1),	//notify flag
	            Importancy.valueOf(cursor.getString(15)),	//Importance enum
	            Double.parseDouble(cursor.getString(16)),	//lat
	            Double.parseDouble(cursor.getString(17)),    //long
	            (cursor.getInt(18) == 1)	//done flag
	            );
	    return task;
	}
	public void sortTasks(SortingMannor sortMannor)
	{
		if(sortMannor != SortingMannor.BY_DUE_DATE && sortMannor != SortingMannor.BY_NEAREST_LOCATION &&
				sortMannor != SortingMannor.BY_HIGHER_IMPORTANCY )
		{
			sortMannor = SortingMannor.BY_DUE_DATE;
		}
		final SortingMannor srtMnr = sortMannor;
		Collections.sort(tasksList, new Comparator<Task>()
				{
					int toastCount = 1;
			  		public int compare(Task task1, Task task2) 
			  		{
			  			//first put all done tasks at the end
			  			int ans = 0;
			  			if(task1.isDone())
			  			{
			  				return 1;
			  			}
			  			if(task2.isDone())
			  			{
			  				return -1;
			  			}	
			  			//now decide which sorting method taking place
			  			switch(srtMnr)
			  			{
			  				case BY_DUE_DATE:
			  				{
			  					if(task1.getDueDate().getTimeInMillis() < task2.getDueDate().getTimeInMillis())
			  					{
			  						ans = 1;
			  					}
			  					else
			  					{
			  						ans = -1;
			  					}
			  					break;
			  				}
			  				case BY_HIGHER_IMPORTANCY:
			  				{
			  					if(task1.getImportancy().ordinal() < task2.getImportancy().ordinal())
			  					{
			  						ans = 1;
			  					}
			  					else
			  					{
			  						ans = -1;
			  					}
			  					break;
			  				}
			  				case BY_NEAREST_LOCATION:
			  				{
			  					double distance1=1, distance2=2;
			  					boolean validFix = false;
			  					if(task1.getTaskLat() == -1)
			  					{
			  						distance1 = topDistnace;
			  					}
			  					if(task2.getTaskLat() == -1)
			  					{
			  						distance2 = topDistnace;
			  					}
			  					//calculate distance to destination from my location
			  					//get the location service & create criteria
			  					LocationManager locMan = (LocationManager) mainListActivityContext
			  							.getSystemService(Context.LOCATION_SERVICE);
			  					Criteria crit = new Criteria();
			  					//set accuracy to fine - try GPS first...
			  					crit.setAccuracy(Criteria.ACCURACY_FINE);
			  					String provider = locMan.getBestProvider(crit, true);
			  					Location loc = null;
			  					long fixTime = 0;
				  				//if gps location avail...
								if(provider != null)
								{
									loc = locMan.getLastKnownLocation(provider);
									if(loc != null)
									{
										fixTime = loc.getTime();
										validFix = true;
									}
								}
								//set accuracy to course - try network now...
			  					crit.setAccuracy(Criteria.ACCURACY_COARSE);
			  					provider = locMan.getBestProvider(crit, true);
			  					if(provider != null)
			  					{
			  						loc = locMan.getLastKnownLocation(provider);
			  						if(loc != null)
			  						{
			  							long netFixTime = loc.getTime();
			  							if(netFixTime > fixTime)
			  							{
			  								fixTime = netFixTime;
			  							}
			  							validFix = true;
			  						}
			  					}
			  				//DEBUG only! print fix update time 
//			  					GregorianCalendar fixCal = new GregorianCalendar();
//			  					fixCal.setTimeInMillis(fixTime);
//			  					SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMMM,yyyy - HH:mm",Locale.CANADA);
//			  					System.out.println(sdf.format(fixCal.getTime()));
			  				//END OF DEBUG ONLY SECTION
			  					//check fix relevance
			  					if( (System.currentTimeMillis() - fixTime) < fixUpdateConst && validFix == true)
			  					{
			  						Location dest = new Location(provider);
			  						dest.setLatitude(task1.getTaskLat());
			  						dest.setLongitude(task1.getTaskLong());
			  						if(distance1 != topDistnace)
			  						{
			  							distance1 = loc.distanceTo(dest);
			  						}
			  						dest.setLatitude(task2.getTaskLat());
			  						dest.setLongitude(task2.getTaskLong());
			  						if(distance1 != topDistnace)
			  						{
			  							distance2 = loc.distanceTo(dest);
			  						}
			  					}
			  					else
			  					{
			  						if(toastCount >0 )
			  						{
				  						CharSequence text = mainListActivityContext.getString(R.string.loc_prob);
				  						int duration = Toast.LENGTH_SHORT;
				  						Toast toast = Toast.makeText(mainListActivityContext, text, duration);
				  						toast.show();
				  						toastCount--;
			  						}
			  						return 1;
			  					}
			  					if(distance1 > distance2)
			  					{
			  						ans = 1;
			  					}
			  					else
			  					{
			  						ans = -1;
			  					}
			  					break;
			  				}
			  			}
			  			return ans;
			  		}
			  });
		
	}
}
