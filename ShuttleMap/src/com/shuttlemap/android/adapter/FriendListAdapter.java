package com.shuttlemap.android.adapter;

import java.util.ArrayList;

import com.shuttlemap.android.R;
import com.shuttlemap.android.adapter.MyShuttleListAdapter.ViewHolder;
import com.shuttlemap.android.common.bitmapDownloader.BitmapDownloader;
import com.shuttlemap.android.server.entity.FriendEntity;
import com.shuttlemap.android.server.entity.ShuttleEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<FriendEntity> friends;
	BitmapFactory.Options bitmapOption;
	boolean sendView;
	FriendListener listener;
	boolean hideBtn;
	
	public FriendListAdapter(Context context,boolean sendView, FriendListener l){
		this.context = context;
		friends = new ArrayList<FriendEntity>();
		bitmapOption = new BitmapFactory.Options();
		bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOption.inSampleSize = 2;
		bitmapOption.inDither = false;
		this.sendView = sendView;
		this.listener = l;
		hideBtn = false;
	}
	
	
	@Override
	public int getCount() {
		return friends.size();
	}

	@Override
	public Object getItem(int position) {
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendEntity friend = (FriendEntity)getItem(position);
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_friends, null);
			ViewHolder viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.updateView(friend);
		
		return convertView;
	}


	public void setFriends(ArrayList<FriendEntity> friends,boolean sendView,boolean hideBtn) {
		this.sendView = sendView;
		this.hideBtn = hideBtn;
		this.friends.clear();
		this.friends.addAll(friends);
	}

	class ViewHolder{
		public TextView nameLabel;
		public TextView phoneLabel;
		public ImageView imageProfile;
		public Button friendButton;
		
		FriendEntity entity;
		
		public ViewHolder(View parent){
			nameLabel = (TextView)parent.findViewById(R.id.nameLabel);
			imageProfile = (ImageView)parent.findViewById(R.id.imageProfile);
			phoneLabel = (TextView)parent.findViewById(R.id.phoneLabel);
			friendButton = (Button)parent.findViewById(R.id.btnFriends);
			
			friendButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(entity.isFriendProgress()){
						listener.approveFriends(entity.getFriendId(), entity.getName());
					}else{
						//친구요청 Dialog를 띄운다.
						listener.requestFriends(entity.getUserId(), entity.getName());
					}
				}
			});
			
		}
		
		public void updateView(FriendEntity entity){
			this.entity = entity;
			
			this.nameLabel.setText(entity.getName());
			String phone = entity.getPhone();
			
			if(phone != null && phone.length() > 4){
				this.phoneLabel.setText("010-XXXX-" + phone.substring(phone.length()-4, phone.length()));
			}
			
			String imageUrl = entity.getProfileImgURL();
			if(imageUrl != null){
				BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption, imageProfile, null);
			}else{
				imageProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.map_btn02_1_n));
			}
			
			if(hideBtn){
				friendButton.setVisibility(View.GONE);
			}else{
			
				if(!entity.isFriendFlag()){
					friendButton.setVisibility(View.VISIBLE);
					if(entity.isFriendProgress()){
						if(sendView){
							friendButton.setVisibility(View.GONE);
						}else{
							friendButton.setText("승인");
						}
					}else{
						friendButton.setText("지인요청");
					}
					
				}else{
					friendButton.setVisibility(View.GONE);
				}
			}
			
		}
	}
	
	public interface FriendListener{
		public void requestFriends(String userId,String name);
		public void approveFriends(String friendId, String name);
	}
}
