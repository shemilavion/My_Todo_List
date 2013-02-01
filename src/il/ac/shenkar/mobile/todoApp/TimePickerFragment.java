package il.ac.shenkar.mobile.todoApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.example.my_todo_app.R;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener 
{
	GregorianCalendar cal = new GregorianCalendar();
	//constructor
	public TimePickerFragment()
	{
		cal.setTimeInMillis(System.currentTimeMillis()+1800000);
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current time as the default time in the picker + 30 min
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(c.getTimeInMillis()+1800000);
		int hour = c.get(Calendar.HOUR);
		int minuts = c.get(Calendar.MINUTE);
		// Create a new instance of DatePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minuts, true);
	}
	
	public void onTimeSet(TimePicker view, int hour, int minute) 
	{
		Button time = (Button) getActivity().findViewById(R.id.new_task_time);
		//create calendar object & update the  hour & minute values
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		time.setText(sdf.format(cal.getTime()));
	}
	// get the current selected time 
	public GregorianCalendar getTime()
	{
		return cal;
	}

	
}