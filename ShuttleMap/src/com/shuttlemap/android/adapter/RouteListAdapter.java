package com.shuttlemap.android.adapter;

import java.util.ArrayList;

import com.shuttlemap.android.server.entity.RouteEntity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RouteListAdapter extends BaseAdapter {
	private Context context;
	
	public RouteListAdapter(Context context){
		this.context = context;
		
	}
	
	public void setRoutes(ArrayList<RouteEntity> routes){
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
