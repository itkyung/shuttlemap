package com.shuttlemap.android;

import java.util.ArrayList;

import com.google.android.gms.common.GooglePlayServicesUtil;
import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.map.MapDataSet;
import com.shuttlemap.android.map.MapService;
import com.shuttlemap.android.map.Placemark;
import com.shuttlemap.android.server.ServerStaticVariable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
	private boolean isDrawPath = false;
	
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
        String title = intent.getStringExtra("routeName");
        TitleBar titleBar = (TitleBar)getFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.setTitle(title);
		
        getRouteKmlData(kmlUrl);
	}
	
	private void getRouteKmlData(String url){
		if(url != null && url.length() > 0){
			if(ServerStaticVariable.KML_PREFIX.equals(url)){
				new AlertDialog.Builder(context)
				 .setTitle("노선 지도 정보가 등록되지 않았습니다.").show();
			}else{
				new MapDataTask().execute(url);
			}
		}
	}
	
	private void drawPath(MapDataSet navigationData){
		
		if(navigationData == null){
			new AlertDialog.Builder(context)
	        .setTitle("노선 지도 정보가 등록되지 않았습니다.").show();
			return;
		}
		isDrawPath = true;
		
		ArrayList<Placemark> routes = navigationData.getPlacemarks();
		for(int i=0; i < routes.size()-1; i++){
			boolean first = false;
			boolean last = false;
			if(i==0){
				first = true;
			}else if(i == routes.size()-2){
				last = true;
			}
			//addMarker(routes.get(i),first, first||last ? true : false);
			addMarker(routes.get(i),first, false);
		}
		String routeCoordinates = navigationData.getCurrentPlacemark().getCoordinates();
		String[] points = routeCoordinates.split(" ");
		for(int i=0;i <  points.length; i++){
			if(i < points.length-1){
				String pointStart = points[i];
				String pointEnd = points[i+1];
				googleMap.addPolyline(new PolylineOptions()
				  .add(getLatLng(pointStart),getLatLng(pointEnd))
				  .width(20)
				  .color(Color.RED));
			}
		}
		
	}
	
	private LatLng getLatLng(String coordinates){
		
		String[] datas = coordinates.split(",");
		double latitude = Double.parseDouble(datas[1]);
		double longitude = Double.parseDouble(datas[0]);
		return new LatLng(latitude, longitude);
	}
	
	private void addMarker(Placemark marker,boolean needMove,boolean changeColor){
		String coordinates = marker.getCoordinates();
		String[] datas = coordinates.split(",");
		if(datas.length == 0){
			return;
		}
		double latitude = Double.parseDouble(datas[1]);
		double longitude = Double.parseDouble(datas[0]);
		LatLng ll = new LatLng(latitude, longitude);
		if(changeColor){
			if(needMove){
				googleMap.addMarker(new MarkerOptions().position(ll).title(marker.getTitle())
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.mk_point)).snippet("출발지"));
			}else{
				googleMap.addMarker(new MarkerOptions().position(ll).title(marker.getTitle())
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.mk_point)).snippet("도착지"));
			}
		}else{
			googleMap.addMarker(new MarkerOptions().position(ll).title(marker.getTitle())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.mk_point)));
		}
		
		if(needMove){
			CameraPosition cp = new CameraPosition.Builder().target(ll).zoom(17f).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
		
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
		if(!isDrawPath){
			googleMap.moveCamera(center);
			googleMap.animateCamera(zoom);
		}
		
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
