package com.shuttlemap.android.service;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.shuttlemap.android.ShuttlemapApplication;

import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.server.entity.AccountEntity;
import com.shuttlemap.android.server.handler.LocationUpdateHandler;
import com.shuttlemap.android.server.handler.LoginHandler;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 백그라운드로 구동되면서 현재 위치를 서버에 호출해서 알려주는 서비스
 * @author bizwave
 *
 */
public class LocationUpdateService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, 
	LocationListener{

	private final IBinder binder = new LocationBinder();
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private LocationRequest mLocationRequest;
	
	@Override
	public IBinder onBind(Intent intent) {
		//여기에서 location Manager를 시작한다.
		buildGoogleApiClient();
		return binder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		stopLocationUpdates();
		return super.onUnbind(intent);
	}



	public class LocationBinder extends Binder {
		public LocationUpdateService getService() {
			return LocationUpdateService.this;
		}
	}



	@Override
	public void onCreate() {
		super.onCreate();
		checkLogin();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void checkLogin(){
		AccountManager.getInstance().load();
		if(!AccountManager.isLogin()) {
			
		}
	}
	
	private synchronized void buildGoogleApiClient() {
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .addApi(LocationServices.API)
	        .build();
	    
	    mGoogleApiClient.connect();
	}
	
	/**
	 * 위치 정보 업데이트를 시작한다.
	 */
	public void startLocationUpdates() {
		AccountEntity accountEntity = AccountManager.getInstance().getAccountEntity();
		int interval = 180000;	//3분 일반사용자인경우. 드라이버는 1분단위 
		int displacement = 500; //일반사용자는 500m, 드라이버는 100m
		if (accountEntity != null && accountEntity.isDriver()) {
			interval = 60000;
			displacement = 100;
		} 
		
		mLocationRequest = new LocationRequest();
	    mLocationRequest.setInterval(interval);
	    mLocationRequest.setFastestInterval(interval);
	    mLocationRequest.setSmallestDisplacement(displacement);
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
	    
	    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}

	/**
	 * 위치정보를 주기적으로 알려달라는 요청을 한다.
	 */
	protected void createLocationRequest() {
		AccountEntity accountEntity = AccountManager.getInstance().getAccountEntity();
		int interval = 180000;	//3분 일반사용자인경우. 드라이버는 1분단위 
		int displacement = 500; //일반사용자는 500m, 드라이버는 100m
		if (accountEntity != null && accountEntity.isDriver()) {
			interval = 60000;
			displacement = 100;
		} 
	    LocationRequest mLocationRequest = new LocationRequest();
	    mLocationRequest.setInterval(interval);
	    mLocationRequest.setFastestInterval(interval);
	    mLocationRequest.setSmallestDisplacement(displacement);
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY	);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		int result = arg0.getErrorCode();
	}

	/**
	 * GoogleApiClient와 연결되면 호출됨.
	 */
	
	@Override
	public void onConnected(Bundle connectionHint) {
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		onLocationChanged(mLastLocation);
		startLocationUpdates();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		int code = arg0;
		
	}

	/**
	 * 위치가 변경되면 서버에 현재 위치 정보를 알려주게 한다.
	 * 현재 사용자의 설정이 위치업데이트를 하는지에 따라서 다르게 한다.
	 * 운자사이면 무조건 서버에 호출한다.
	 */
	@Override
	public void onLocationChanged(Location location) {
		
		AccountManager accountManager = AccountManager.getInstance();
		AccountEntity accountEntity = accountManager.getAccountEntity();
		
		//if(accountEntity.isDriver() || accountEntity.sendLocation) {
			//1. 운전수인가?
			//2. 아니면 설정이 위치정보를 알아보게 되어있는가?
			//현재 위치 정보를 전송한다.
			new UpdateTask().execute(location);
		//}
		
	}
	
	public void stopLocationUpdates() {
		if (mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}
	}
	
	private void reStartLocationUpdate(){
		if (mGoogleApiClient.isConnected()) {
	        startLocationUpdates();
	    }
	}
	
	class UpdateTask extends AsyncTask<Location, Void, Void> {

		@Override
		protected Void doInBackground(Location... params) {
			Location location = params[0];
			//if(AccountManager.isLogin()){
			AccountManager.createInstance(LocationUpdateService.this).load();
			AccountEntity accountEntity = AccountManager.getInstance().getAccountEntity();
			if(accountEntity != null){
				LocationUpdateHandler.updateLocation(accountEntity.loginId, location.getLatitude(), location.getLongitude());
			}
			return null;
		}
	}
	
	
}
