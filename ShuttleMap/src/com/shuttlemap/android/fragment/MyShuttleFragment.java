package com.shuttlemap.android.fragment;

import java.util.ArrayList;

import com.shuttlemap.android.LoginActivity;
import com.shuttlemap.android.R;
import com.shuttlemap.android.ShuttleDetailActivity;
import com.shuttlemap.android.adapter.MyShuttleListAdapter;
import com.shuttlemap.android.adapter.MyShuttleListAdapter.BookmarkListener;
import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.fragment.ShuttleListFragment.ShuttleSearchTask;
import com.shuttlemap.android.server.entity.ShuttleEntity;
import com.shuttlemap.android.server.handler.ShuttleHandler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
public class MyShuttleFragment extends Fragment implements BookmarkListener{
	private ListView listView;
	private View needLoginFrame;
	private MyShuttleListAdapter adapter;
	private Context context;
	private int currentPage;
	
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
		currentPage = 1;
		
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.context = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_shuttle, container,false);
		
		listView = (ListView)rootView.findViewById(R.id.listView);
		needLoginFrame = rootView.findViewById(R.id.needLoginFrame);
		
		this.adapter = new MyShuttleListAdapter(context, this);
		listView.setAdapter(adapter);
		
		rootView.findViewById(R.id.btnGoLogin).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),LoginActivity.class);
				startActivityForResult(intent, 1);
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
			getBookmarks();
		}else{
			listView.setVisibility(View.GONE);
			needLoginFrame.setVisibility(View.VISIBLE);
			
		}
		
	}
	
	private void getBookmarks(){
		ShuttleSearchTask task = new ShuttleSearchTask();
		
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}else{
			task.execute();				
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			initView();
			
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	class ShuttleSearchTask extends AsyncTask<Void, Void, ArrayList<ShuttleEntity>>{

		@Override
		protected ArrayList<ShuttleEntity> doInBackground(Void... params) {
			return ShuttleHandler.listBookmark(currentPage);
		}

		@Override
		protected void onPreExecute() {
			//WaitDialog.showWailtDialog(context, false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ArrayList<ShuttleEntity> result) {
			super.onPostExecute(result);
		//	WaitDialog.hideWaitDialog();
			
			adapter.setShuttles(result);
			adapter.notifyDataSetChanged();
			
		}
	}

	
	@Override
	public void viewShuttle(ShuttleEntity shuttle) {
		Intent intent = new Intent(context,ShuttleDetailActivity.class);
		intent.putExtra("shuttle", shuttle);
		startActivity(intent);
	}
	
	
	
}
