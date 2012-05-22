package com.carefactor.samup4web.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class CareFactorDialogs {
	
	
	public void PopIt(Context context, String title, String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
				.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// do stuff onclick of YES
						arg0.cancel();
					}
				}).show();
	}
	
	public ProgressDialog showBusy(Context context, String message){
		ProgressDialog pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage(message);
		pd.setIndeterminate(true);
		pd.setCancelable(true);
		pd.show();
		return pd;
	}
	
	public void endBusy(Context context, ProgressDialog pd){
		pd.dismiss();
	}
}
