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
	public void setIdDoneFlag(boolean isDoneFlag)
	{
		this.isDoneFlag = isDoneFlag;
	}
	public int compareTo(Object another) 
	{
		Task compTask = (Task)another;
		int returnVal = 0;
		if(compTask.importancy.ordinal() < this.importancy.ordinal() )
		{
			returnVal = -1;
		}
		else if(compTask.importancy.ordinal() > this.importancy.ordinal() )
		{
			returnVal = 1;
		}
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
}
