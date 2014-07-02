package com.shuttlemap.android.fragment;

import java.util.ArrayList;

import com.shuttlemap.android.R;
import com.shuttlemap.android.ShuttleDetailActivity;
import com.shuttlemap.android.adapter.ShuttleListAdapter;
import com.shuttlemap.android.adapter.ShuttleListAdapter.ShuttleListListener;
import com.shuttlemap.android.common.WaitDialog;
import com.shuttlemap.android.server.entity.ShuttleEntity;
import com.shuttlemap.android.server.handler.ShuttleHandler;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

public class ShuttleListFragment extends Fragment implements ShuttleListListener{
	private ListView listView;
	private EditText searchText;
	private ShuttleListAdapter adapter;
	private Context context;
	private int currentPage;
	
	public static ShuttleListFragment newInstance(){
		ShuttleListFragment fragment = new ShuttleListFragment();
		
		return fragment;
	}
	
	public ShuttleListFragment(){
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentPage = 1;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_shuttle_list, container,false);
		this.listView = (ListView)rootView.findViewById(R.id.listView);
		this.searchText = (EditText)rootView.findViewById(R.id.searchText);
		
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				
				if (s.toString().length() > 1){
					searchShuttle(s.toString());
				}else{
					searchShuttle(s.toString());
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
		
		this.adapter = new ShuttleListAdapter(context,this);
		this.listView.setAdapter(adapter);
		
		initView();
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initView(){
		
		searchShuttle(null);
	}
	
	private void searchShuttle(String keyword){
		ShuttleSearchTask task = new ShuttleSearchTask();
		
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,keyword);
		}else{
			task.execute(keyword);				
		}
	}
	
	class ShuttleSearchTask extends AsyncTask<String, Void, ArrayList<ShuttleEntity>>{

		@Override
		protected ArrayList<ShuttleEntity> doInBackground(String... params) {
			String keyword = params[0];
			return ShuttleHandler.searchShuttle(keyword, currentPage);
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
	public void viewShuttleDetail(ShuttleEntity entity) {
		Intent intent = new Intent(context,ShuttleDetailActivity.class);
		intent.putExtra("shuttle", entity);
		startActivity(intent);
	}
	
	
	
}
