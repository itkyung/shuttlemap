package com.shuttlemap.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class FirstJoinActivity extends ShuttlemapBaseActivity{
	private Context context;
	private EditText phone;
	private String phoneNo;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_join_first);
		
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE); 
		String phoneNum = telManager.getLine1Number();
		
		this.phone = (EditText)findViewById(R.id.editPhone2);
		phone.setText(phoneNum);
		this.phoneNo = phoneNum;
		
		Button loginBtn = (Button)findViewById(R.id.btn_login);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstJoinActivity.this,LoginActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button joinBtn = (Button)findViewById(R.id.btn_join);
		joinBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstJoinActivity.this,RegistAccountActivity.class);
				String no = phone.getText().toString();
				if(no.length() > 0) {
					phoneNo = no;
				}
				intent.putExtra("PHONE", phoneNo);
				startActivity(intent);
				
			}
		});
	}	
	
}
