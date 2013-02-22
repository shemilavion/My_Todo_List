package il.ac.shenkar.mobile.todoApp;

import com.example.my_todo_app.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskListBaseAdapter extends BaseAdapter
{
	private Dal taskDal = null;
	private LayoutInflater l_Inflater;
	Context cont;
	
	//constructor
	public TaskListBaseAdapter(Context context) 
	{
		cont = context;
		taskDal =  Dal.getDal(cont);	//call static method to get single tone dal object 
		l_Inflater = LayoutInflater.from(context);
	}
	
	public int getCount() 
	{
		return taskDal.getTasksCount();
	}

	public Object getItem(int position) 
	{
		
		return taskDal.getTask(position);
	}

	public long getItemId(int position)
	{
		return position;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null) 
		{
			convertView = l_Inflater.inflate(R.layout.list_item_view, null);
			holder = new ViewHolder();
			holder.txt_taskName = (TextView) convertView.findViewById(R.id.Task_name);
			holder.chk_isDone = (CheckBox) convertView.findViewById(R.id.done_flag);
			holder.del_button = (Button) convertView.findViewById(R.id.delete_button);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_taskName.setText(taskDal.getTask(position).getTaskName());
		//make text strike-thru if task is done & check box checked
		if(taskDal.getTask(position).isDone())
		{
			holder.txt_taskName.setPaintFlags(holder.txt_taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
			//set done task text to green
			holder.txt_taskName.setTextColor(Color.GREEN);
		}
		else
		{
			holder.txt_taskName.setPaintFlags(holder.txt_taskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			//check if task is late
			if(taskDal.getTask(position).getDueDate().getTimeInMillis() < System.currentTimeMillis())
			{
				//set done task text to green
				holder.txt_taskName.setTextColor(Color.RED);				
			}
			else
			{
				//set text color to white
				holder.txt_taskName.setTextColor(Color.WHITE);
			}
		}

		//set the task id as the checkbox tag - so we can interpret a checkbox pressure to task id
		int taskId = taskDal.getTask(position).getTaskId();
		holder.chk_isDone.setTag(taskId);
		holder.chk_isDone.setChecked(taskDal.getTask(position).isDone());
		//set the task id as the textfield tag - so we can interpret the pressure
		holder.txt_taskName.setTag(taskId);
		//set the task id as the delete button tag so we can determine which task to delete
		holder.del_button.setTag(taskId);
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView txt_taskName;
		CheckBox chk_isDone;
		Button	 del_button;
	}
	
}
