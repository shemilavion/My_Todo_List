package il.ac.shenkar.mobile.todoApp;

import java.util.GregorianCalendar;

@SuppressWarnings("rawtypes")
public class Task implements Comparable
{

	//define task fields
	private String 				taskName;
	private String 				taskDescription;
	private GregorianCalendar	startDate;
	private GregorianCalendar	dueDate;
	private boolean				reminderFlag;
	private Importancy			importancy;
	private boolean				isDoneFlag;
	//Default constructor
	public Task()
	{
		super();
		//initial task start date for current time
		startDate = new GregorianCalendar();
	}
	//constructor
	public Task(String taskName, String taskDescription, GregorianCalendar startDate,
			GregorianCalendar dueDate, boolean reminderFlag, Importancy importancy,
			boolean isDoneFlag) {
		super();
		this.taskName = taskName;
		this.taskDescription = taskDescription;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.reminderFlag = reminderFlag;
		this.importancy = importancy;
		this.isDoneFlag = isDoneFlag;
	}
	//copy constructor
	public Task(Task currTask) 
	{
		super();
		this.taskName = currTask.taskName;
		this.taskDescription = currTask.taskDescription;
		this.startDate = currTask.startDate;
		this.dueDate = currTask.dueDate;
		this.reminderFlag = currTask.reminderFlag;
		this.importancy = currTask.importancy;
		this.isDoneFlag = currTask.isDoneFlag;
	}
	//getters & setters
	public String getTaskName() 
	{
		return taskName;
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
	public boolean isReminderFlag()
	{
		return reminderFlag;
	}
	public void setReminderFlag(boolean reminderFlag)
	{
		this.reminderFlag = reminderFlag;
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
	public void setIsDoneFlag(boolean isDoneFlag)
	{
		this.isDoneFlag = isDoneFlag;
	}
	public int compareTo(Object another) 
	{
		Task compTask = (Task)another;
		int returnVal = 0;
		//order by importancy
		//if(compTask.importancy.ordinal() < this.importancy.ordinal() )
		//{
		//	returnVal = -1;
	//	}
	//	else if(compTask.importancy.ordinal() > this.importancy.ordinal() )
	//	{
	//		returnVal = 1;
	//	}
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
