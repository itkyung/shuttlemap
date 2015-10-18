package com.shuttlemap.android.common;

import java.util.ArrayList;
import java.util.List;

import com.shuttlemap.android.server.entity.AccountEntity;
import com.shuttlemap.android.utils.DeviceUtil;


import android.content.Context;

public class AccountManager{
	public static String osName = "Android";
	public static int osVersion;
	public static boolean isTablet;
	public static String appVersion;
	
	protected static AccountManager manager;
	protected Context context;
	
	private boolean login;
	public AccountEntity entity;
	
	private List<OnLoginChangeListener> listenerList = new ArrayList<OnLoginChangeListener>();

	protected AccountManager(Context context){
		this.context = context;
		entity = new AccountEntity();
		entity.load(context);
		
		isTablet = DeviceUtil.isTablet(context);
		osVersion = DeviceUtil.getOSVersionNumber();
		appVersion = DeviceUtil.getAppVersion(context);
	}
	
	public static AccountManager createInstance(Context context){
		if (manager == null)
			manager = new AccountManager(context);
		
		return manager;
	}
	
	public static AccountManager getInstance()
	{
		return manager;
	}
	
	public static boolean isLogin(){
		if (manager == null)
			return false;
		
		return manager.login;
	}
	
	
	public static boolean isMyUserIndex(String userIndex){
		if (manager == null || manager.login == false || userIndex == null)
			return false;
		
		return userIndex.equals(manager.getAccountEntity().id);
	}
	
	public static String getUserIdx(){
		if (manager == null || manager.login == false || manager.entity == null)
			return null;
		
		return manager.entity.id;
	}
	
	public String getUserIndex(){
		if (login == false || entity == null)
			return null;
		
		return entity.id;
	}
	
	public AccountEntity getAccountEntity(){
		return this.entity;
	}
	
	public void setAccountEntity(AccountEntity entity){
		if(this.entity != null && this.entity.loginId.endsWith(entity.loginId)) {
			this.entity.copyFrom(entity);
		}else{
			this.entity = entity;
		}
		this.entity.store(context);
	}
	
	public void addOnLoginChangeListener(OnLoginChangeListener listener){
		listenerList.remove(listener);
		listenerList.add(listener);
	}
	public void removeOnLoginChangeListener(OnLoginChangeListener listener){
		listenerList.remove(listener);
	}
	
	public void setLogin(boolean value){
		this.login = value;
		
		if (value == false){
			this.entity = null;
		}else{
			this.entity = new AccountEntity();
			this.entity.load(context);
		}
		
		for (OnLoginChangeListener listener : listenerList){
			listener.onLoginChanged(value);
		}
	}
	
	public void setAutoLogin(boolean value)
	{
		entity.keepLogin = value;
		entity.store(context);
	}
	
	public void store()
	{
		entity = new AccountEntity();
		entity.store(context);
	}
	
	public void load()
	{
		entity = new AccountEntity();
		entity.load(context);
	}
	
	public interface OnLoginChangeListener
	{
		void onLoginChanged(boolean login);
	}
	
	public boolean isDriver(){
		AccountEntity account = getAccountEntity();
		if(account == null) return false;
		return account.isDriver();
	}
}
