package com.shuttlemap.web.delegate;

import com.google.gson.annotations.Expose;
import com.shuttlemap.web.entity.Shuttle;

public class ShuttleDelegate {
	@Expose
	private String id;
	@Expose
	private String companyName;
	@Expose
	private String companyLogo;
	@Expose
	private String shuttleName;
	@Expose
	private String shuttleLongName;	//시간기준을 포함한 표현.
	@Expose
	private String companyId;
	@Expose
	private String carNo;
	@Expose
	private String carType;
	@Expose
	private double startLatitude;
	@Expose
	private double startLongitude;
	@Expose
	private String startPointName;
	@Expose
	private double endLatitude;
	@Expose
	private double endLongitude;
	@Expose
	private String endPointName;		
	@Expose
	private int startHour;	//24시간 기준으로 출발지에서 출발시간.(운행시작시간)
	@Expose
	private int startMinute;
	@Expose
	private int endHour;	//24시간 기준으로 운행종료시간 
	@Expose
	private int endMinute;
	@Expose
	private String scheduleType;
	@Expose
	private String bookmarkId;
	
	@Expose
	private String bookmarkName;
	@Expose
	private String driverId;
	@Expose
	private String driverName;
	@Expose
	private String driverPhone;
	@Expose
	private String routeFilePath;
	
	public ShuttleDelegate(Shuttle shuttle){
		this.id = shuttle.getId();
		this.companyId = shuttle.getCompany().getId();
		this.companyLogo = shuttle.getCompany().getCompanyLogo();
		this.companyName = shuttle.getCompany().getName();
		
		this.shuttleName = shuttle.getName();
		this.carNo = shuttle.getCarNo();
		this.carType = shuttle.getCarType().name();
		if(shuttle.getStartLatitude() != null)
			this.startLatitude = shuttle.getStartLatitude().doubleValue();
		
		if(shuttle.getStartLongitude() != null)
			this.startLongitude = shuttle.getStartLongitude().doubleValue();
		
		if(shuttle.getEndLatitude() != null)
			this.endLatitude = shuttle.getEndLatitude().doubleValue();
		
		if(shuttle.getEndLongitude() != null)
			this.endLongitude = shuttle.getEndLongitude().doubleValue();
		this.startPointName = shuttle.getStartPointName();
		this.endPointName = shuttle.getEndPointName();
		this.startHour = shuttle.getStartHour();
		this.startMinute = shuttle.getStartMinute();
		this.endHour = shuttle.getEndHour();
		this.endMinute = shuttle.getEndMinute();
		
		this.scheduleType = shuttle.getScheduleType().name();
		if(shuttle.getDriver() != null){
			this.driverId = shuttle.getDriver().getId();
			this.driverName = shuttle.getDriver().getName();
			this.driverPhone = shuttle.getDriver().getPhone();
		}
		this.routeFilePath = shuttle.getRouteFilePath();
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getShuttleName() {
		return shuttleName;
	}

	public void setShuttleName(String shuttleName) {
		this.shuttleName = shuttleName;
	}

	public String getShuttleLongName() {
		return shuttleLongName;
	}

	public void setShuttleLongName(String shuttleLongName) {
		this.shuttleLongName = shuttleLongName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public String getStartPointName() {
		return startPointName;
	}

	public void setStartPointName(String startPointName) {
		this.startPointName = startPointName;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public String getEndPointName() {
		return endPointName;
	}

	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
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

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}


	public String getBookmarkName() {
		return bookmarkName;
	}


	public void setBookmarkName(String bookmarkName) {
		this.bookmarkName = bookmarkName;
	}


	public String getDriverId() {
		return driverId;
	}


	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}


	public String getDriverName() {
		return driverName;
	}


	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}


	public String getDriverPhone() {
		return driverPhone;
	}


	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}


	public String getBookmarkId() {
		return bookmarkId;
	}


	public void setBookmarkId(String bookmarkId) {
		this.bookmarkId = bookmarkId;
	}


	public String getRouteFilePath() {
		return routeFilePath;
	}


	public void setRouteFilePath(String routeFilePath) {
		this.routeFilePath = routeFilePath;
	}



	
	
}
