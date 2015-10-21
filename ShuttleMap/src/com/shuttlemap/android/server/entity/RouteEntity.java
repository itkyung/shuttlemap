package com.shuttlemap.android.server.entity;

import java.util.Date;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteEntity implements Parcelable {
	private String id;
	
	private String routeName;
	
	private double latitude;
	
	private double longitude;
	
	private boolean arrived;
	
	private Date arriveDate;
	
	private int idx;
	
	private String arriveDateStr;
	
	
	public RouteEntity(){
		
	}
	
	public void importData(JSONObject json) throws Exception{
		this.id = json.getString("id");
		this.routeName = json.getString("routeName");
		this.latitude = json.getDouble("latitude");
		this.longitude = json.getDouble("longitude");
		this.arrived = json.getBoolean("arrived");
		long date = json.getLong("arriveDate");
		this.idx = json.getInt("idx");
		if(json.has("arriveDateStr")){
			this.arriveDateStr = json.getString("arriveDateStr");
		}
		arriveDate = new Date(date);
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isArrived() {
		return arrived;
	}

	public void setArrived(boolean arrived) {
		this.arrived = arrived;
	}

	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getArriveDateStr() {
		if(arriveDateStr != null && arriveDateStr.endsWith(":0")) {
			return arriveDateStr + "0";
		}
		return arriveDateStr;
	}

	public void setArriveDateStr(String arriveDateStr) {
		this.arriveDateStr = arriveDateStr;
	}


	
	
}
