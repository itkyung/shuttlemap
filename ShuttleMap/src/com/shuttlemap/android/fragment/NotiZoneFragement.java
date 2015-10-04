package com.shuttlemap.android.fragment;

import com.shuttlemap.android.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotiZoneFragement extends Fragment {

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
		
		return rootView;
	}
}
