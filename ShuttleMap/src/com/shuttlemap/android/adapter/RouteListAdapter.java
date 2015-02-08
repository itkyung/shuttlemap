package com.shuttlemap.android.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.shuttlemap.android.R;
import com.shuttlemap.android.adapter.ShuttleListAdapter.ViewHolder;
import com.shuttlemap.android.server.entity.RouteEntity;
import com.shuttlemap.android.server.entity.ShuttleEntity;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RouteListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<RouteEntity> routes;
	
	private DateFormat format = new SimpleDateFormat("h:mm a");
	
	public RouteListAdapter(Context context){
		this.context = context;
		this.routes = new ArrayList<RouteEntity>();
	}
	
	public void setRoutes(ArrayList<RouteEntity> routes){
		this.routes.clear();
		this.routes.addAll(routes);
	}
	
	@Override
	public int getCount() {
		return this.routes.size();
	}

	@Override
	public Object getItem(int position) {
		return this.routes.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RouteEntity entity = (RouteEntity)getItem(position);
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_route, parent, false);
		}
		
		TextView routeLabel = (TextView)convertView.findViewById(R.id.routeLabel);
		TextView arriveLabel = (TextView)convertView.findViewById(R.id.arriveLabel);
		routeLabel.setText(entity.getRouteName());
		if(entity.isArrived()){
			arriveLabel.setVisibility(View.VISIBLE);
			arriveLabel.setText(format.format(entity.getArriveDate()) + "- 도착");
		}else{
			arriveLabel.setVisibility(View.GONE);
		}
		
		return convertView;
	}

}
