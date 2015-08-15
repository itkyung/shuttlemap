package com.shuttlemap.web.delegate;

import org.springframework.web.multipart.MultipartFile;

import com.google.gson.annotations.Expose;
import com.shuttlemap.web.entity.CarType;
import com.shuttlemap.web.entity.ShuttleStatus;

public class ShuttleParam {
	
	private String shuttleId;
	
	private String name;
	
	private ShuttleStatus status;
	
	private CarType carType;
	
	private String carNo;
	
	private int startHour;	//24시간 기준으로 출발지에서 출발시간.(운행시작시간)

	private int startMinute;

	private int endHour;	//24시간 기준으로 운행종료시간 

	private int endMinute; 
	
	private MultipartFile routeFile;
	
	private String googleMapUrl;
	
	private String driverId;
	
	private String companyId;

	public String getShuttleId() {
		return shuttleId;
	}

	public void setShuttleId(String shuttleId) {
		this.shuttleId = shuttleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShuttleStatus getStatus() {
		return status;
	}

	public void setStatus(ShuttleStatus status) {
		this.status = status;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	public MultipartFile getRouteFile() {
		return routeFile;
	}

	public void setRouteFile(MultipartFile routeFile) {
		this.routeFile = routeFile;
	}

	public String getGoogleMapUrl() {
		return googleMapUrl;
	}

	public void setGoogleMapUrl(String googleMapUrl) {
		this.googleMapUrl = googleMapUrl;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	
}
