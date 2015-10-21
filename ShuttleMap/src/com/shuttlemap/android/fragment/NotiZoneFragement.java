package com.shuttlemap.android.fragment;

import com.shuttlemap.android.R;
import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.server.entity.AccountEntity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class NotiZoneFragement extends Fragment implements View.OnClickListener {

	private View btnShuttleNoti;
	private View btnFriendNoti;
	private View btnTakeNoti;
	
	private ImageButton btnShuttle1;
	private ImageButton btnShuttle2;
	private ImageButton btnShuttle3;
	private ImageButton btnShuttle4;
	private ImageButton btnShuttle5;
	
	private ImageButton btnFriend1;
	private ImageButton btnFriend2;
	private ImageButton btnFriend3;
	private ImageButton btnFriend4;
	private ImageButton btnFriend5;
	
	
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
				if(!btnShuttleNoti.isSelected()) {
					clearShuttle();
				}
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
				if(!btnFriendNoti.isSelected()) {
					clearFriend();
				}
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
		
		this.btnShuttle1 = (ImageButton)rootView.findViewById(R.id.btn_shuttle_1);
		this.btnShuttle2 = (ImageButton)rootView.findViewById(R.id.btn_shuttle_2);
		this.btnShuttle3 = (ImageButton)rootView.findViewById(R.id.btn_shuttle_3);
		this.btnShuttle4 = (ImageButton)rootView.findViewById(R.id.btn_shuttle_4);
		this.btnShuttle5 = (ImageButton)rootView.findViewById(R.id.btn_shuttle_5);
		
		this.btnShuttle1.setOnClickListener(this);
		this.btnShuttle2.setOnClickListener(this);
		this.btnShuttle3.setOnClickListener(this);
		this.btnShuttle4.setOnClickListener(this);
		this.btnShuttle5.setOnClickListener(this);
		
		switch (accountEntity.notiShuttleRange) {
		case 500:
			this.btnShuttle1.setSelected(true);
			break;
		case 1000:
			this.btnShuttle2.setSelected(true);
			break;
		case 3000:
			this.btnShuttle3.setSelected(true);
			break;
		case 5000:
			this.btnShuttle4.setSelected(true);
			break;
		case 10000:
			this.btnShuttle5.setSelected(true);
			break;
		default:
			break;
		}
		
		this.btnFriend1 = (ImageButton)rootView.findViewById(R.id.btn_friend_1);
		this.btnFriend2 = (ImageButton)rootView.findViewById(R.id.btn_friend_2);
		this.btnFriend3 = (ImageButton)rootView.findViewById(R.id.btn_friend_3);
		this.btnFriend4 = (ImageButton)rootView.findViewById(R.id.btn_friend_4);
		this.btnFriend5 = (ImageButton)rootView.findViewById(R.id.btn_friend_5);
		
		this.btnFriend1.setOnClickListener(this);
		this.btnFriend2.setOnClickListener(this);
		this.btnFriend3.setOnClickListener(this);
		this.btnFriend4.setOnClickListener(this);
		this.btnFriend5.setOnClickListener(this);
		
		switch (accountEntity.notiFriendRange) {
		case 500:
			this.btnFriend1.setSelected(true);
			break;
		case 1000:
			this.btnFriend2.setSelected(true);
			break;
		case 3000:
			this.btnFriend3.setSelected(true);
			break;
		case 5000:
			this.btnFriend4.setSelected(true);
			break;
		case 10000:
			this.btnFriend5.setSelected(true);
			break;
		default:
			break;
		}
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_shuttle_1:
			setNotiRange(v, 500);
			break;
		case R.id.btn_shuttle_2:
			setNotiRange(v, 1000);
			break;
		case R.id.btn_shuttle_3:
			setNotiRange(v, 3000);
			break;
		case R.id.btn_shuttle_4:
			setNotiRange(v, 5000);
			break;
		case R.id.btn_shuttle_5:
			setNotiRange(v, 10000);
			break;
		case R.id.btn_friend_1:
			setFriendRange(v, 500);
			break;
		case R.id.btn_friend_2:
			setFriendRange(v, 1000);
			break;
		case R.id.btn_friend_3:
			setFriendRange(v, 3000);
			break;
		case R.id.btn_friend_4:
			setFriendRange(v, 5000);
			break;
		case R.id.btn_friend_5:
			setFriendRange(v, 10000);
			break;	
			
		default:
			break;
		}
		
	}
	
	private void setNotiRange(View btn, int value){
		if (!btn.isSelected()) {
			final AccountEntity accountEntity = AccountManager.getInstance().getAccountEntity();
			accountEntity.loadExtra(getActivity());
			clearShuttle();
			btn.setSelected(true);
			accountEntity.notiShuttleRange = value;
			accountEntity.store(getActivity());
			accountEntity.storeExtra(getActivity());
		}
		
	}
	
	private void clearShuttle(){
		this.btnShuttle1.setSelected(false);
		this.btnShuttle2.setSelected(false);
		this.btnShuttle3.setSelected(false);
		this.btnShuttle4.setSelected(false);
		this.btnShuttle5.setSelected(false);
		
	}
	
	private void setFriendRange(View btn, int value){
		if (!btn.isSelected()) {
			final AccountEntity accountEntity = AccountManager.getInstance().getAccountEntity();
			accountEntity.loadExtra(getActivity());
			clearFriend();
			btn.setSelected(true);
			accountEntity.notiFriendRange = value;
			accountEntity.store(getActivity());
			accountEntity.storeExtra(getActivity());
		}
		
	}
	
	private void clearFriend(){
		this.btnFriend1.setSelected(false);
		this.btnFriend2.setSelected(false);
		this.btnFriend3.setSelected(false);
		this.btnFriend4.setSelected(false);
		this.btnFriend5.setSelected(false);
		
	}
}
