package com.shuttlemap.android.server.entity;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ShuttleEntity implements Parcelable {

	private String id;

	private String companyName;
	
	private String companyLogo;
	
	private String shuttleName;
	
	private String companyId;
	
	private String carNo;
	
	private CarType carType;
	
	private double startLatitude;
	
	private double startLongitude;
	
	private String startPointName;
	
	private double endLatitude;
	
	private double endLongitude;
	
	private String endPointName;		
	
	private int startHour;	//24시간 기준으로 출발지에서 출발시간.(운행시작시간)
	
	private int startMinute;
	
	private int endHour;	//24시간 기준으로 운행종료시간 
	
	private int endMinute;
	
	private ScheduleType scheduleType;
	
	private String bookmarkId;
	
	
	private String bookmarkName;
	
	private String driverId;
	
	private String driverName;
	
	private String driverPhone;
	
	public ShuttleEntity(){
		
	}
	
	public void importData(JSONObject jsonObj) throws Exception{
		this.id = jsonObj.getString("id");
		this.companyId = jsonObj.getString("companyId");
		this.companyName = jsonObj.getString("companyName");
		if(jsonObj.has("companyLogo")){
			this.companyLogo = jsonObj.getString("companyLogo");
		}
		this.shuttleName = jsonObj.getString("shuttleName");
		
		if(jsonObj.has("carNo")){
			this.carNo = jsonObj.getString("carNo");
		}
		
		if(jsonObj.has("carType")){
			this.carType = CarType.valueOf(jsonObj.getString("carType"));
		}else{
			this.carType = CarType.UNKNOWN;
		}
		
		if(jsonObj.has("startLatitude")){
			this.startLatitude = jsonObj.getDouble("startLatitude");
		}else{
			this.startLatitude = 0;
		}
		if(jsonObj.has("startLongitude")){
			this.startLongitude = jsonObj.getDouble("startLongitude");
		}else{
			this.startLongitude = 0;
		}
		
		if(jsonObj.has("endLatitude")){
			this.endLatitude = jsonObj.getDouble("endLatitude");
		}else{
			this.endLatitude = 0;
		}
		if(jsonObj.has("endLongitude")){
			this.endLongitude = jsonObj.getDouble("endLongitude");
		}else{
			this.endLongitude = 0;
		}
		
		if(jsonObj.has("startPointName")){
			this.startPointName = jsonObj.getString("startPointName");
		}
		if(jsonObj.has("endPointName")){
			this.endPointName = jsonObj.getString("endPointName");
		}
		
		if(jsonObj.has("startHour")){
			this.startHour = jsonObj.getInt("startHour");
		}else{
			this.startHour = 0;
		}
		if(jsonObj.has("startMinute")){
			this.startMinute = jsonObj.getInt("startMinute");
		}else{
			this.startMinute = 0;
		}
		
		if(jsonObj.has("endHour")){
			this.endHour = jsonObj.getInt("endHour");
		}else{
			this.endHour = 0;
		}
		if(jsonObj.has("endMinute")){
			this.endMinute = jsonObj.getInt("endMinute");
		}else{
			this.endMinute = 0;
		}
		
		this.scheduleType = ScheduleType.valueOf(jsonObj.getString("scheduleType"));
		
		if(jsonObj.has("bookmarkId")){
			this.bookmarkId = jsonObj.getString("bookmarkId");
		}
		
		if(jsonObj.has("bookmarkName")){
			this.bookmarkName = jsonObj.getString("bookmarkName");
		}
		
		this.driverId = jsonObj.getString("driverId");
		this.driverName = jsonObj.getString("driverName");
		if(jsonObj.has("driverPhone")){
			this.driverPhone = jsonObj.getString("driverPhone");
		}
		
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

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
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

	public ScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(String bookmarkId) {
		this.bookmarkId = bookmarkId;
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
