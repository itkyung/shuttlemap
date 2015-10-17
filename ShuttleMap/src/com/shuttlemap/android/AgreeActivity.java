package com.shuttlemap.android;

import com.shuttlemap.android.server.ServerStaticVariable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
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
		
		
		WebView agreeWebview1 = (WebView)findViewById(R.id.agreeWebview1);
		agreeWebview1.loadUrl("file:///android_asset/agree1.html");
		
		WebView agreeWebview2 = (WebView)findViewById(R.id.agreeWebview2);
		agreeWebview2.loadUrl("file:///android_asset/agree2.html");
		
		WebView agreeWebview3 = (WebView)findViewById(R.id.agreeWebview3);
		agreeWebview3.loadUrl("file:///android_asset/agree3.html");
	}

	
	
}
