package com.shuttlemap.android.server.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONObject;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AccountEntity implements Parcelable{
	public String loginToken;
	public String loginId;
	public String id;
	public String name;
	
	public String facebook;
	public String twitter;
	public String profileImg;
	public String phone;
	
	public boolean keepLogin;
	public int resultCode;
	public boolean success;
	public String userType;
	public boolean sendLocation;
	
	public boolean notiShuttle;
	public boolean notiFriend;
	public boolean notiTake;
	
	public int notiShuttleRange;
	public int notiFriendRange;

	public AccountEntity(){
		
	}
	
	public AccountEntity(Parcel in){
		readFromParcel(in);
	}
	
	public boolean isDriver(){
		return userType == null ? false : userType.equals("DRIVER");
	}
	
	public void importData(JSONObject data) throws Exception{
		this.id = data.getString("id");
		this.name = data.getString("name");
		
		if (data.has("loginToken") && !data.isNull("loginToken"))
			this.loginToken  = data.getString("loginToken");
		
		if (data.has("loginId") && !data.isNull("loginId"))
			this.loginId   = data.getString("loginId");
		else
			this.loginId = null;

		if (data.has("facebook") && !data.isNull("facebook"))
			this.facebook  = data.getString("facebook");
		else
			this.facebook = null;

		if (data.has("twitter") && !data.isNull("twitter"))
			this.twitter   = data.getString("twitter");
		else
			this.twitter = null;

		if (data.has("phone")){
			this.phone = data.getString("phone");
		}
		
		if (data.has("profileImg")){
			this.profileImg = data.getString("profileImg");
		}
		
		if (data.has("userType")){
			this.userType = data.getString("userType");
		}
		
	}
	
	public void load(Context context){
		SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
		
		try{
			this.id = preferences.getString("id", null);
			this.name = preferences.getString("name", null);
			this.loginToken = preferences.getString("loginToken", null);
			this.loginId  = preferences.getString("loginId", null);
			this.facebook  = preferences.getString("facebook", null);
			this.twitter   = preferences.getString("twitter", null);
			this.profileImg = preferences.getString("profileImg", null);
			this.phone = preferences.getString("phone", null);
			this.keepLogin = preferences.getBoolean("keepLogin", false);
			this.userType = preferences.getString("userType", null);
			this.sendLocation = preferences.getBoolean("sendLocation", false);
			this.notiShuttle = preferences.getBoolean("notiShuttle", false);
			this.notiFriend = preferences.getBoolean("notiFriend", false);
			this.notiTake = preferences.getBoolean("notiTake", false);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void store(Context context){
		SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putString("id",this.id);
		editor.putString("name", this.name);
		editor.putString("loginToken", this.loginToken);
		editor.putString("loginId", this.loginId);
		editor.putString("facebook", this.facebook);
		editor.putString("twitter", this.twitter);
		editor.putString("phone", this.phone);
		editor.putString("profileImg", this.profileImg);
		editor.putBoolean("keepLogin", this.keepLogin);
		editor.putString("userType", this.userType);
		editor.putBoolean("sendLocation", this.sendLocation);
		editor.putBoolean("notiShuttle", this.notiShuttle);
		editor.putBoolean("notiFriend", this.notiFriend);
		editor.putBoolean("notiTake", this.notiTake);
		editor.commit();
		
	}
	
	public void loadExtra(Context context){
		SharedPreferences preferences = context.getSharedPreferences("AccountExtra", Context.MODE_PRIVATE);
		
		this.notiShuttle = preferences.getBoolean("notiShuttle", false);
		this.notiFriend = preferences.getBoolean("notiFriend", false);
		this.notiTake = preferences.getBoolean("notiTake", false);
		
		this.notiShuttleRange = preferences.getInt("notiShuttleRange", 0);
		this.notiFriendRange = preferences.getInt("notiFriendRange", 0);
	}
	
	public void storeExtra(Context context){
		SharedPreferences preferences = context.getSharedPreferences("AccountExtra", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("notiShuttle", this.notiShuttle);
		editor.putBoolean("notiFriend", this.notiFriend);
		editor.putBoolean("notiTake", this.notiTake);
		editor.putInt("notiFriendRange", this.notiFriendRange);
		editor.putInt("notiShuttleRange", this.notiShuttleRange);
		editor.commit();
	}
	
	public void storePhotoURL(Context context){
		SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString("photo", this.profileImg);

		editor.commit();
	}

	public String getProfileImageURL(){
		if (profileImg != null && profileImg.length() > 0)
			return profileImg;
		else
			return null;
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		out.writeString(id);
		out.writeString(name);
		out.writeString(loginToken);
		out.writeString(loginId);
		
		out.writeString(twitter);
		out.writeString(facebook);
		out.writeString(phone);
		out.writeString(profileImg);
		out.writeString(userType);
		out.writeInt(sendLocation ? 1 : 0);
		out.writeInt(notiShuttle ? 1: 0);
		out.writeInt(notiFriend ? 1 : 0);
		out.writeInt(notiTake ? 1 : 0);
		out.writeInt(notiShuttleRange);
		out.writeInt(notiFriendRange);
	}
	
	protected void readFromParcel(Parcel in){
		id = in.readString();
		name = in.readString();
		loginToken   = in.readString();
		loginId    = in.readString();
	
		twitter    = in.readString();
		facebook   = in.readString();
		phone = in.readString();
		profileImg = in.readString();
		userType = in.readString();
		sendLocation = in.readInt() == 1 ? true : false;
		notiShuttle = in.readInt() == 1 ? true : false;
		notiFriend = in.readInt() == 1 ? true : false;
		notiTake = in.readInt() == 1 ? true : false;
		notiShuttleRange = in.readInt();
		notiFriendRange = in.readInt();
	}

	public void copyFrom(AccountEntity entity) {
		this.name = entity.name;
		this.loginToken = entity.loginToken;
		this.twitter = entity.twitter;
		this.facebook = entity.facebook;
		this.phone = entity.phone;
		this.userType = entity.userType;
		this.sendLocation = entity.sendLocation;
		
		
	}
	
	public static final Parcelable.Creator<AccountEntity> CREATOR = new Parcelable.Creator<AccountEntity>()
	{
		public AccountEntity createFromParcel(Parcel in)
		{
			return new AccountEntity(in);
		}
		
		public AccountEntity[] newArray(int size)
		{
			return new AccountEntity[size];
		}
	};


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
