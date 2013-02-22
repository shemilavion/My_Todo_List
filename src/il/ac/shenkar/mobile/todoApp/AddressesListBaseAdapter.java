package il.ac.shenkar.mobile.todoApp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.my_todo_app.R;

public class AddressesListBaseAdapter extends BaseAdapter
{
	private List<Address> addreses = new ArrayList<Address>();
	private LayoutInflater l_Inflater;
	private Context cont;
	//constructor
	public AddressesListBaseAdapter(Context context) 
	{
		cont = context;
		l_Inflater = LayoutInflater.from(context);
	}
	
	public int getCount() 
	{
		return addreses.size();
	}

	public Object getItem(int position) 
	{
		
		return addreses.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}
	//update adapter data-set
	public void setDataSet(List<Address> add)
	{
		addreses = add;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null) 
		{
			convertView = l_Inflater.inflate(R.layout.address_list_item_view, null);
			holder = new ViewHolder();
			holder.txt_address = (TextView) convertView.findViewById(R.id.address);
			holder.txt_state = (TextView) convertView.findViewById(R.id.state);
			holder.txt_distance = (TextView) convertView.findViewById(R.id.distance);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		//set address to list item
		holder.txt_address.setText(addreses.get(position).getAddressLine(0));
		holder.txt_state.setText(addreses.get(position).getAddressLine(1));
		holder.txt_distance.setText("test1");
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView txt_address;
		TextView txt_state;
		TextView txt_distance;
	}
	
}
