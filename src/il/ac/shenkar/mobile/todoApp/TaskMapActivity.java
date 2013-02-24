package il.ac.shenkar.mobile.todoApp;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;




public class TaskMapActivity extends FragmentActivity 
{
  private static  LatLng TASKWAYPOINT = new LatLng(53.558, 9.927); 	//default location 
  Dal tasksDal = Dal.getDal(this);
  private GoogleMap googleMap;
  SupportMapFragment mMapFragment;
  Task spottedTask = null;
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
	  //first verify that google location services are available
	if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_map);
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Try to obtain the map from the SupportMapFragment.
		mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		// Not found so make a new instance and add it to the transaction for swapping
		if (mMapFragment == null) 
		{
		   mMapFragment = SupportMapFragment.newInstance();
		   ft.add(R.id.map, mMapFragment, "MAP_FRAG_NAME");
		}
		ft.commit();
		Intent incomeIntent = this.getIntent();
        int taskToEditId = incomeIntent.getIntExtra("edit_task_id", -1);
        if(taskToEditId != -1)
        {
        	spottedTask = tasksDal.getTaskById(taskToEditId);
        	if(spottedTask != null)
        	{
        		TASKWAYPOINT = new LatLng(spottedTask.getTaskLat(),spottedTask.getTaskLong());	
        	}
        }    
	}
  }
  @Override
  public void onAttachedToWindow()
  {
	    // Load the map here so that the fragment has a chance to completely load or else the GoogleMap value may be null
	    googleMap = (mMapFragment).getMap();
	    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    googleMap.addMarker(new MarkerOptions()
	        .position(TASKWAYPOINT)
	        .title(getString(R.string.task_spot))
	        .snippet(spottedTask.getTaskName())
	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	    googleMap.getUiSettings().setCompassEnabled(true);
	    googleMap.getUiSettings().setZoomControlsEnabled(true);
	    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
	    googleMap.setMyLocationEnabled(true);
	    //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TASKWAYPOINT, 15));
	    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(TASKWAYPOINT, 15));
	    super.onAttachedToWindow();
	}
}