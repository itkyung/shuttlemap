package com.shuttlemap.web.delegate;

import com.google.gson.annotations.Expose;
import com.shuttlemap.web.entity.ShuttleRoute;

public class RouteDelegate {
	@Expose
	private String id;
	
	@Expose
	private String routeName;
	
	@Expose
	private double latitude;
	 
	@Expose
	private double longitude;
	
	@Expose
	private boolean arrived;
	
	@Expose
	private long arriveDate;
	
	@Expose
	private int idx;
	
	@Expose
	private String arriveDateStr;
	
	public RouteDelegate(ShuttleRoute route){
		this.id = route.getId();
		this.routeName = route.getRouteName();
		if(route.getLatitude() != null)
			this.latitude = route.getLatitude().doubleValue();
		if(route.getLongitude() != null)
			this.longitude = route.getLongitude().doubleValue();
		
		this.arrived = false;
		arriveDate = 0;
		this.arriveDateStr = route.getArrivedHour() + ":" + route.getArrivedMinute();
		this.idx = route.getIdx();
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

	public long getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(long arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getArriveDateStr() {
		return arriveDateStr;
	}

	public void setArriveDateStr(String arriveDateStr) {
		this.arriveDateStr = arriveDateStr;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	
}
