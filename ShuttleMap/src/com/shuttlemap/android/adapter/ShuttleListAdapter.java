package com.shuttlemap.android.adapter;

import java.util.ArrayList;

import com.shuttlemap.android.server.entity.ShuttleEntity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ShuttleListAdapter extends BaseAdapter {
	private ArrayList<ShuttleEntity> shuttles;
	
	public ShuttleListAdapter(){
		this.shuttles = new ArrayList<ShuttleEntity>();
		
	}
	
	public void setShuttles(ArrayList<ShuttleEntity> sh){
		shuttles.clear();
		shuttles.addAll(sh);
	}
	
	@Override
	public int getCount() {
		return shuttles.size();
	}

	@Override
	public Object getItem(int position) {
		return shuttles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		
		
		
		return null;
	}

}
