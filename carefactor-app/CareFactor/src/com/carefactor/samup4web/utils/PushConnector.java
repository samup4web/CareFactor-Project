package com.carefactor.samup4web.utils;

import android.content.Context;
import android.util.Log;

import com.ericsson.labs.push.android.ConnectionListener.ConnectionListenerAdapter;
import com.ericsson.labs.push.android.PushReceiver;
import com.ericsson.labs.push.android.core.PushHandler;

public class PushConnector {

	public void connectToPushServer(Context context, String appName) {
		if (!PushHandler.isConnected(context)) {
			try {
				PushHandler.connect(context, appName,
						new ConnectionListenerAdapter() {
							@Override
							public void onConnected() {
								// makeAToast("Connected!");
								Log.d("CAREFACTOR", "PUSH-Connected!");

							}

							@Override
							public void onConnectingFailed(Exception e) {
								// makeAToast("Failed to connect, " +
								// e.getMessage());
								Log.d("CAREFACTOR", "PUSH Connection fail!");
							}
						});
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void disconnectFromPushServer(Context context) {
		try {
			if (PushHandler.isConnected(context)) {
				PushHandler.disconnect(context,
						new ConnectionListenerAdapter() {
							@Override
							public void onDisconnected() {
								Log.d("CAREFACTOR", "PUSH-Disconnected!");
							}

							@Override
							public void onDisconnectingFailed(Exception e) {
								Log.d("CAREFACTOR",
										"PUSH disconnection - fail!");
							}
						});
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
