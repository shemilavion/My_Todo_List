package il.ac.shenkar.mobile.todoApp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.my_todo_app.R;



import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncTaskWebServer extends AsyncTask<URL, Integer, String>
{

    private Context mContext;

    public AsyncTaskWebServer(Context context)
    {
        mContext = context;
    } 
	@Override
	protected String doInBackground(URL... params)
	{
		//call method to get url response
		try 
		{
			return getResponseFromUrl(params[0]);
		} 
		catch (IOException e) 
		{
			//case there is an error connecting the server - notify user & proceed
			publishProgress();	
			return null;
		} 
		
	}
	
	
	@Override
	protected void onPostExecute(String result) 
	{
		if(result.equals(""))
		{
			Toast.makeText(mContext,mContext.getString(R.string.random_con_err), Toast.LENGTH_LONG).show();	
		}
		super.onPostExecute(result);
		JSONObject taskJson = null;
		Task randomTask = new Task();
		try 
		{
			taskJson = new JSONObject(result);
			randomTask.setTaskName(taskJson.getString("topic"));
			randomTask.setTaskDescription(taskJson.getString("description"));
			((NewTaskActivity)mContext).fillTaskForm(randomTask);
			//stop the progress bar
			((NewTaskActivity)mContext).myProgressDialogStop();
		} 
		catch (JSONException e) 
		{
			publishProgress();
			return;
		}
		
	}
	protected void onProgressUpdate(Integer... progress) 
	{
		Toast.makeText(mContext,mContext.getString(R.string.random_con_err), Toast.LENGTH_LONG).show();
	}
 	//the method gets a url connection & returns the response from the url
 	private String getResponseFromUrl(URL tasksServerUrl) throws IOException
 	{
    	//open the connection
		HttpURLConnection urlConnection = (HttpURLConnection)tasksServerUrl.openConnection();
 		//get the input stream & reader
 		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
 		InputStreamReader inReader = new InputStreamReader(in);
 		BufferedReader bufferedReader = new	BufferedReader(inReader);
 		StringBuilder responseBuilder = new StringBuilder();
 		//read the input steram into stringbuilder & return it
 		for(String line=bufferedReader.readLine();line!=null;line=bufferedReader.readLine())
 		{
 			responseBuilder.append(line);
 		}
 		//create the json from the response string
 		return responseBuilder.toString();
 	}

}
