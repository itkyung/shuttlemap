package com.shuttlemap.android.server.handler;

import java.util.HashMap;


import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.server.entity.AccountEntity;
import com.shuttlemap.android.utils.http.HttpClientWrapper;
import com.shuttlemap.android.utils.http.HttpRequestParameters;
import com.shuttlemap.android.utils.http.HttpUtil;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class LoginHandler{
	public static int CodeSuccess = 0;
	public static int CodeDuplicateAccount = 8;
	public static int CodeNoAccount = 9;
	public static int CodeEmpty = 10;
	public static int CodeAlreadyExist = 11;
	public static int CodeUnknown = -1;
	
	public static AccountEntity login(Context context,String userId, String passwd, boolean keepLogin){
		String deviceType = "mobile";
		String osType = "Android";
		String osVersion = "" + AccountManager.osVersion;
		String appVersion = "" + AccountManager.appVersion;
		
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("deviceType",deviceType);
		params.add("osVersion",osVersion);
		params.add("osType",osType);
		params.add("appVersion", appVersion);
		params.add("loginId",userId);
		params.add("pwd",passwd);
		
		AccountEntity entity = new AccountEntity();
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.LoginURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				
				syncCookie(context,httpClient);
				
				
				JSONObject data = (JSONObject)object.get("data");
				entity.importData(data);
				entity.resultCode = errorNo;
				entity.keepLogin = keepLogin;
				entity.success = true;
				entity.sendLocation = true;
			}else{
				entity = new AccountEntity();
				entity.loginId = userId;
				entity.resultCode = errorNo;
				entity.success = false;
			}
		}catch(Exception e){
			e.printStackTrace();
			entity.resultCode = -1;
			entity.success = false;
		}

		return entity;
	}

	
	public static AccountEntity login(Context context,String loginId,String loginToken){
		AccountEntity entity = new AccountEntity();
		
		String deviceType = "mobile";
		String osType = "Android";
		String osVersion = "" + AccountManager.osVersion;
		String appVersion = "" + AccountManager.appVersion;
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("deviceType",deviceType);
		params.add("osVersion",osVersion);
		params.add("osType",osType);
		params.add("appVersion", appVersion);
		params.add("loginToken", loginToken);
		params.add("loginId", loginId);
	
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient,ServerStaticVariable.LoginByTokenURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");

			if (success){
				syncCookie(context,httpClient);
				JSONObject data = (JSONObject)object.get("data");
				entity.importData(data);
				entity.keepLogin = true;
				entity.resultCode = 0;
				entity.success = true;
				entity.sendLocation = true;
			}else{
				entity.success = false;
				entity.resultCode = errorNo;
			}
		}
		catch(Exception e)
		{
			entity.resultCode = -1;
			entity.success = false;
			e.printStackTrace();
		}

		return entity;
	}
//	
	public static void syncCookie(Context context,DefaultHttpClient client){
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		
		if (!client.getCookieStore().getCookies().isEmpty()) {
			cookieManager.removeSessionCookie();
			
			try{
				// ICS 
				Thread.sleep(500);
			}catch(Exception e){
				
			}
		}
			
		for (Cookie cookie : client.getCookieStore().getCookies()){
			CookieManager.getInstance().setCookie(cookie.getDomain(), cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain());
		}
		CookieSyncManager.getInstance().sync();
	}
	
	public static AccountEntity loginWithSocialAccount(String socialType, String socialId, String loginId){
		AccountEntity entity = new AccountEntity();;
		HttpRequestParameters params = new HttpRequestParameters();
		
		String deviceType = "mobile";
		String osType = "Android";
		String osVersion = "" + AccountManager.osVersion;
		String appVersion = "" + AccountManager.appVersion;
	
		params.add("deviceType",deviceType);
		params.add("osVersion",osVersion);
		params.add("osType",osType);
		params.add("appVersion", appVersion);
		params.add("socialType", socialType);
		params.add("socialId", socialId);
		params.add("loginId", loginId);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.LoginBySocialURL, params);

		try
		{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");

			if (success){
				JSONObject data = (JSONObject)object.get("data");
				entity.importData(data);
				entity.keepLogin = true;
				entity.resultCode = errorNo;
				entity.success = true;
				entity.sendLocation = true;
			}else{
			
				entity.resultCode = errorNo;
				entity.success = false;
			}
		}catch(Exception e){
			entity.success = false;
			entity.resultCode = -1;
			e.printStackTrace();
		}

		return entity;
	}
	
	public static boolean logout(){
		boolean result = false;
		HttpRequestParameters params = new HttpRequestParameters();
		
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.LoginOutURL, params);

		try
		{
//			JSONObject object = new JSONObject(resultString);
//			result = object.getInt("errno") == 0;
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;		
	}
	
	public static boolean resendActivateLink(String userId){
		int result = -1;
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("userId",userId);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ResendActivateURL, params);

		boolean success = false;
		try
		{
			JSONObject object = new JSONObject(resultString);
			success = object.getBoolean("success");
			result = object.getInt("errno");
		}catch(Exception e){
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}
	
	public static boolean registAccount(String loginId, String passwd, 
			String nickName, String phone){
		

		String osType = "Android";
		String appVersion = "" + AccountManager.appVersion;
		
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("loginId", loginId);
		params.add("password",passwd);
		params.add("name",nickName);
		params.add("phone",phone);
		params.add("osType",osType);
		params.add("appVersion", appVersion);
	
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.RegistAccountURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int result = object.getInt("errno");
			
			if (success){
				return true;
			}
			return false;
		}
		catch(Exception e){
		
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean changePassword(String currentPasswd,String newPasswd){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("currentPasswd",currentPasswd);
		params.add("newPasswd",newPasswd);
		
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ChangePassworkdURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			if(object.getBoolean("success")){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	

}
