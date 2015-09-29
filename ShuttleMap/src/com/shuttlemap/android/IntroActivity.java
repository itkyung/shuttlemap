package com.shuttlemap.android;




import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.common.GCMManager;
import com.shuttlemap.android.server.entity.AccountEntity;
import com.shuttlemap.android.server.handler.GCMServiceHandler;
import com.shuttlemap.android.server.handler.LoginHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class IntroActivity extends ShuttlemapBaseActivity{
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		this.context = this;
		
		AccountManager.createInstance(this);
		
		autoLogin();
	}
	
	private void autoLogin(){
		AccountManager.getInstance().load();
		AccountEntity account = AccountManager.getInstance().getAccountEntity();
		if(account != null && account.keepLogin){
			//자동 로그인을 시도한다.
			AutoLoginTask task = new AutoLoginTask();
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,account.loginId,account.loginToken);
			}else{
				task.execute(account.loginId,account.loginToken);				
			}
		}else{
			//Login Activity로 이동시킨다.
			goLogin(800);
		}
	}
	
	private void goLogin(int miliseconds){
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(IntroActivity.this,FirstJoinActivity.class);
				startActivity(intent);
				finish();
			}
			
		},miliseconds);
	}
	
	private void goMain(int miliseconds){
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(IntroActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
			
		},miliseconds);
	}
	
	class AutoLoginTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;
			String loginId = params[0];
			String loginToken = params[1];
			
			AccountEntity entity = LoginHandler.login(context, loginId, loginToken);
			if (entity.success){

				AccountManager.getInstance().setAccountEntity(entity);
				AccountManager.getInstance().setLogin(true);
				entity.store(context);
				result = true;
				
				if(entity.isDriver() || entity.sendLocation){
					((ShuttlemapApplication)getApplication()).startLocationUpdate();
				}
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result){
				AccountEntity account = AccountManager.getInstance().getAccountEntity();
				UpdateRegistTask taskU = new UpdateRegistTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					taskU.executeOnExecutor(THREAD_POOL_EXECUTOR,account.id);
				}else{
					taskU.execute(account.id);					
				}
				
				AccountManager.getInstance().setLogin(result);
				
				
//				Bundle bundle = getIntent().getExtras();
//				if (bundle != null && bundle.containsKey("messageIndex")){
//					long messageIndex = bundle.getLong("messageIndex");
//					GCMManager.handleGCMessage(IntroActivity.this, messageIndex);
//					getIntent().putExtra("messageIndex", (long)0);
//				}
				
				
			}else{
				goMain(10);
			}
			super.onPostExecute(result);
		}
	}

	class UpdateRegistTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String regId = GCMManager.getRegistrationId(IntroActivity.this);
			try {
				if (regId == null || "".equals(regId))
					GCMManager.registerGCM(IntroActivity.this);
				else
					GCMServiceHandler.updateGCMRegistrationId(params[0], regId);
			} catch (Exception e){
				
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			goMain(10);
		}
		
		
	}
	
}
