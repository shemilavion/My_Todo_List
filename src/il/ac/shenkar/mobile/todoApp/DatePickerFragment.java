package il.ac.shenkar.mobile.todoApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.example.my_todo_app.R;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

@SuppressLint("NewApi")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
{
	GregorianCalendar cal;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	public void onDateSet(DatePicker view, int year, int month, int day) 
	{
		Button date = (Button) getActivity().findViewById(R.id.new_task_date);
		SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMMM,yyyy");
		cal = new GregorianCalendar( year, month, day );
		date.setText(sdf.format(cal.getTime()));
		
	}


	// get the current selected date 
	public GregorianCalendar getDate()
	{
		return cal;
	}

	
}