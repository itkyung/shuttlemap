package com.shuttlemap.android.server.entity;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteEntity implements Parcelable {
	private String id;
	
	private String routeName;
	
	private double latitude;
	
	private double longitude;
	
	public RouteEntity(){
		
	}
	
	public void importData(JSONObject json) throws Exception{
		this.id = json.getString("id");
		this.routeName = json.getString("routeName");
		this.latitude = json.getDouble("latitude");
		this.longitude = json.getDouble("longitude");
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

}
