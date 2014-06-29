package com.shuttlemap.android.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.GCMRegistrar;
import com.shuttlemap.android.server.handler.GCMServiceHandler;
import com.shuttlemap.android.utils.file.FileUtil;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;



public class GCMManager{
	public static final String SENDER_ID = "184879146181";
	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;

	public static String getRegistrationId(Context context){
		return GCMRegistrar.getRegistrationId(context);
	}
	
	/**
	 * GCM Server에 디바이스를 등록한다.
	 * @param context
	 */
	public static void registerGCM(final Context context){
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		
		String registrationId = GCMRegistrar.getRegistrationId(context);
		
		if (registrationId.equals(""))
			GCMRegistrar.register(context, GCMManager.SENDER_ID);
	}

	/**
	 * 3rd-party 메시지 서버에 registration id를 등록한다. 
	 * @param context
	 * @param registrationId
	 */
	public static void register(Context context, String registrationId){
		if (GCMServiceHandler.updateGCMRegistrationId(AccountManager.getUserIdx(), registrationId))
			GCMRegistrar.setRegisteredOnServer(context, true);
	}
	
	/**
	 * 3rd-party 메시지 서버에 registration id를 삭제한다.
	 * @param context
	 */
	public static void unregister(Context context){
		if (AccountManager.isLogin()){
			if (GCMServiceHandler.updateGCMRegistrationId(AccountManager.getUserIdx(), null))
				GCMRegistrar.setRegisteredOnServer(context, false);
		}
	}
	
}
