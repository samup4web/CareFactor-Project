package com.carefactor.samup4web.generic.producer;

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
import com.carefactor.samup4web.generic.consumer.ConsumerSearchGroupActivity;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.PushConnector;

import android.app.Activity;
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

public class ProducerTab extends TabActivity {
	private TextView userInfoDisplay;
	private UserInfoPersist userInfo;
	private TabHost tabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.producer_tab);
		
		
		
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
		TabSpec addToFoodBankTab = tabHost.newTabSpec("add_to_foodbank_tab");
		TabSpec advertiseTab = tabHost.newTabSpec("advertise_tab");
		TabSpec settingsTab = tabHost.newTabSpec("settings_tab");

		Resources res = getResources();
		/** TabSpec setIndicator() is used to set name for the tab. */
		/** TabSpec setContent() is used to set content for a particular tab. */
		homeTab.setIndicator("Home", res.getDrawable(R.drawable.home1)).setContent(
				new Intent(this, ProducerActivity.class));
		addToFoodBankTab.setIndicator("Add New", res.getDrawable(R.drawable.add_new1)).setContent(
				new Intent(this, ProducerUpdateFoodBankActivity.class));
		advertiseTab.setIndicator("Advertise", res.getDrawable(R.drawable.advert2)).setContent(
				new Intent(this, ProducerPushNotificationActivity.class));
		settingsTab.setIndicator("Settings", res.getDrawable(R.drawable.ic_menu_settings)).setContent(
				new Intent(this, ProducerSettingsActivity.class));

		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(homeTab);
		tabHost.addTab(addToFoodBankTab);
		tabHost.addTab(advertiseTab);
		tabHost.addTab(settingsTab);
		
		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.producermenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.producer_menu_logout:
			// Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();
			logOut();
			// Toast.makeText(this, new
			// Boolean(userInfo.getUserIsLoggedIn()).toString(),
			// Toast.LENGTH_SHORT).show();
			break;
	
		case R.id.producer_menu_refresh:
			
			try{
				((ProducerActivity) getLocalActivityManager().getCurrentActivity()).refresh();
			}catch (Exception e) {
				// TODO: handle exception
			}
			break;
			
		case R.id.producer_menu_about:
			Toast.makeText(this, "CareFactor Team | EAA 2012", Toast.LENGTH_SHORT).show();
			break;
		
			
		case R.id.producer_menu_add_new:
			
			 tabHost.setCurrentTab(1);
			break;
		
		case R.id.producer_menu_advertise:
			tabHost.setCurrentTab(2);
			break;
		case R.id.producer_menu_settings:
			tabHost.setCurrentTab(3);
			break;
		}
		return true;
	}
	
	public void logOut(){
		new PushConnector().disconnectFromPushServer(this);
		userInfo.clearPreference();
		Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(i);
		finish();
	}

}