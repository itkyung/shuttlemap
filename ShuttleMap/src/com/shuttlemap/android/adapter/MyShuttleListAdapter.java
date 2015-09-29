package com.shuttlemap.android.adapter;

import java.util.ArrayList;

import com.shuttlemap.android.R;

import com.shuttlemap.android.common.bitmapDownloader.BitmapDownloader;
import com.shuttlemap.android.server.entity.ShuttleEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyShuttleListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ShuttleEntity> shuttles;
	private BookmarkListener listener;
	BitmapFactory.Options bitmapOption;
	
	public MyShuttleListAdapter(Context context,BookmarkListener l){
		this.context = context;
		this.listener = l;
		bitmapOption = new BitmapFactory.Options();
		bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOption.inSampleSize = 2;
		bitmapOption.inDither = false;
		shuttles = new ArrayList<ShuttleEntity>();
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
		
		ShuttleEntity entity = (ShuttleEntity)getItem(position);
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_my_shuttle, null);
			ViewHolder viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.updateView(entity);
		
		return convertView;
	}


	class ViewHolder{
		public TextView nameLabel;
		public TextView addressLabel;
		public ImageView imageProfile;

		public TextView bookmarkLabel;
		ShuttleEntity entity;
		
		public ViewHolder(View parent){
			nameLabel = (TextView)parent.findViewById(R.id.nameLabel);
			addressLabel = (TextView)parent.findViewById(R.id.addressLabel);
			imageProfile = (ImageView)parent.findViewById(R.id.imageProfile);
		
			bookmarkLabel = (TextView)parent.findViewById(R.id.bookmarkLabel);
			
			imageProfile.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.viewShuttle(entity);
				}
			});
			
			nameLabel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.viewShuttle(entity);
				}
			});
			parent.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.viewShuttle(entity);
				}
			});
			
			
		}
		
		public void updateView(ShuttleEntity entity){
			this.entity = entity;
			
			this.nameLabel.setText(entity.getShuttleName());
		
			this.addressLabel.setText(entity.getAddress());
			this.bookmarkLabel.setText(entity.getBookmarkName());
			
			String imageUrl = entity.getCompanyLogoURL();
			if(imageUrl != null){
				BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption, imageProfile, null);
			}else{
				imageProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.mk01_color_a));
			}
			
		}
	}
	public interface BookmarkListener{
		public void viewShuttle(ShuttleEntity shuttle);
		
	}
	
}
