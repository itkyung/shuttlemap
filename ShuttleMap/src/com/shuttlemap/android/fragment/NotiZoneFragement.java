package com.shuttlemap.android.fragment;

import com.shuttlemap.android.R;
import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.server.entity.AccountEntity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotiZoneFragement extends Fragment {

	private View btnShuttleNoti;
	private View btnFriendNoti;
	private View btnTakeNoti;
	
	public NotiZoneFragement(){
		
	}
	
	public static NotiZoneFragement newInstance(){
		NotiZoneFragement frament = new NotiZoneFragement();
		
		return frament;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_noti_zone, container,false);
		
		this.btnShuttleNoti = rootView.findViewById(R.id.btnShuttleNoti);
		this.btnFriendNoti = rootView.findViewById(R.id.btnFriendNoti);
		this.btnTakeNoti = rootView.findViewById(R.id.btnTakeNoti);
		
		final AccountEntity accountEntity = AccountManager.getInstance().getAccountEntity();
		accountEntity.loadExtra(getActivity());
		this.btnShuttleNoti.setSelected(accountEntity.notiShuttle);
		this.btnShuttleNoti.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean oldValue = btnShuttleNoti.isSelected();
				btnShuttleNoti.setSelected(!oldValue);
				accountEntity.notiShuttle = !oldValue;
				accountEntity.store(getActivity());
				accountEntity.storeExtra(getActivity());
			}
		});
		
		this.btnFriendNoti.setSelected(accountEntity.notiFriend);
		this.btnFriendNoti.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean oldValue = btnFriendNoti.isSelected();
				btnFriendNoti.setSelected(!oldValue);
				accountEntity.notiFriend = !oldValue;
				accountEntity.store(getActivity());
				accountEntity.storeExtra(getActivity());
			}
		});
		
		this.btnTakeNoti.setSelected(accountEntity.notiTake);
		this.btnTakeNoti.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean oldValue = btnTakeNoti.isSelected();
				btnTakeNoti.setSelected(!oldValue);
				accountEntity.notiTake = !oldValue;
				accountEntity.store(getActivity());
				accountEntity.storeExtra(getActivity());
			}
		});
		
		return rootView;
	}
}
