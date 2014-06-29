package com.shuttlemap.android.common;

import com.shuttlemap.android.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class WaitDialog extends Dialog
{
	protected static WaitDialog dialog;
	protected static int refCount = 0;
	
	public static synchronized void showWailtDialog(Context context, boolean cancelable)
	{
		if (dialog != null && dialog.isShowing())
			return;
		
		if (dialog == null)
			dialog = new WaitDialog(context);
		
		dialog.setCancelable(cancelable);
		dialog.show();
		
		refCount++;
	}
	
	public static synchronized void hideWaitDialog()
	{
		if (dialog == null)
			return;
		
		if (refCount == 1)
		{
			dialog.dismiss();
			dialog = null;
		}

		refCount--;
	}
	
	public static synchronized void sweepWaitDialog()
	{
		if (dialog != null)
			dialog.dismiss();
		
		dialog = null;
		refCount = 0;
	}

	public WaitDialog(Context context)
	{
		super(context, R.style.WaitDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.popup_wait_dialog);
	}

}
