package com.carefactor.samup4web.generic.consumer;

/**
 * 
 * @author Samuel Idowu
 * 
 * 
 * @project CareFactor 
 * @Competition Ericsson Application Awards
 * 
 * 
 */
import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ConsumerSettingsGroupActivity extends ActivityGroup {

	// Keep this in a static variable to make it accessible for all the nesten
	// activities, lets them manipulate the view
	public static ConsumerSettingsGroupActivity group;

	// Need to keep track of the history if you want the back-button to work
	// properly, don't use this if your activities requires a lot of memory.
	private ArrayList<View> history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.history = new ArrayList<View>();
		group = this;

		// you can get the local activitymanager to start the new activity

		View view = getLocalActivityManager().startActivity(
				"ReferenceName",
				new Intent(this, ConsumerSettingsActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);

	}

	public void replaceView(View v) {
		// Adds the old one to history
		history.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}

	public void back() {
		//Log.d("CAREFACTOR", new Integer(history.size()).toString());
		if (history.size() > 1) {
			history.remove(history.size() - 1);
			setContentView(history.get(history.size() - 1));
		} else {
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		ConsumerSettingsGroupActivity.group.back();
		return;
	}
}
