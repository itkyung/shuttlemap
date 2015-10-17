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
import com.shuttlemap.android.ManageFriendsActivity;
import com.shuttlemap.android.R;
import com.shuttlemap.android.SearchFriendsActivity;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ArroundMapFragment extends Fragment implements LocationListener{

	private GoogleMap googleMap;
	private LocationManager locationManager;
    private String provider;
    private Context context;
    boolean locationTag=true;
    private ScrollView scrollView;
    private ViewGroup friendContainer;
    
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
		
		Button btnAddFriend = (Button)view.findViewById(R.id.btnAddFriend);
		btnAddFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),SearchFriendsActivity.class);
				startActivity(intent);
			}
		});
		
		final Button btnFriend = (Button)view.findViewById(R.id.btnFriend);
		btnFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean selected = btnFriend.isSelected();
				if(selected) {
					btnFriend.setSelected(false);
					scrollView.setVisibility(View.GONE);
				} else {
					btnFriend.setSelected(true);
					scrollView.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		view.findViewById(R.id.btnCurrent).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Location location = googleMap.getMyLocation();
				onLocationChanged(location);
			}
		});
		
		this.friendContainer = (ViewGroup)view.findViewById(R.id.friendContainer);
		this.scrollView = (ScrollView)view.findViewById(R.id.friendScroll);
		
		return view;
	}

	@Override
	public void onLocationChanged(Location location) {
		//if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
			moveLocation(ll);
	    //}
	}
	
	private void moveLocation(LatLng ll){
		CameraUpdate center = CameraUpdateFactory.newLatLng(ll);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
		googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);
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
		LatLng startingPoint = new LatLng(37.50525, 127.08886);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,14));
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
		
		addFriendView(friends);
	}
	
	private void addFriendView(ArrayList<FriendEntity> friends){
		for(final FriendEntity friend : friends){
			
			LinearLayout layout = new LinearLayout(getActivity());
			layout.setOrientation(LinearLayout.VERTICAL);
	        // creating LayoutParams  
	        LayoutParams linLayoutParam = new LayoutParams(convertToPx(40), convertToPx(60)); 
	        linLayoutParam.setMargins(0, 0, convertToPx(10), 0);
	        
			ImageView iView = new ImageView(getActivity());
			ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(10,10);
			layoutParams.width = convertToPx(40);
			layoutParams.height = convertToPx(40);
			iView.setLayoutParams(layoutParams);
			
			iView.setBackgroundResource(R.drawable.map_btn02_1_n);
			
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(friendContainer.getLayoutParams());
			
			lParams.width = convertToPx(40);
			lParams.height = convertToPx(40);
			
			layout.addView(iView, lParams);
			
			TextView textView = new TextView(getActivity());
			textView.setText(friend.getName());
			textView.setGravity(Gravity.CENTER);
			ViewGroup.LayoutParams textParams = new LinearLayout.LayoutParams(10,10);
			textParams.width = convertToPx(40);
			textParams.height = convertToPx(20);
			
			layout.addView(textView, textParams);
			
			friendContainer.addView(layout, linLayoutParam);
			
			layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (friend.getLatitude() > 0) {
						LatLng ll = new LatLng(friend.getLatitude(), friend.getLongitude());
						moveLocation(ll);
					}else {
						Toast.makeText(context, "[" + friend.getName() + "]님의 위치정보를 찾을수 없습니다.", Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}
	
	private int convertToPx(int dp){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getActivity().getResources().getDisplayMetrics());
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
