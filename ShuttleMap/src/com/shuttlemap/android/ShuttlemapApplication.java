package com.shuttlemap.android;

import com.shuttlemap.android.service.LocationUpdateService;
import com.shuttlemap.android.service.LocationUpdateService.LocationBinder;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ShuttlemapApplication extends Application {
	
	private LocationUpdateService locationUpdateService;
	
	private Intent serviceIntent;
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocationBinder binder = (LocationBinder)service;
			locationUpdateService = binder.getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
	};
	
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (LocationUpdateService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void startLocationUpdate(){
		if(serviceIntent == null) {
			serviceIntent = new Intent(getApplicationContext(), LocationUpdateService.class);
			
			if(!isServiceRunning()) {
				startService(serviceIntent);
			}
		}
		bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	public void stopLocationUpdate(){
		locationUpdateService.stopLocationUpdates();
		unbindService(serviceConnection);
	}

	@Override
	public void onTerminate() {
		if(isServiceRunning()){
			unbindService(serviceConnection);
			stopService(serviceIntent);
		}
		super.onTerminate();
	}
	
	
}
