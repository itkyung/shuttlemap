package com.shuttlemap.android;

import java.util.ArrayList;

import com.google.android.gms.common.GooglePlayServicesUtil;
import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.map.MapDataSet;
import com.shuttlemap.android.map.MapService;
import com.shuttlemap.android.map.Placemark;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

public class MapRouteActivity extends ShuttlemapBaseActivity implements LocationListener,TitleBarListener{
	private GoogleMap googleMap;
	private Context context;
	private LocationManager locationManager;
	private String provider;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map_route);
		
		
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
       	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
		
        locationManager.requestLocationUpdates(provider, 1, 1, this);
        
        if(provider != null){
			setUpMapIfNeeded();
			Location location = locationManager.getLastKnownLocation(provider);
        	if(location != null){
        		onLocationChanged(location);
        	}
		}
        
        Intent intent = getIntent();
        String kmlUrl = intent.getStringExtra("kmlUrl");
        getRouteKmlData(kmlUrl);
	}
	
	private void getRouteKmlData(String url){
		new MapDataTask().execute(url);
	}
	
	private void drawPath(MapDataSet navigationData){
		ArrayList<Placemark> routes = navigationData.getPlacemarks();
		for(int i=0; i < routes.size(); i++){
			addMarker(routes.get(i),i==0?true:false);
		}
		
	}
	
	private void addMarker(Placemark marker,boolean needMove){
		String coordinates = marker.getCoordinates();
		String[] datas = coordinates.split(",");
		if(datas.length == 0){
			return;
		}
		double latitude = Double.parseDouble(datas[1]);
		double longitude = Double.parseDouble(datas[0]);
		LatLng ll = new LatLng(latitude, longitude);
		googleMap.addMarker(new MarkerOptions().position(ll).title(marker.getTitle()));
		if(needMove){
			CameraUpdate center = CameraUpdateFactory.newLatLng(ll);
			googleMap.moveCamera(center);
		}
	}
	
	
	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
		CameraUpdate center = CameraUpdateFactory.newLatLng(ll);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
	//	googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);
		
	}

	private void setUpMapIfNeeded() {
		if (googleMap == null) {
			googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			if (googleMap != null) {
				setUpMap();
			}
		}
	}
	    
	private void setUpMap() {
		googleMap.setMyLocationEnabled(true);
		Location location = googleMap.getMyLocation();
		if(location != null){
			
		}
	}

	class MapDataTask extends AsyncTask<String, Void, MapDataSet>{

		@Override
		protected MapDataSet doInBackground(String... params) {
			return MapService.getMapDateSet(params[0]);
		}

		@Override
		protected void onPostExecute(MapDataSet result) {
			super.onPostExecute(result);
			drawPath(result);
		}
		
		
	}
}
