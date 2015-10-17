package com.shuttlemap.android;

import java.util.ArrayList;

import com.shuttlemap.android.adapter.FriendListAdapter;
import com.shuttlemap.android.adapter.FriendListAdapter.FriendListener;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.server.entity.FriendEntity;
import com.shuttlemap.android.server.handler.FriendHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFriendsActivity extends ShuttlemapBaseActivity implements FriendListener {
	ListView listView;
	FriendListAdapter adapter;
	Context context;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = this;
		setContentView(R.layout.activity_search_friends);
		
		this.listView = (ListView)findViewById(R.id.listView);
		EditText searchText = (EditText)findViewById(R.id.searchText);
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				
				if (s.toString().length() > 1){
					searchFriends(s.toString());
				}else{
					searchFriends(s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
		});
		this.adapter = new FriendListAdapter(this,true,this);
		this.listView.setAdapter(adapter);
		
		searchFriends("");
	}

	private void searchFriends(String keyword){
		new SearchFriendsTask().execute(keyword);
	}
	
	@Override
	public void requestFriends(final String userId,String name) {
		AlertDialog.Builder altDig = new AlertDialog.Builder(this);
		String msg = name + "님 에게 지인요청을 하시겠습니까?";
		
		altDig.setMessage(msg).setCancelable(false).
				setPositiveButton("네",new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int id) {
					            new RequestFriendsTask().execute(userId);
					        }
						}).
				setNegativeButton("취소", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            // Action for 'NO' Button
			            dialog.cancel();
			        }
				});
		
	    AlertDialog alert = altDig.create();
	    alert.show();
		
	}

	@Override
	public void approveFriends(final String friendId,String name) {
		AlertDialog.Builder altDig = new AlertDialog.Builder(this);
		String msg = name + "님 의 지인요청을 승인하시겠습니까?";
		
		altDig.setMessage(msg).setCancelable(false).
				setPositiveButton("네",new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int id) {
					            new ApproveFriendsTask().execute(friendId);
					        }
						}).
				setNegativeButton("취소", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			            // Action for 'NO' Button
			            dialog.cancel();
			        }
				});
		
	    AlertDialog alert = altDig.create();
	    alert.show();
	}

	
	class RequestFriendsTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return FriendHandler.requestFriend(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Toast.makeText(context, "지인요청이 접수되었습니다.", Toast.LENGTH_SHORT).show();
			searchFriends("");
		}
	}
	
	class ApproveFriendsTask extends AsyncTask<String, Void, Boolean>{
		@Override
		protected Boolean doInBackground(String... params) {
			return FriendHandler.approveFriend(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Toast.makeText(context, "지인으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
			searchFriends("");
		}
	}

	class SearchFriendsTask extends AsyncTask<String, Void, ArrayList<FriendEntity>>{

		@Override
		protected ArrayList<FriendEntity> doInBackground(String... params) {
			return FriendHandler.searchFriend(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			super.onPostExecute(result);
			adapter.setFriends(result,true,false);
			adapter.notifyDataSetChanged();
		}
		
		
	}

	
	
}
