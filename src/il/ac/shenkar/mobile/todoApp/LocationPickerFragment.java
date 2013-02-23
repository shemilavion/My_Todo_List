package il.ac.shenkar.mobile.todoApp;

import java.io.IOException;
import java.util.List;
import com.example.my_todo_app.R;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



@SuppressLint({ "NewApi", "ValidFragment" })
public class LocationPickerFragment extends DialogFragment
{
	private Context caller;
	private ViewGroup contain;
	private Geocoder geoc;
	private List<Address> addreses ;
	private Address selectedAdd=null;
	//the max number of addresses that will be returned by geocoder
	private final int geocoderMaxQueryNum = 25;
	public interface ConfirmDialogCompliant 
	{
        public void doOkConfirmClick();
        public void doCancelConfirmClick();
	}
    public LocationPickerFragment(Context caller)
    {
        super();
        this.caller = caller;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	contain = container;
	    final View view = inflater.inflate(R.layout.address_picker, container, false);
	    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
	    ((Button) view.findViewById(R.id.search_button)).setOnClickListener(new OnClickListener() 
	    {
	        public void onClick(View v) 
	        {
	            // When button is clicked, call relevant method
	            onSearchPressed();
	        }
	    });
	    //set the addresses list view
	    final ListView addressesListView = (ListView)view.findViewById(R.id.listV_addreses);
        //set the listView adapter
	    addressesListView.setAdapter(new AddressesListBaseAdapter(caller));
        //item click listener
	    addressesListView.setOnItemClickListener(new OnItemClickListener() 
		{
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{ 
        		Object o = addressesListView.getItemAtPosition(position);
        		selectedAdd = (Address)o;
        		((NewTaskActivity)caller).updateLocation(selectedAdd);
        		dismiss();
        	}
        });

	    return view;
    }

	public void onSearchPressed()
	{
		//hide the soft keyboard
	    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		//create geocoder
		geoc = new Geocoder(caller);
		//get address query string
		EditText editText = (EditText) getView().findViewById(R.id.task_address);
		String addQuery = editText.getText().toString();
		try 
		{
			 addreses = geoc.getFromLocationName(addQuery, geocoderMaxQueryNum);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final ListView addressesListView = (ListView) getView().findViewById(R.id.listV_addreses);
		//update addresses
		((AddressesListBaseAdapter)addressesListView.getAdapter()).setDataSet(addreses);
        //set the listView adapter
		((BaseAdapter) addressesListView.getAdapter()).notifyDataSetChanged();
	}
	@Override
	public void onStop() 
	{
		//dismiss the soft keyboard
		((NewTaskActivity)caller).closeKeyBoard();
		super.onStop();
	}	

}
