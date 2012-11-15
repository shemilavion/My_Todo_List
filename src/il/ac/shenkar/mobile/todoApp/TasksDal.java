package il.ac.shenkar.mobile.todoApp;

import java.util.Collection;

public interface TasksDal 
{
	public abstract boolean addTask(Task newTask);
	public abstract boolean deleteTask(Task task);
	public abstract int GetSize();
	public abstract Task GetTask(int position);
	public abstract Task GetTask(String taskName);
	public abstract Collection<Task> getAllTasks();
}
