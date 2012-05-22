package com.carefactor.samup4web.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.carefactor.samup4web.generic.NotificationActivity;
import com.ericsson.labs.push.android.PushReceiver;
import com.ericsson.labs.push.android.core.PushHandler;


public class CFPushReceiver extends PushReceiver {

	/**
	 * Tag for logging.
	 */
	private static final String TAG = "CAREFACTOR";

	@Override
	public void onPushMessageReceived(Context context, String msg) {
		Log.i(TAG, "onPushMessageReceived(): " + msg);
		launchMessageDisplay(msg, context); 
	}

	@Override
	public void onPushFailed(Context context, Exception e) {
		Log.e(TAG, "onPushFailed()", e);
		// handleError(e, context);
	}

	private void launchMessageDisplay(String msg, Context context) {
		Intent trigger = new Intent(context, NotificationActivity.class);
		trigger.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Store some custom data in the Intent.
		trigger.putExtra("sms", msg);

		// All data set, start the Activity.
		context.startActivity(trigger);
	}
	
	
}