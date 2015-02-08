package com.shuttlemap.android.server.handler;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.utils.http.HttpClientWrapper;
import com.shuttlemap.android.utils.http.HttpRequestParameters;
import com.shuttlemap.android.utils.http.HttpUtil;

public class LocationUpdateHandler {
	
	public static void updateLocation(String loginId, double latitude, double longitude){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("loginId",loginId);
		params.add("latitude",String.valueOf(latitude));
		params.add("longitude", String.valueOf(longitude));
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.UpdateLocationURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
}
