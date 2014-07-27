package com.shuttlemap.android.server.handler;

import java.util.ArrayList;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.server.entity.FriendEntity;
import com.shuttlemap.android.server.entity.ShuttleEntity;
import com.shuttlemap.android.utils.http.HttpClientWrapper;
import com.shuttlemap.android.utils.http.HttpRequestParameters;
import com.shuttlemap.android.utils.http.HttpUtil;

public class FriendHandler {

	public static ArrayList<FriendEntity> searchFriend(String keyword){
		ArrayList<FriendEntity> results = new ArrayList<FriendEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		if(keyword != null)
			params.add("phone",keyword);
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.SearchFriendURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray array = object.getJSONArray("datas");
				
				for(int i=0; i < array.length(); i++){
					JSONObject json = array.getJSONObject(i);
					FriendEntity entity = new FriendEntity();
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
	
	public static ArrayList<FriendEntity> getFriend(int page){
		ArrayList<FriendEntity> results = new ArrayList<FriendEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.GetFriendsURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray array = object.getJSONArray("datas");
				
				for(int i=0; i < array.length(); i++){
					JSONObject json = array.getJSONObject(i);
					FriendEntity entity = new FriendEntity();
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
	
	
	public static boolean requestFriend(String userId){
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("userId",userId);
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.RequestFriendURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean approveFriend(String friendId){
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("friendId",friendId);
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.ApproveFriendURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<FriendEntity> waitingForMeFriends(int page){
		ArrayList<FriendEntity> results = new ArrayList<FriendEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.WaitingForMeURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray array = object.getJSONArray("datas");
				
				for(int i=0; i < array.length(); i++){
					JSONObject json = array.getJSONObject(i);
					FriendEntity entity = new FriendEntity();
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
	
	public static ArrayList<FriendEntity> waitingFriends(int page){
		ArrayList<FriendEntity> results = new ArrayList<FriendEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.WaitingURL, params);
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray array = object.getJSONArray("datas");
				
				for(int i=0; i < array.length(); i++){
					JSONObject json = array.getJSONObject(i);
					FriendEntity entity = new FriendEntity();
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
