package com.shuttlemap.android;



import com.shuttlemap.android.IntroActivity.UpdateRegistTask;
import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.common.GCMManager;
import com.shuttlemap.android.common.WaitDialog;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.server.entity.AccountEntity;
import com.shuttlemap.android.server.handler.GCMServiceHandler;
import com.shuttlemap.android.server.handler.LoginHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.view.Window;

public class LoginActivity extends ShuttlemapBaseActivity implements TitleBarListener,View.OnClickListener{
	private Context context;
	private EditText editEmail;
	private EditText editPasswd;
	
	private ImageButton btnKeepLoggedIn;
	private Button btnCreateAccount;
	private TitleBar titleBar;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		this.titleBar = (TitleBar)getFragmentManager().findFragmentById(R.id.titleBar);
		this.titleBar.setTitle("로그인");
		
		editEmail  = (EditText)findViewById(R.id.editEmail);
		editPasswd = (EditText)findViewById(R.id.editPasswd);
		
		Button btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		
		btnKeepLoggedIn = (ImageButton)findViewById(R.id.btnKeepLogin);
		btnKeepLoggedIn.setOnClickListener(this);
		btnKeepLoggedIn.setSelected(true);
		
		TextView tvKeepLoggedIn = (TextView)findViewById(R.id.keepLoginLabel);
		tvKeepLoggedIn.setOnClickListener(this);
		
		this.btnCreateAccount = (Button)findViewById(R.id.createAccount);
		btnCreateAccount.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnKeepLogin:
		case R.id.keepLoginLabel:
			btnKeepLoggedIn.setSelected(!btnKeepLoggedIn.isSelected());
			break;
		case R.id.btnLogin:
			String loginId  = editEmail.getText().toString();
			String passwd = editPasswd.getText().toString();
			boolean keepLogin = btnKeepLoggedIn.isSelected();
			
			if (loginId == null || loginId.length() == 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("로그인");
				builder.setMessage("아이디를 입력하세요.");
				builder.setNegativeButton("확인", null);
				builder.create().show();
				return;
			}
			
			if (passwd == null || passwd.length() == 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("로그인");
				builder.setMessage("비밀번호를 입력하세요.");
				builder.setNegativeButton("확인", null);
				builder.create().show();
				return;
			}
			WaitDialog.showWailtDialog(context, false);
			LoginTask task = new LoginTask();
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,loginId, passwd, keepLogin);
			}else{
				task.execute(loginId, passwd, keepLogin);				
			}
			
			
			break;
	
		case R.id.createAccount:
			Intent intent = new Intent(context,RegistAccountActivity.class);
			startActivity(intent);
			break;
		
		default:
			break;
		}
		
	}




	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		setResult(RESULT_CANCELED);
		finish();
		
	}
	
	
	class LoginTask extends AsyncTask<Object, Void, Boolean>{

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
				
			}
			super.onPostExecute(result);
		}
	}

	class UpdateRegistTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String regId = GCMManager.getRegistrationId(context);
			
			if (regId == null || "".equals(regId))
				GCMManager.registerGCM(context);
			else
				GCMServiceHandler.updateGCMRegistrationId(params[0], regId);
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			setResult(RESULT_OK);
			finish();
		}
		
		
	}
}
