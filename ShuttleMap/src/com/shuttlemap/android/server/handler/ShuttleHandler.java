package com.shuttlemap.android.server.handler;

import java.util.ArrayList;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.server.entity.ShuttleEntity;
import com.shuttlemap.android.utils.http.HttpClientWrapper;
import com.shuttlemap.android.utils.http.HttpRequestParameters;
import com.shuttlemap.android.utils.http.HttpUtil;

public class ShuttleHandler {
	
	public static ArrayList<ShuttleEntity> searchShuttle(String keyword,int page){
		ArrayList<ShuttleEntity> results = new ArrayList<ShuttleEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		if(keyword != null)
			params.add("keyword",keyword);
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.SearchShuttleURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray array = object.getJSONArray("datas");
				
				for(int i=0; i < array.length(); i++){
					JSONObject json = array.getJSONObject(i);
					ShuttleEntity entity = new ShuttleEntity();
					entity.importData(json);
					results.add(entity);
				}
			}
			
			return results;
		}catch(Exception e){
			e.printStackTrace();
			return results;
		}
	}
	
	
	
}
