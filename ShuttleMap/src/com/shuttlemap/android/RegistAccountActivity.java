package com.shuttlemap.android;



import com.shuttlemap.android.LoginActivity.LoginTask;
import com.shuttlemap.android.LoginActivity.UpdateRegistTask;
import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.common.GCMManager;
import com.shuttlemap.android.common.WaitDialog;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.server.entity.AccountEntity;
import com.shuttlemap.android.server.handler.GCMServiceHandler;
import com.shuttlemap.android.server.handler.LoginHandler;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegistAccountActivity extends ShuttlemapBaseActivity implements View.OnFocusChangeListener,View.OnClickListener,TitleBarListener {
	private Context context;
	private EditText editPasswd;
	private Button btnDone;
	private EditText editNickName;
	private String phone;
	private boolean canDone;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		
		android.accounts.AccountManager mgr = android.accounts.AccountManager.get(this);
	    Account[] accts = mgr.getAccounts();
	    
		String defaultEmail = findDefaultEmail(accts);
		EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
		editBigtureId.setOnFocusChangeListener(this);
		
		editBigtureId.setText(defaultEmail);
		
		this.phone = getIntent().getStringExtra("PHONE");
		
		editPasswd = (EditText)findViewById(R.id.editPassword);
		editPasswd.setOnFocusChangeListener(this);
		
		editNickName = (EditText)findViewById(R.id.editNickname);
		editNickName.setOnFocusChangeListener(this);
		
		
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				
				if(validateData()){
					String passwd1 = editPasswd.getText().toString();
					
					String nickName = editNickName.getText().toString();
					
					EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
					String bigtureId = editBigtureId.getText().toString();
				
					AccountRegisterTask task = new AccountRegisterTask();
					task.execute(bigtureId, passwd1, nickName, phone);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Notice");
					builder.setMessage("값을 다 입력하세요.");
					builder.setNegativeButton("확인", null);
					builder.create().show();
				}
			}
		});
	}
	
	private String findDefaultEmail(Account[] accts) {
		final int count = accts.length;
	    Account acct = null;
	              
        for(int i=0;i<count;i++) {
           acct = accts[i];
           if(acct.type.equals("com.google")){
        	   return acct.name;
           }
        }   
        return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus == false){
			switch(v.getId()){
			case R.id.editPassword:
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {
						validatePasswd(true, true);
						validateData();
					}
				}, 200);
				
				
				break;
			case R.id.editNickname:
			case R.id.editBigtureId:
				validateData();
				
				break;
			}
		}
	}

	private boolean validatePasswd(boolean showAlert,boolean compareTest){
		String passwd = editPasswd.getText().toString();
		
		if (passwd == null || passwd.length() < 6){
			canDone = false;
			if(showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("비밀번호는 6자리이상이어야합니다.");
				builder.setNegativeButton("확인", null);
				builder.create().show();
				
				editPasswd.requestFocus();
				
			}
			return false;
		}else if (passwd.length() > 20){
			canDone = false;
			if(showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("비밀번호는 20자리보다 작아야합니다.");
				builder.setNegativeButton("확인", null);
				builder.create().show();
				
				editPasswd.requestFocus();
			}
			return false;
		}
		
		canDone = true;
		return true;
	}
	
	private boolean validateData(){
	
		
		String nickName = editNickName.getText().toString();
		
		EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
		String bigtureId = editBigtureId.getText().toString();
		
		if(bigtureId == null || bigtureId.length() == 0){
			canDone = false;
			return false;
		}
		
		
		boolean result = validatePasswd(false,false);
		
		if(result == false) return false;
		
		
		if (nickName == null || nickName.length() == 0){
			canDone = false;
			return false;
		}
		
		canDone = true;
		
		return true;
	}
	

	class AccountRegisterTask extends AsyncTask<String,Void,Boolean>{
		//socialType, socialId, bigtureId, passwd1, nickName, birthdayString, gender, countryCode
		private int errorCode;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			WaitDialog.showWailtDialog(context, false);
		}

		@Override
		protected Boolean doInBackground(String... params){
		
			String loginId     = params[0];
			String passwd    = params[1];
			String nickName = params[2];
			String phone  = params[3];
			
			boolean result = LoginHandler.registAccount(loginId,passwd,nickName,phone);
			if (result){
			
				AccountManager.getInstance().setLogin(false);
				return true;
			}
			
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result){
			WaitDialog.hideWaitDialog();
			if (result){
				setResult(RESULT_OK);
				
				EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
				String loginId = editBigtureId.getText().toString();
				String passwd = editPasswd.getText().toString();
				
				LoginTask task = new LoginTask();
				if(Build.VERSION.SDK_INT >= 11){
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,loginId, passwd, true);
				}else{
					task.execute(loginId, passwd, true);				
				}
				
				
			}else{
				StringBuffer errorMsg = new StringBuffer("");
				//if(errorCode == ServerStaticVariable.ERROR_DUPLICATE){
					errorMsg.append("이미 등록된 아이디(이메일)입니다.");
				//}else if(errorCode == ServerStaticVariable.ERROR_MANDATORY){
				//	errorMsg.append("필수값을 입력하세요.");
				//}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(errorMsg.toString());
				builder.setNegativeButton("확인", null);
				builder.create().show();
			}
			
			
			
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			WaitDialog.hideWaitDialog();
			super.onCancelled();
		}
	}


	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
	}
	
	class LoginTask extends AsyncTask<Object, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			WaitDialog.showWailtDialog(context, false);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			String loginId = (String)params[0];
			String passwd = (String)params[1];
			boolean keepLogin = (Boolean)params[2];
			
			AccountEntity entity = LoginHandler.login(context, loginId, passwd, keepLogin);
			if (entity.success){

				AccountManager.getInstance().setAccountEntity(entity);
				AccountManager.getInstance().setLogin(true);
				entity.store(context);
				result = true;
				
				//if(entity.isDriver() || entity.sendLocation){
					((ShuttlemapApplication)getApplication()).startLocationUpdate();
			//	}
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			WaitDialog.hideWaitDialog();
			if (result){
				AccountEntity account = AccountManager.getInstance().getAccountEntity();
				UpdateRegistTask taskU = new UpdateRegistTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					taskU.executeOnExecutor(THREAD_POOL_EXECUTOR,account.id);
				}else{
					taskU.execute(account.id);					
				}
				
				AccountManager.getInstance().setLogin(result);
				
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("로그인 에러");
				builder.setMessage("아이디 또는 비밀번호를 확인하세요.");
				builder.setNegativeButton("확인", null);
				builder.create().show();
			}
			super.onPostExecute(result);
		}
	}

	class UpdateRegistTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String regId = GCMManager.getRegistrationId(context);
			try {
				if (regId == null || "".equals(regId))
					GCMManager.registerGCM(context);
				else
					GCMServiceHandler.updateGCMRegistrationId(params[0], regId);
			} catch (Exception e) {
				//무시함. 에뮬레이터 테스트임.
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			Intent intent = new Intent(RegistAccountActivity.this, AgreeActivity.class);
			startActivity(intent);
			finish();
		}
		
		
	}
}
