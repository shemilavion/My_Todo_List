package il.ac.shenkar.mobile.todoApp;

import java.util.GregorianCalendar;

@SuppressWarnings("rawtypes")
public class Task implements Comparable
{

	//define task fields
	private	int					taskId;
	private String				serverId = "";
	private String 				taskName;
	private String 				taskDescription;
	private GregorianCalendar	startDate;
	private GregorianCalendar	dueDate;
	private boolean				notifyFlag;
	private Importancy			importancy;
	//task lat & long initialed to -1 if not inserted
	private double 				taskLat = -1;	
	private double				taskLong = -1;
	private boolean				isDoneFlag = false;
	//Default constructor
	public Task()
	{
		super();
		//initial task start date for current time
		startDate 			= new GregorianCalendar();
		dueDate 			=  new GregorianCalendar();
		//set due date to half an hour from now
		dueDate.setTimeInMillis(dueDate.getTimeInMillis() + 1800000);
		importancy 			= Importancy.NONE;
		notifyFlag 			= false;
	}
	//constructor
	public Task(int id,String serverId, String taskName, String taskDescription, GregorianCalendar startDate,
			GregorianCalendar dueDate, boolean notifyFlag, Importancy importancy,
			double lat, double lon, boolean isDoneFlag) 
	{
		super();
		this.taskId 			= 	id;
		this.serverId			= 	serverId;
		this.taskName 			=	taskName;
		this.taskDescription	= 	taskDescription;
		this.startDate 			= 	startDate;
		this.dueDate 			= 	dueDate;
		this.notifyFlag 		= 	notifyFlag;
		this.importancy 		= 	importancy;
		this.taskLat			=	lat;
		this.taskLong			=	lon;
		this.isDoneFlag 		= 	isDoneFlag;
	}
	//copy constructor
	public Task(Task currTask) 
	{
		super();
		this.taskName = currTask.taskName;
		this.taskDescription = currTask.taskDescription;
		this.startDate = currTask.startDate;
		this.dueDate = currTask.dueDate;
		this.notifyFlag = currTask.notifyFlag;
		this.importancy = currTask.importancy;
		this.isDoneFlag = currTask.isDoneFlag;
	}
	//getters & setters
	public int getTaskId() 
	{
		return taskId;
	}
	public void setTaskId(int taskId)
	{
		this.taskId = taskId;
	}
	public String getTaskName() 
	{
		return taskName;
	}
	
	public String getServerId() 
	{
		return serverId;
	}
	public void setServerId(String serverId)
	{
		this.serverId = serverId;
	}
	public void setTaskName(String taskName) 
	{
		this.taskName = taskName;
	}
	public String getTaskDescription()
	{
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) 
	{
		this.taskDescription = taskDescription;
	}
	public GregorianCalendar getStartDate() 
	{
		return startDate;
	}
	public void setStartDate(GregorianCalendar startDate)
	{
		this.startDate = startDate;
	}
	public GregorianCalendar getDueDate()
	{
		return dueDate;
	}
	public void setDueDate(GregorianCalendar dueDate)
	{
		this.dueDate = dueDate;
	}
	public boolean getNotifyFlag()
	{
		return notifyFlag;
	}
	public void setNotifyFlag(boolean notifyFlag)
	{
		this.notifyFlag = notifyFlag;
	}
	public Importancy getImportancy() 
	{
		return importancy;
	}
	public void setImportancy(Importancy importancy) 
	{
		this.importancy = importancy;
	}
	public boolean isDone()
	{
		return isDoneFlag;
	}
	public double getTaskLat() 
	{
		return taskLat;
	}
	public void setTaskLat(double taskLat) 
	{
		this.taskLat = taskLat;
	}
	public double getTaskLong() 
	{
		return taskLong;
	}
	public void setTaskLong(double taskLong) 
	{
		this.taskLong = taskLong;
	}
	public void setIsDoneFlag(boolean isDoneFlag)
	{
		this.isDoneFlag = isDoneFlag;
	}
	public int compareTo(Object another) 
	{
		Task compTask = (Task)another;
		int returnVal = 0;
		//order by insertion date
		if(compTask.startDate.after(this.startDate) )
		{
			returnVal = -1;
		}
		else if(this.startDate.after(compTask.startDate) )
		{
			returnVal = 1;
		}
		//order by executed
		if(this.isDoneFlag && (!compTask.isDoneFlag))
		{
			returnVal = 1;
		}
		else if((!this.isDoneFlag) && compTask.isDoneFlag)
		{
			returnVal = -1;
		}
		else if(this.isDoneFlag && compTask.isDoneFlag)
		{
			returnVal = 0;
		}
		return returnVal;
	}
	/**
	 * if there is no task name or there is no due date or the due date precedes start date - don't validate the task*/
	public boolean validateTask()
	{
		if(this.taskName.equals("") || this.dueDate == null || (this.startDate.after(this.dueDate)))
		{
			return false;
		}
		return true;
	}
}
