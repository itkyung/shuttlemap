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
import com.shuttlemap.android.MapRouteActivity.MapDataTask;
import com.shuttlemap.android.adapter.RouteListAdapter;
import com.shuttlemap.android.common.bitmapDownloader.BitmapDownloader;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.map.MapDataSet;
import com.shuttlemap.android.map.MapService;
import com.shuttlemap.android.map.Placemark;
import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.server.entity.RouteEntity;
import com.shuttlemap.android.server.entity.ShuttleEntity;
import com.shuttlemap.android.server.handler.ShuttleHandler;
import com.shuttlemap.android.views.dialog.BookmarkDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShuttleDetailActivity extends ShuttlemapBaseActivity implements LocationListener {
	private Context context;
	private ShuttleEntity shuttle;
	private GoogleMap googleMap;

	private LocationManager locationManager;
	private String provider;
	
	private int currentTab = 1;
	private static int BASIC_TAB = 1;
	private static int ROUTE_TAB = 2;
	
	private View normalInfo;
	private View routeContainer;
	private ListView routeList;
	BitmapFactory.Options bitmapOption;
	private ImageButton tab1;
	private ImageButton tab2;
	private RouteListAdapter adapter;
	private boolean isDrawPath = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = this;
		
		Intent intent = getIntent();
		this.shuttle = intent.getParcelableExtra("shuttle");
		
		setContentView(R.layout.activity_shuttle_detail);
		
		
		bitmapOption = new BitmapFactory.Options();
		bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOption.inSampleSize = 2;
		bitmapOption.inDither = false;
		
		TextView shuttleLabel = (TextView)findViewById(R.id.shuttleLabel);
		shuttleLabel.setText(shuttle.getShuttleName());
		
		tab1 = (ImageButton)findViewById(R.id.tvMenu1R);
		tab2 = (ImageButton)findViewById(R.id.tvMenu2R);
		
		tab1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(BASIC_TAB);
			}
		});
		tab2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(ROUTE_TAB);
			}
		});
		
		this.normalInfo = findViewById(R.id.normalInfo);
		this.routeList = (ListView)findViewById(R.id.routeList);
		this.adapter = new RouteListAdapter(this);
		routeList.setAdapter(adapter);
		this.routeContainer = findViewById(R.id.routeContainer);
		
		findViewById(R.id.btnBookmark).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				BookmarkDialog dialog = new BookmarkDialog(context,shuttle.getId());
				dialog.show();
				
			}
		});
		
		findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(ShuttleDetailActivity.this,MapRouteActivity.class);
				intent.putExtra("kmlUrl", ServerStaticVariable.KML_PREFIX + shuttle.getRouteFilePath());
				intent.putExtra("routeName", shuttle.getShuttleName());
				startActivity(intent);
			}
		});
		
		findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshRoutes();
//				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shuttle.getDriverPhone()));
//				startActivity(intent);
			}
		});
		
		updateMapInfo();
		refreshRoutes();
		
	}
	
	private void updateMapInfo(){
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
        
        String kmlUrl = ServerStaticVariable.KML_PREFIX + shuttle.getRouteFilePath();
		
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
	
	private void changeTab(int tab){
		currentTab = tab;
		
		if(tab == BASIC_TAB){
			tab1.setImageResource(R.drawable.m1_tab01_p);
			tab2.setImageResource(R.drawable.m1_tab02_n);
			
			normalInfo.setVisibility(View.VISIBLE);
			routeContainer.setVisibility(View.GONE);
			
		}else{
			tab1.setImageResource(R.drawable.m1_tab01_n);
			tab2.setImageResource(R.drawable.m1_tab02_p);
			
			normalInfo.setVisibility(View.GONE);
			routeContainer.setVisibility(View.VISIBLE);
			
		}
	}
	
	private void refreshRoutes(){
		GetRouteListTask task = new GetRouteListTask();
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,shuttle.getId());
		}else{
			task.execute(shuttle.getId());				
		}
	}
	
	class GetRouteListTask extends AsyncTask<String, Void, ArrayList<RouteEntity>>{

		@Override
		protected ArrayList<RouteEntity> doInBackground(String... params) {
			return ShuttleHandler.getRoutes(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<RouteEntity> result) {
			
			super.onPostExecute(result);
			adapter.setRoutes(result);
			adapter.notifyDataSetChanged();
			
		}
		
	}
	
}
