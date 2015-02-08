package com.shuttlemap.android.fragment;


import com.shuttlemap.android.LoginActivity;
import com.shuttlemap.android.ManageFriendsActivity;
import com.shuttlemap.android.MyInfoActivity;
import com.shuttlemap.android.R;
import com.shuttlemap.android.SearchFriendsActivity;
import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.server.handler.LoginHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFragment extends Fragment {
	private View needLoginFrame;
	private View settingFrame;
	private Button btnLogout;
	
	public SettingFragment(){
		
	}
	
	public static SettingFragment newInstance(){
		SettingFragment fragment = new SettingFragment();
		
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setting, container,false);
		
		needLoginFrame = rootView.findViewById(R.id.needLoginFrame);
		settingFrame = rootView.findViewById(R.id.settingFrame);
		btnLogout = (Button)rootView.findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logout();
			}
		});
		
		View registFriend = rootView.findViewById(R.id.registFriend);
		registFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),SearchFriendsActivity.class);
				startActivity(intent);
			}
		});
		
		View manageFriend = rootView.findViewById(R.id.manageFriend);
		manageFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),ManageFriendsActivity.class);
				startActivity(intent);
			}
		});
		
		View myProfile = rootView.findViewById(R.id.myProfile);
		myProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),MyInfoActivity.class);
				startActivity(intent);
			}
		});
		
		rootView.findViewById(R.id.btnGoLogin).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),LoginActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initView();
	}
	
	public void initView(){
		if(AccountManager.isLogin()){
			settingFrame.setVisibility(View.VISIBLE);
			needLoginFrame.setVisibility(View.GONE);
			btnLogout.setVisibility(View.VISIBLE);
		}else{
			settingFrame.setVisibility(View.GONE);
			needLoginFrame.setVisibility(View.VISIBLE);
			btnLogout.setVisibility(View.GONE);
		}
	}
	
	
	public void logout(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Notice");
		builder.setMessage("로그아웃하시겠습니까?");
		builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				new AsyncTask<Void,Void,Void>(){

					@Override
					protected Void doInBackground(Void... params){
						LoginHandler.logout();
						return null;
					}

					@Override
					protected void onPostExecute(Void result){
						AccountManager manager = AccountManager.getInstance();
						manager.setAutoLogin(false);
						manager.setLogin(false);
						
						initView();
						
						super.onPostExecute(result);
					}
					
				}.execute();				
			}
		});
		
		builder.setNegativeButton("아니오", null);
		builder.create().show();
	}
	
}
