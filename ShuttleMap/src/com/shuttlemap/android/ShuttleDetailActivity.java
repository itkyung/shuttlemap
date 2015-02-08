package com.shuttlemap.android;

import java.util.ArrayList;

import com.shuttlemap.android.adapter.RouteListAdapter;
import com.shuttlemap.android.common.bitmapDownloader.BitmapDownloader;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.server.entity.RouteEntity;
import com.shuttlemap.android.server.entity.ShuttleEntity;
import com.shuttlemap.android.server.handler.ShuttleHandler;
import com.shuttlemap.android.views.dialog.BookmarkDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShuttleDetailActivity extends ShuttlemapBaseActivity implements TitleBarListener {
	private Context context;
	private ShuttleEntity shuttle;
	private TitleBar titleBar;
	private int currentTab = 1;
	private static int BASIC_TAB = 1;
	private static int ROUTE_TAB = 2;
	
	private View normalInfo;
	private ListView routeList;
	BitmapFactory.Options bitmapOption;
	private View ivMark1;
	private View ivMark2;
	private RouteListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = this;
		
		Intent intent = getIntent();
		this.shuttle = intent.getParcelableExtra("shuttle");
		
		setContentView(R.layout.activity_shuttle_detail);
		
		
		this.titleBar = (TitleBar)getFragmentManager().findFragmentById(R.id.titleBar);
		this.titleBar.setTitle("셔틀정보");
		
		bitmapOption = new BitmapFactory.Options();
		bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOption.inSampleSize = 2;
		bitmapOption.inDither = false;
		
		
		TextView companyLabel = (TextView)findViewById(R.id.companyLabel);
		companyLabel.setText(shuttle.getCompanyName());
		TextView shuttleLabel = (TextView)findViewById(R.id.shuttleLabel);
		shuttleLabel.setText(shuttle.getShuttleName());
		ImageView imageCompany = (ImageView)findViewById(R.id.imageCompany);
		String imageUrl = shuttle.getCompanyLogoURL();
		if(imageUrl != null){
			BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption, imageCompany, null);
		}
		
		ivMark1 = findViewById(R.id.ivMark1);
		ivMark2 = findViewById(R.id.ivMark2);
		
		findViewById(R.id.tvMenu1R).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(BASIC_TAB);
			}
		});
		findViewById(R.id.tvMenu2R).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(ROUTE_TAB);
			}
		});
		
		this.normalInfo = findViewById(R.id.normalInfo);
		this.routeList = (ListView)findViewById(R.id.routeList);
		this.adapter = new RouteListAdapter(this);
		routeList.setAdapter(adapter);
		
		
		findViewById(R.id.btnBookmark).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				BookmarkDialog dialog = new BookmarkDialog(context,shuttle.getId());
				dialog.show();
				
			}
		});
		
		findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(ShuttleDetailActivity.this,MapRouteActivity.class);
				intent.putExtra("kmlUrl", ServerStaticVariable.KML_PREFIX + shuttle.getRouteFilePath());
				intent.putExtra("routeName", shuttle.getShuttleName());
				startActivity(intent);
			}
		});
		
		findViewById(R.id.btnCall).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shuttle.getDriverPhone()));
				startActivity(intent);
			}
		});
		
		updateNormalInfo();
		refreshRoutes();
		
	}
	
	private void updateNormalInfo(){
		TextView startPointName = (TextView)findViewById(R.id.startPointName);
		startPointName.setText(shuttle.getStartPointName());
		TextView endPointName = (TextView)findViewById(R.id.endPointName);
		endPointName.setText(shuttle.getEndPointName());
		TextView driverName = (TextView)findViewById(R.id.driverName);
		driverName.setText(shuttle.getDriverName());
		TextView driverPhone = (TextView)findViewById(R.id.driverPhone);
		driverPhone.setText(shuttle.getDriverPhone());
		
		driverPhone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shuttle.getDriverPhone()));
				startActivity(intent);
			}
		});
		
		TextView time = (TextView)findViewById(R.id.time);
		time.setText(shuttle.getStartHour() + ":" + shuttle.getStartMinute() + "~" + shuttle.getEndHour() + ":" + shuttle.getEndMinute());
		TextView carNo = (TextView)findViewById(R.id.carNo);
		carNo.setText(shuttle.getCarNo());
		TextView carType = (TextView)findViewById(R.id.carType);
		carType.setText(shuttle.getCarType().getLabel());
		
		
	}

	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
	}

	private void changeTab(int tab){
		currentTab = tab;
		
		if(tab == BASIC_TAB){
			ivMark1.setVisibility(View.VISIBLE);
			ivMark2.setVisibility(View.GONE);
			
			normalInfo.setVisibility(View.VISIBLE);
			routeList.setVisibility(View.GONE);
			
		}else{
			ivMark1.setVisibility(View.GONE);
			ivMark2.setVisibility(View.VISIBLE);
			
			normalInfo.setVisibility(View.GONE);
			routeList.setVisibility(View.VISIBLE);
			
		}
	}
	
	private void refreshRoutes(){
		GetRouteListTask task = new GetRouteListTask();
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,shuttle.getId());
		}else{
			task.execute(shuttle.getId());				
		}
	}
	
	class GetRouteListTask extends AsyncTask<String, Void, ArrayList<RouteEntity>>{

		@Override
		protected ArrayList<RouteEntity> doInBackground(String... params) {
			return ShuttleHandler.getRoutes(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<RouteEntity> result) {
			
			super.onPostExecute(result);
			adapter.setRoutes(result);
			adapter.notifyDataSetChanged();
			
		}
		
	}
	
}
