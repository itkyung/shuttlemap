package com.shuttlemap.android.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shuttlemap.android.R;
import com.shuttlemap.android.server.entity.FriendEntity;
import com.shuttlemap.android.server.handler.FriendHandler;

import android.app.Activity;
import android.app.AlertDialog;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ArroundMapFragment extends Fragment implements LocationListener{

	private GoogleMap googleMap;
	private LocationManager locationManager;
    private String provider;
    private Context context;
    boolean locationTag=true;
    
	public ArroundMapFragment(){
		
	}
	
	public static ArroundMapFragment newInstance(){
		ArroundMapFragment fragment = new ArroundMapFragment();
		
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
       	locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        
        if(provider==null){  //위치정보 설정이 안되어 있으면 설정하는 엑티비티로 이동합니다
         	new AlertDialog.Builder(context)
	        .setTitle("위치서비스 동의")
	        .setNeutralButton("이동" ,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
					}
				}).setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						
					}
				}
			).show();
        }else{   //위치 정보 설정이 되어 있으면 현재위치를 받아옵니다
        	
    		locationManager.requestLocationUpdates(provider, 1, 1, this);
        }
	}

	private static View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try{
			view = inflater.inflate(R.layout.fragment_arround, container,false);
		} catch (InflateException e) {
			/* map is already there, just return view as it is */
		}
		if(provider != null){
			setUpMapIfNeeded();
			Location location = locationManager.getLastKnownLocation(provider);
        	if(location != null){
        		onLocationChanged(location);
        	}
			
		}
		
		Button btn = (Button)view.findViewById(R.id.btnRefresh);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GetFriendsTask().execute();
			}
		});
		
		return view;
	}

	@Override
	public void onLocationChanged(Location location) {
		//if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate center = CameraUpdateFactory.newLatLng(ll);
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
			googleMap.moveCamera(center);
			googleMap.animateCamera(zoom);
	    //}
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
		
		new GetFriendsTask().execute();
	}

	DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public void viewFriendsLocation(ArrayList<FriendEntity> friends){
		googleMap.clear();
		for(FriendEntity friend : friends){
			if(friend.getLatitude() > 0) {
				googleMap.addMarker(new MarkerOptions().position(
					new LatLng(friend.getLatitude(), friend.getLongitude())).title(friend.getName())
					.snippet("마지막시간 " + fm.format(friend.getLastLocationDate())));
			}
		}
	}
	
	    
	 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {//위치설정 엑티비티 종료 후 
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			 locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		        Criteria criteria = new Criteria();
		        provider = locationManager.getBestProvider(criteria, true);
		        if(provider==null){//사용자가 위치설정동의 안했을때 종료 
					
			}else{//사용자가 위치설정 동의 했을때 
				locationManager.requestLocationUpdates(provider, 1L, 2F, this);
		        	setUpMapIfNeeded();
			}
			break;
		}
	}
	
	class GetFriendsTask extends AsyncTask<Void, Void, ArrayList<FriendEntity>>{

		@Override
		protected ArrayList<FriendEntity> doInBackground(Void... params) {
			return FriendHandler.getFriend(1);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			super.onPostExecute(result);
			viewFriendsLocation(result);
			
		}
	}
}
