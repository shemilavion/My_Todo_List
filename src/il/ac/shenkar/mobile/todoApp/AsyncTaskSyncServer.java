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
import android.content.Context;
import android.os.AsyncTask;

/*
 * this class intended to grab task data from server & insert a new task into tasks db
 * */
public class AsyncTaskSyncServer extends AsyncTask<URL, Integer, String>
{
    private Context mContext;
    Dal tasklDal;
    public AsyncTaskSyncServer(Context context)
    {
        mContext = context;
        tasklDal = Dal.getDal(mContext);
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
	
	//create the json from the server response & create a new task from it
	@Override
	protected void onPostExecute(String result) 
	{
		super.onPostExecute(result);
		JSONObject taskJson = null;
		Task randomTask = new Task();
		try 
		{
			taskJson = new JSONObject(result);
			randomTask.setTaskName(taskJson.getString("topic"));
			randomTask.setTaskDescription(taskJson.getString("description"));
			tasklDal.addTask(randomTask);
		} 
		catch (JSONException e) 
		{
			publishProgress();
			return;
		}
		
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
