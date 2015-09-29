package com.shuttlemap.android;

import java.util.Locale;

import com.shuttlemap.android.fragment.ArroundMapFragment;
import com.shuttlemap.android.fragment.MyShuttleFragment;
import com.shuttlemap.android.fragment.NotiFragement;
import com.shuttlemap.android.fragment.SettingFragment;
import com.shuttlemap.android.fragment.ShuttleListFragment;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ShuttlemapBaseActivity implements View.OnClickListener {
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	private boolean mFlag = false;
	private Handler mHandler=null;
	
	ViewPager mViewPager;
	
	private Button menu1;
	private Button menu2;
	private Button menu3;
	private Button menu4;
	private Button menu5;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0){
					mFlag = false;
				    //2초가 지나면 다시 Falg 를 false로 바꾼다.
				    //Log.d("", "handleMessage mFlag : " + mFlag);
				}
			}
		};
	
		menu1 = (Button)findViewById(R.id.menu1);
		menu1.setOnClickListener(this);
		menu2 = (Button)findViewById(R.id.menu2);
		menu2.setOnClickListener(this);
		menu3 = (Button)findViewById(R.id.menu3);
		menu3.setOnClickListener(this);
		menu4 = (Button)findViewById(R.id.menu4);
		menu4.setOnClickListener(this);
		menu5 = (Button)findViewById(R.id.menu5);
		menu5.setOnClickListener(this);
		
		menu1.setSelected(true);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				menu1.setSelected(false);
				menu2.setSelected(false);
				menu3.setSelected(false);
				menu4.setSelected(false);
				menu5.setSelected(false);
				
				switch (position) {
				case 0:
					menu1.setSelected(true);
					break;
				case 1:
					menu2.setSelected(true);
					break;
				case 2:
					menu3.setSelected(true);
					break;
				case 3:
					menu4.setSelected(true);
					break;
				case 4:
					menu5.setSelected(true);
					break;
				default:
					break;
				}
				
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu1:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.menu2:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.menu3:
			mViewPager.setCurrentItem(2);
			break;
		case R.id.menu4:
			mViewPager.setCurrentItem(3);
			break;
		case R.id.menu5:
			mViewPager.setCurrentItem(4);
			break;
		default:
			break;
		}
		
	}



	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch(position){
			case 0:
				fragment = MyShuttleFragment.newInstance();
				break;
			case 1:
				fragment = ShuttleListFragment.newInstance();
				break;
			case 2:
				fragment = ArroundMapFragment.newInstance();
				break;
			case 3:
				fragment = NotiFragement.newInstance();
				break;
			case 4:
				fragment = SettingFragment.newInstance();
				break;
			}
			
			
			return fragment;
		}

		@Override
		public int getCount() {
			
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toLowerCase(l);
			case 4:
				return getString(R.string.title_section4).toLowerCase(l);
			}
			return null;
		}
	}




	@Override
	public void onBackPressed() {
	   if(!mFlag){
		   Toast.makeText(this, getResources().getString(R.string.message_backkey_to_exit), Toast.LENGTH_SHORT).show();
		   mFlag = true;
		   mHandler.sendEmptyMessageDelayed(0, 1000*2);
		   //2초 후에 handleMessage에 메시지를 전달한다.
	   }else{
		   super.onBackPressed();
	   }
    }
}
