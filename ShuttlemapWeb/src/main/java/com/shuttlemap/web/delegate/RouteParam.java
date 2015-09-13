package com.shuttlemap.web.delegate;

public class RouteParam {
	
	private String shuttleId;
	private String routeName;
	private String routeId;
	private int routeIdx;
	private double longitude;
	private double latitude;
	private int arrivedHour;
	private int arrivedMinute;
	public String getShuttleId() {
		return shuttleId;
	}
	public void setShuttleId(String shuttleId) {
		this.shuttleId = shuttleId;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public int getRouteIdx() {
		return routeIdx;
	}
	public void setRouteIdx(int routeIdx) {
		this.routeIdx = routeIdx;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public int getArrivedHour() {
		return arrivedHour;
	}
	public void setArrivedHour(int arrivedHour) {
		this.arrivedHour = arrivedHour;
	}
	public int getArrivedMinute() {
		return arrivedMinute;
	}
	public void setArrivedMinute(int arrivedMinute) {
		this.arrivedMinute = arrivedMinute;
	}
	
	
}
