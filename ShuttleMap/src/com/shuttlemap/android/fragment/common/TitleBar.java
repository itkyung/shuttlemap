package com.shuttlemap.android.fragment.common;

import java.util.ArrayList;
import java.util.List;

import com.shuttlemap.android.R;




import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


public class TitleBar extends Fragment {

	TextView textTitle;


	ImageView imageBack;

	
	TitleBarListener listener;

	
	PopupWindow noticePopup;
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		this.listener = (TitleBarListener)activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view =  inflater.inflate(R.layout.fragment_title_bar, container,false);
		initView(view);
		return view;
	}
	
	public void initView(final View view){
		
		
		textTitle = (TextView)view.findViewById(R.id.textTitle);
		
		imageBack = (ImageView)view.findViewById(R.id.imageBack);
		imageBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				listener.onBackButtonClicked(TitleBar.this);
			}
		});
		
		
	}

	public void setTitle(String title){
		textTitle.setText(title);
	}
	

	public void hideBackButton(){
		imageBack.setVisibility(View.GONE);
		
	}
	
	

	public interface TitleBarListener
	{
		void onBackButtonClicked(TitleBar titleBar);
		
	}


}
