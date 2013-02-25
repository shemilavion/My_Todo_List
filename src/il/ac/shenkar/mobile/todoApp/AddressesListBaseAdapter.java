package il.ac.shenkar.mobile.todoApp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AddressesListBaseAdapter extends BaseAdapter
{
	private List<Address> addreses = new ArrayList<Address>();
	private LayoutInflater l_Inflater;
	private Context cont;
	private final int fixUpdateConst = 30000;
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
		
		//calculate distance to destination from my location
		//get the location service & create criteria
		boolean legalFix = false;
		LocationManager locMan = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);
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
				legalFix = true;
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
				legalFix = true;
			}
		}
	//DEBUG only! print fix update time 
//		GregorianCalendar fixCal = new GregorianCalendar();
//		fixCal.setTimeInMillis(fixTime);
//		SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMMM,yyyy - HH:mm",Locale.CANADA);
//		System.out.println(sdf.format(fixCal.getTime()));
	//END OF DEBUG ONLY SECTION
		//check fix relevance
		if( (System.currentTimeMillis() - fixTime) < fixUpdateConst && legalFix == true)
		{
			Location dest = new Location(provider);
			dest.setLatitude(addreses.get(position).getLatitude());
			dest.setLongitude(addreses.get(position).getLongitude());
			double distance = loc.distanceTo(dest);
			distance = distance/1000;
			DecimalFormat df = new DecimalFormat("#.#");
			String dis=df.format(distance);
			holder.txt_distance.setText(dis + cont.getString(R.string.address_distance_suffix));
		}
		//if fix is not relevant - ignore the distance
		else
		{
			holder.txt_distance.setText("");
		}
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView txt_address;
		TextView txt_state;
		TextView txt_distance;
	}
	
}
