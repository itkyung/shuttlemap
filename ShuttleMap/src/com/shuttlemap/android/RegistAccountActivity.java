package com.shuttlemap.android;



import com.shuttlemap.android.common.AccountManager;
import com.shuttlemap.android.common.WaitDialog;
import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;
import com.shuttlemap.android.server.ServerStaticVariable;
import com.shuttlemap.android.server.entity.AccountEntity;
import com.shuttlemap.android.server.handler.LoginHandler;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
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
	private EditText editPasswd1;
	private EditText editPasswd2;
	private Button btnDone;
	private EditText editNickName;
	private EditText editPhone;
	private boolean canDone;
	private TitleBar titleBar;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		
		this.titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		this.titleBar.setTitle("회원가입");
		
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE); 
		String phoneNum = telManager.getLine1Number();
		
		android.accounts.AccountManager mgr = android.accounts.AccountManager.get(this);
	    Account[] accts = mgr.getAccounts();
	    final int count = accts.length;
	    Account acct = null;
	              
        for(int i=0;i<count;i++) {
           acct = accts[i];
           break;
        }       
		
		EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
		editBigtureId.setOnFocusChangeListener(this);
		
		editBigtureId.setText(acct.name);
		
		
		editPasswd1 = (EditText)findViewById(R.id.editPassword1);
		editPasswd1.setOnFocusChangeListener(this);
		
		editPasswd2 = (EditText)findViewById(R.id.editPassword2);
		editPasswd2.setOnFocusChangeListener(this);
		
		editNickName = (EditText)findViewById(R.id.editNickname);
		editNickName.setOnFocusChangeListener(this);
		
		editPhone = (EditText)findViewById(R.id.editPhone);
		editPhone.setText(phoneNum);
		
		
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				
				if(validateData()){
					String passwd1 = editPasswd1.getText().toString();
					
					String nickName = editNickName.getText().toString();
					
					EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
					String bigtureId = editBigtureId.getText().toString();
					
					String phone = editPhone.getText().toString();
	
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus == false){
			switch(v.getId()){
			case R.id.editPassword1:
			case R.id.editPassword2:
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
		String passwd = editPasswd1.getText().toString();
		String passwd2 = editPasswd2.getText().toString();
		
		if (passwd == null || passwd.length() < 6){
			canDone = false;
			if(showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("비밀번호는 6자리이상이어야합니다.");
				builder.setNegativeButton("확인", null);
				builder.create().show();
				
				editPasswd1.requestFocus();
				
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
				
				editPasswd1.requestFocus();
			}
			return false;
		}else if(compareTest && passwd != null && passwd2 != null && passwd.length() > 0 &&
				passwd2.length() > 0 && passwd.equals(passwd2) == false){
			canDone = false;
			if (showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("비밀번호가 서로 다릅니다.");
				builder.setNegativeButton("확인", null);
				builder.create().show();
				
				editPasswd2.setText("");
				editPasswd2.requestFocus();
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
		
		String phone = editPhone.getText().toString();
		if (phone == null || phone.length() == 0){
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
				finish();
			}else{
				StringBuffer errorMsg = new StringBuffer("");
				if(errorCode == ServerStaticVariable.ERROR_DUPLICATE){
					errorMsg.append("이미 등록된 아이디(이메일)입니다.");
				}else if(errorCode == ServerStaticVariable.ERROR_MANDATORY){
					errorMsg.append("필수값을 입력하세요.");
				}
				
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
	
}
