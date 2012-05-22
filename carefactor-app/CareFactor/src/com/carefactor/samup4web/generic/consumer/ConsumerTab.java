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
import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.generic.LoginActivity;
import com.carefactor.samup4web.generic.producer.ProducerActivity;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.PushConnector;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class ConsumerTab extends TabActivity {
	private TextView userInfoDisplay;
	public UserInfoPersist userInfo;
	TabHost tabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumer_tab);

		userInfoDisplay = (TextView) findViewById(R.id.txt_user_info_display);

		userInfo = ((UserInfoPersist) getApplicationContext());

		userInfo.prepareSharedPreference();

		userInfoDisplay.setText(userInfo.getUsername() + " ");

		/** TabHost will have Tabs */
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		/**
		 * TabSpec used to create a new tab. By using TabSpec only we can able
		 * to setContent to the tab. By using TabSpec setIndicator() we can set
		 * name to tab.
		 */

		/** tid1 is firstTabSpec Id. Its used to access outside. */
		TabSpec homeTab = tabHost.newTabSpec("home_tab");
		TabSpec searchTab = tabHost.newTabSpec("search_tab");
		TabSpec requestTab = tabHost.newTabSpec("request_tab");
		TabSpec settingsTab = tabHost.newTabSpec("settings_tab");

		Resources res = getResources();
		/** TabSpec setIndicator() is used to set name for the tab. */
		/** TabSpec setContent() is used to set content for a particular tab. */
		homeTab.setIndicator("Home", res.getDrawable(R.drawable.home1))
				.setContent(new Intent(this, ConsumerActivity.class));

		// searchTab.setIndicator("Search",
		// res.getDrawable(R.drawable.ic_menu_search)).setContent(
		// new Intent(this, ConsumerSearchActivity.class));

		searchTab.setIndicator("Search",
				res.getDrawable(R.drawable.ic_menu_search1)).setContent(
				new Intent(this, ConsumerSearchGroupActivity.class));
		// .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

		// tabHost.addTab(tabHost.newTabSpec("Tab")

		requestTab.setIndicator("Wish list",
				res.getDrawable(R.drawable.ic_menu_request)).setContent(
				new Intent(this, ConsumerRequestActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		settingsTab.setIndicator("Settings",
				res.getDrawable(R.drawable.ic_menu_settings1)).setContent(
				new Intent(this, ConsumerSettingsGroupActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(homeTab);
		tabHost.addTab(searchTab);
		tabHost.addTab(requestTab);
		tabHost.addTab(settingsTab);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.consumermenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.consumer_menu_logout:
			logOut();
			break;
		case R.id.consumer_menu_browse:
			tabHost.setCurrentTab(1);
			break;
		case R.id.consumer_menu_refresh:
			try {
				((ConsumerActivity) getLocalActivityManager()
						.getCurrentActivity()).refresh();
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				((ConsumerSearchActivity) ConsumerSearchGroupActivity.group.getLocalActivityManager()
						.getCurrentActivity()).refresh();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				((ConsumerRequestActivity) getLocalActivityManager()
						.getCurrentActivity()).refresh();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.consumer_menu_request:
			tabHost.setCurrentTab(2);
			break;
		case R.id.consumer_menu_settings:
			tabHost.setCurrentTab(3);
			break;
		case R.id.consumer_menu_about:
			Toast.makeText(this, "CareFactor Team | EAA 2012",
					Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	public void logOut() {

		new PushConnector().disconnectFromPushServer(this);

		userInfo.clearPreference();
		Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(i);
		finish();
	}

}