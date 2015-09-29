package com.shuttlemap.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AgreeActivity extends ShuttlemapBaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_agree);
		
		Button agree = (Button)findViewById(R.id.btnAgree);
		agree.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AgreeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	
	
}
