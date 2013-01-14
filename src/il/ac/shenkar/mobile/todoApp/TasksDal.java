package il.ac.shenkar.mobile.todoApp;


public interface TasksDal 
{
	public abstract boolean addTask(Task newTask);
	public abstract void syncDal();
	public abstract void deleteTask(Task task);
	public abstract int getTasksCount();
	public abstract Task getTask(int position);
	public abstract Task getTaskById(int id);
	
}
