package com.carefactor.samup4web.generic;

import com.carefactor.sam.samup4web.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NotificationActivity extends Activity {

	/**
	 * Tag for logging.
	 */
	private static final String TAG = "ShowMessageActivity";

	private NotificationManager mNotificationManager;
	private int SIMPLE_NOTFICATION_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.d(TAG, "onCreate()");

	

		// Get the sms message from the Intent and display it.
		Intent trigger = getIntent();
		String msg = trigger.getStringExtra("sms");
		
		String[] msg_array = msg.split("#");
		
		String title = msg_array[0];
		
		StringBuilder body = new StringBuilder();
		
		for (int i = 1; i < msg_array.length; i++) {
			body.append(msg_array[i]);
		}
		
		

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		final Notification notifyDetails = new Notification(
				R.drawable.ic_logo_tiny, title,
				System.currentTimeMillis());

		long[] vibrate = { 100, 100, 200, 300 };
		notifyDetails.vibrate = vibrate;
		notifyDetails.defaults = Notification.DEFAULT_ALL;
		notifyDetails.flags = Notification.FLAG_AUTO_CANCEL;
		Context context = getApplicationContext();

		CharSequence contentTitle = "CareFactor: "+title;
		CharSequence contentText = body;
				

		Intent notifyIntent = new Intent(context, LoginActivity.class);

		PendingIntent intent = PendingIntent.getActivity(
				NotificationActivity.this, 0, notifyIntent,
				android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

		notifyDetails.setLatestEventInfo(context, contentTitle, contentText,
				intent);

		mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);
		
		finish();

	}
}