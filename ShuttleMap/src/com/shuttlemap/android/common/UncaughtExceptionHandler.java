package com.shuttlemap.android.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.shuttlemap.android.views.ErrorView;


import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;


public class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler{
	private final Context myContext;

	public UncaughtExceptionHandler(Context context){
		myContext = context;
	}

	public void uncaughtException(Thread thread, Throwable exception){
		final StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		System.err.println(stackTrace);
		
		Log.e("Catch " , "Catch Code ");
		//이메일로 보낸다.
		
		
		Intent intent = new Intent(myContext, ErrorView.class);
		intent.putExtra(ErrorView.STACKTRACE, stackTrace.toString());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		myContext.startActivity(intent); 		
		
		Process.killProcess(Process.myPid());
//		System.exit(10);
	}
}
