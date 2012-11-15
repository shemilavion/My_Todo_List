package il.ac.shenkar.mobile.todoApp;

import com.example.my_todo_app.R;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskListBaseAdapter extends BaseAdapter
{
	private Dal taskDal = null;
	private LayoutInflater l_Inflater;
	
	//constructor
	public TaskListBaseAdapter(Context context) 
	{
		taskDal =  Dal.getDal();	//call static method to get single tone dal object 
		l_Inflater = LayoutInflater.from(context);
	}
	
	public int getCount() 
	{
		return taskDal.GetSize();
	}

	public Object getItem(int position) 
	{
		return taskDal.GetTask(position);
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
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_taskName.setText(taskDal.GetTask(position).getTaskName());
		//make text strike-thru if task is done & check box checked
		if(taskDal.GetTask(position).isDone())
		{
			holder.txt_taskName.setPaintFlags(holder.txt_taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
		}
		else
		{
			holder.txt_taskName.setPaintFlags(holder.txt_taskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
		}
		//set the task nae as the checkbox tag - so we can interpret a checkbox pressure to task name
		holder.chk_isDone.setTag(taskDal.GetTask(position).getTaskName());
		holder.chk_isDone.setChecked(taskDal.GetTask(position).isDone());
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView txt_taskName;
		CheckBox chk_isDone;
	}
	
}
