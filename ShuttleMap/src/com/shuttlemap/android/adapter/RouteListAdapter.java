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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<RouteEntity> routes;
	private int lastIdx;
	private DateFormat format = new SimpleDateFormat("h:mm a");
	
	public RouteListAdapter(Context context){
		this.context = context;
		this.routes = new ArrayList<RouteEntity>();
		lastIdx = routes.size()-1;
	}
	
	public void setRoutes(ArrayList<RouteEntity> routes){
		this.routes.clear();
		this.routes.addAll(routes);
		lastIdx = routes.size();
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
		ImageButton arriveIcon = (ImageButton)convertView.findViewById(R.id.arriveIcon);
		ImageButton arrivePoint = (ImageButton)convertView.findViewById(R.id.arrivePoint);
		routeLabel.setText(entity.getRouteName());
		if(entity.isArrived()){
			arrivePoint.setVisibility(View.VISIBLE);
		}else{
			arrivePoint.setVisibility(View.GONE);
		}
		if(entity.getIdx() == 1) {
			arriveIcon.setImageResource(R.drawable.road01);
		}else if(entity.getIdx() == lastIdx) {
			arriveIcon.setImageResource(R.drawable.road04);
		}else {
			arriveIcon.setImageResource(R.drawable.road02);
		}
		
		TextView timeLabel = (TextView)convertView.findViewById(R.id.timeLabel);
		timeLabel.setText(entity.getArriveDateStr());
		
		return convertView;
	}

}
