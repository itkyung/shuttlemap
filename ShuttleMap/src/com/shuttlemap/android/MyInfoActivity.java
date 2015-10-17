package com.shuttlemap.android;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.server.entity.AccountEntity;

public class MyInfoActivity extends ShuttlemapBaseActivity  {
	
	private Context context;
	
	private Button btnDone;
	private EditText editNickName;
	private EditText editPhone;
	private TextView driverText;
	private ImageButton btnSendLocation;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = this;
		setContentView(R.layout.activity_my_info);
		
		
		final AccountEntity accountEntity = AccountManager.getInstance().entity;
		
		EditText editLoginId = (EditText)findViewById(R.id.editBigtureId);
		editLoginId.setText(accountEntity.loginId);
		
		editNickName = (EditText)findViewById(R.id.editNickname);
		editNickName.setText(accountEntity.name);
		
		
		editPhone = (EditText)findViewById(R.id.editPhone);
		editPhone.setText(accountEntity.phone);
		
		driverText = (TextView)findViewById(R.id.driverText);
		if(accountEntity.isDriver()){
			driverText.setVisibility(View.VISIBLE);
		}else{
			driverText.setVisibility(View.GONE);
		}
		
		btnSendLocation = (ImageButton)findViewById(R.id.btnSendLocation);
		btnSendLocation.setSelected(accountEntity.sendLocation);
		btnSendLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnSendLocation.setSelected(!btnSendLocation.isSelected());
			}
		});
		
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO 이후에 서버에 저장하는 부분을 개발해야한다.
				
				boolean oldSendLocation = accountEntity.sendLocation;
				
				accountEntity.sendLocation = btnSendLocation.isSelected();
				accountEntity.store(context);
				
				if(oldSendLocation != accountEntity.sendLocation){
					//달라졌는지여부.
					changeSendLocationService(accountEntity.sendLocation);
				}
				finish();
			}
		});
	}
	
	public void changeSendLocationService(boolean start){
		if(start){
			((ShuttlemapApplication)getApplication()).startLocationUpdate();
		}else{
			((ShuttlemapApplication)getApplication()).stopLocationUpdate();
		}
		
	}

	
}
