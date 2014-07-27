package com.shuttlemap.android;

import java.util.ArrayList;

import com.shuttlemap.android.adapter.FriendListAdapter;
import com.shuttlemap.android.adapter.FriendListAdapter.FriendListener;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.server.entity.FriendEntity;
import com.shuttlemap.android.server.handler.FriendHandler;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class ManageFriendsActivity extends ShuttlemapBaseActivity implements TitleBarListener,FriendListener {
	
	private TitleBar titleBar;
	private Context context;
	private ListView listView;
	private FriendListAdapter adapter;
	BitmapFactory.Options bitmapOption;
	private View ivMark1;
	private View ivMark2;
	private View ivMark3;
	private int currentTab;
	
	private static final int FRIENDS_TAB = 1;
	private static final int RECEIVE_TAB = 2;
	private static final int SEND_TAB = 3;
	
	private int page=1;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = this;
		setContentView(R.layout.activity_manage_friends);
		
		this.titleBar = (TitleBar)getFragmentManager().findFragmentById(R.id.titleBar);
		this.titleBar.setTitle("지인관리");
		
		this.listView = (ListView)findViewById(R.id.friendsList);
		this.adapter = new FriendListAdapter(context, true, this);
		this.listView.setAdapter(adapter);
		
		this.currentTab = FRIENDS_TAB;
		
		ivMark1 = findViewById(R.id.ivMark1);
		ivMark2 = findViewById(R.id.ivMark2);
		ivMark3 = findViewById(R.id.ivMark3);
		
		findViewById(R.id.tvMenu1R).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(FRIENDS_TAB);
			}
		});
		findViewById(R.id.tvMenu2R).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(RECEIVE_TAB);
			}
		});
		findViewById(R.id.tvMenu3R).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(SEND_TAB);
			}
		});
		
		getFriends();
		
	}

	private void changeTab(int tab){
		currentTab = tab;
		
		switch (tab) {
		case FRIENDS_TAB:
			ivMark1.setVisibility(View.VISIBLE);
			ivMark2.setVisibility(View.GONE);
			ivMark3.setVisibility(View.GONE);
			getFriends();
			break;
		case RECEIVE_TAB:
			ivMark1.setVisibility(View.GONE);
			ivMark2.setVisibility(View.VISIBLE);
			ivMark3.setVisibility(View.GONE);
			getReceived();
			break;
		case SEND_TAB:
			ivMark1.setVisibility(View.GONE);
			ivMark2.setVisibility(View.GONE);
			ivMark3.setVisibility(View.VISIBLE);
			getSended();
			break;
		default:
			break;
		}
	}
	
	public void getFriends(){
		page = 1;
		new GetFriendsTask().execute();
	}
	
	public void getReceived(){
		page = 1;
		new ReceivedFriendsTask().execute();
	}
	
	public void getSended(){
		page = 1;
		new SendFriendsTask().execute();
	}
	
	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
	}


	@Override
	public void requestFriends(String userId, String name) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void approveFriends(String friendId, String name) {
		// TODO Auto-generated method stub
		
	}

	class GetFriendsTask extends AsyncTask<Void, Void, ArrayList<FriendEntity>>{

		@Override
		protected ArrayList<FriendEntity> doInBackground(Void... params) {
			return FriendHandler.getFriend(page);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			super.onPostExecute(result);
			adapter.setFriends(result, true, true);
			adapter.notifyDataSetChanged();
		}
	}
	
	class ReceivedFriendsTask extends AsyncTask<Void, Void, ArrayList<FriendEntity>>{

		@Override
		protected ArrayList<FriendEntity> doInBackground(Void... params) {
			return FriendHandler.waitingForMeFriends(page);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			super.onPostExecute(result);
			adapter.setFriends(result, false, false);
			adapter.notifyDataSetChanged();
		}
	}
	
	class SendFriendsTask extends AsyncTask<Void, Void, ArrayList<FriendEntity>>{

		@Override
		protected ArrayList<FriendEntity> doInBackground(Void... params) {
			return FriendHandler.waitingFriends(page);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			super.onPostExecute(result);
			adapter.setFriends(result, true, true);
			adapter.notifyDataSetChanged();
		}
	}
	
}
