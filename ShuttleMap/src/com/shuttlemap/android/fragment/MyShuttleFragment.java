package com.shuttlemap.android.fragment;

import com.shuttlemap.android.R;
import com.shuttlemap.android.common.AccountManager;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 첫번재 Section인 내가 Bookmark한 셔틀리스트.
 * @author bizwave
 *
 */
public class MyShuttleFragment extends Fragment {
	private ListView listView;
	private View needLoginFrame;
	
	public static MyShuttleFragment newInstance(){
		MyShuttleFragment fragment = new MyShuttleFragment();
		Bundle args = new Bundle();
		//args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public MyShuttleFragment(){
		Bundle bundle = getArguments();
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_shuttle, container,false);
		
		listView = (ListView)rootView.findViewById(R.id.listView);
		needLoginFrame = rootView.findViewById(R.id.needLoginFrame);
		
		rootView.findViewById(R.id.btnGoLogin).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		initView();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		initView();
	}
	
	private void initView(){
		if(AccountManager.isLogin()){
			listView.setVisibility(View.VISIBLE);
			needLoginFrame.setVisibility(View.GONE);
			
			
			
		}else{
			listView.setVisibility(View.GONE);
			needLoginFrame.setVisibility(View.VISIBLE);
			
		}
		
	}
	
}
