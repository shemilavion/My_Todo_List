package il.ac.shenkar.mobile.todoApp;

import java.util.ArrayList;

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
	private static ArrayList<Task> tasksDetailsrrayList;
	private LayoutInflater l_Inflater;
	
	//constructor
	public TaskListBaseAdapter(Context context, ArrayList<Task> results) 
	{
		tasksDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}
	
	public int getCount() 
	{
		return tasksDetailsrrayList.size();
	}

	public Object getItem(int position) 
	{
		return tasksDetailsrrayList.get(position);
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
		holder.txt_taskName.setText(tasksDetailsrrayList.get(position).getTaskName());
		//make text strike-thru if task is done & check box checked
		if(tasksDetailsrrayList.get(position).isDone())
		{
			holder.txt_taskName.setPaintFlags(holder.txt_taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
		}
		else
		{
			holder.txt_taskName.setPaintFlags(holder.txt_taskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
		}
		holder.chk_isDone.setChecked(tasksDetailsrrayList.get(position).isDone());
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView txt_taskName;
		CheckBox chk_isDone;
	}
	
}
