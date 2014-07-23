package com.shuttlemap.android.views.dialog;

import com.shuttlemap.android.R;
import com.shuttlemap.android.common.WaitDialog;
import com.shuttlemap.android.server.handler.ShuttleHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class BookmarkDialog extends Dialog {
	private Context context;
	private EditText editBookmark;
	private String shuttleId;
	
	public BookmarkDialog(Context context,String shuttleId) {
		super(context , android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.shuttleId = shuttleId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        
        setContentView(R.layout.popup_bookmark);
        
        editBookmark = (EditText)findViewById(R.id.editBookmarkName);
        
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String bookmarkName = editBookmark.getText().toString();
				if(bookmarkName.length() == 0){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Notice");
					builder.setMessage("북마크이름을 입력하세요.");
					builder.setNegativeButton("OK", null);
					builder.create().show();
					return;
				}
				
				AddBookmarkTask task = new AddBookmarkTask();
				task.execute(shuttleId,bookmarkName);
			}
		});
        
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	class AddBookmarkTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ShuttleHandler.addBookmark(params[0], params[1]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(context, false);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			Toast.makeText(context, "해당 셔틀이 즐겨찾기에 등록되었습니다.", Toast.LENGTH_SHORT).show();
			dismiss();
		}
		
		
	}
	
}
