package com.shuttlemap.android.server.entity;

import java.util.Date;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendEntity implements Parcelable {

	private String userId;
	
	private String friendId;
	
	private String name;
	
	private String profileImg;
	
	private boolean friendFlag;
	
	private boolean friendProgress;
	
	private String phone;
	
	private double latitude;
	
	private double longitude;
	
	private Date lastLocationDate;
	
	
	public FriendEntity(){
		
	}
	
	public FriendEntity(Parcel in){
		readFromParcel(in);
	}
	
	public String getProfileImgURL(){
		if(profileImg == null){
			return null;
		}
		return profileImg;
	}
	
	public void importData(JSONObject json) throws Exception{
		this.userId = json.getString("id");
		if(json.has("friendId")){
			this.friendId = json.getString("friendId");
		}
		
		this.name = json.getString("name");
		this.phone = json.getString("phone");
		if(json.has("profileImg")){
			this.profileImg = json.getString("profileImg");
		}
		
		this.friendFlag = json.getBoolean("friendFlag");
		this.friendProgress = json.getBoolean("friendProgress");
		
		if(json.has("latitude")){
			this.latitude = json.getDouble("latitude");
		}
		
		if(json.has("longitude")){
			this.longitude = json.getDouble("longitude");
		}
		
		if(json.has("lastLocationDate")){
			this.lastLocationDate = new Date(json.getLong("lastLocationDate"));
		}
	}
	
	
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void readFromParcel(Parcel in){
		this.userId = in.readString();
		this.friendId = in.readString();
		this.name = in.readString();
		this.phone = in.readString();
		this.profileImg = in.readString();
		this.friendFlag = in.readInt() == 1 ? true : false;
		this.friendProgress = in.readInt() == 1 ? true : false;
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.lastLocationDate = new Date(in.readLong());
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.userId);
		dest.writeString(this.friendId);
		dest.writeString(this.name);
		dest.writeString(this.phone);
		dest.writeString(this.profileImg);
		dest.writeInt(this.friendFlag ? 1 : 0);
		dest.writeInt(this.friendProgress ? 1 : 0);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeLong(this.lastLocationDate == null ? 0 : this.lastLocationDate.getTime());
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Date getLastLocationDate() {
		return lastLocationDate;
	}

	public void setLastLocationDate(Date lastLocationDate) {
		this.lastLocationDate = lastLocationDate;
	}

	
}
