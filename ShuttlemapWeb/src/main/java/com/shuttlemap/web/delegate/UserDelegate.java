package com.shuttlemap.web.delegate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.shuttlemap.web.entity.Friends;
import com.shuttlemap.web.entity.User;

public class UserDelegate {
	@Expose
	private String id;
	@Expose
	private String friendId;
	
	@Expose
	private String loginId;
	
	private String password;
	@Expose
	private String name;
	@Expose
	private String appVersion;
	@Expose
	private String osType;
	@Expose
	private String phone;
	@Expose
	private String loginToken;
	@Expose
	private String profileImg;
	@Expose
	private double latitude;
	@Expose
	private double longitude;
	@Expose
	private long lastLocationDate;
	
	@Expose
	private boolean friendFlag;
	@Expose
	private boolean friendProgress;
	@Expose
	private String lastLoginDate;
	@Expose
	private String created;
	@Expose
	private String userType;
	
	private static final DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH시");
	
	public UserDelegate(){
		
	}
	
	
	public UserDelegate(User user){
		super();
		setUser(user);
		this.latitude = 0;
		this.longitude = 0;
		friendFlag = false;
		friendProgress = false;
	}
	
	
	

	public void setUser(User user){
		this.id = user.getId();
		this.loginId = user.getLoginId();
		this.name = user.getName();
		this.appVersion = user.getAppVersion();
		if(user.getOsType() == null){
			
		}else{
			this.osType = user.getOsType().name();
		}
		if(user.getPhone() == null){
			this.phone = "전화번호없음";
		}else{
			this.phone = user.getPhone();
		}
		this.loginToken = user.getLoginToken();
		this.profileImg = user.getProfileImg();
		if(user.getLastLoginDate() == null){
			this.lastLoginDate = "로그인없음";
		}else{
			this.lastLoginDate = fm.format(user.getLastLoginDate());
		}
		this.userType = user.getUserType().name();
		this.created = fm.format(user.getCreated());
	}
	
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getLoginId() {
		return loginId;
	}



	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getAppVersion() {
		return appVersion;
	}



	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}



	public String getOsType() {
		return osType;
	}



	public void setOsType(String osType) {
		this.osType = osType;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getLoginToken() {
		return loginToken;
	}



	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}



	public String getProfileImg() {
		return profileImg;
	}



	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}


	public String getFriendId() {
		return friendId;
	}


	public void setFriendId(String friendId) {
		this.friendId = friendId;
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


	public boolean isFriendFlag() {
		return friendFlag;
	}


	public void setFriendFlag(boolean friendFlag) {
		this.friendFlag = friendFlag;
	}


	public boolean isFriendProgress() {
		return friendProgress;
	}


	public void setFriendProgress(boolean friendProgress) {
		this.friendProgress = friendProgress;
	}


	public String getLastLoginDate() {
		return lastLoginDate;
	}


	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}


	public String getCreated() {
		return created;
	}


	public void setCreated(String created) {
		this.created = created;
	}


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	public long getLastLocationDate() {
		return lastLocationDate;
	}


	public void setLastLocationDate(long lastLocationDate) {
		this.lastLocationDate = lastLocationDate;
	}


	
	
}
