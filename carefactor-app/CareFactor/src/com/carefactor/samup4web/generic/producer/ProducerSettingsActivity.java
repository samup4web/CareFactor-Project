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
import com.carefactor.samup4web.generic.consumer.ConsumerSettingsActivity;

import com.carefactor.samup4web.user.UserAccountManager;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorDialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

public class ProducerSettingsActivity extends Activity {
	/** Called when the activity is first created. */

	/**
	 * 
	 * Settings for producers...
	 * 
	 * location settings...
	 * 
	 */
	private Button cancelBtn;
	private Button saveBtn;
	private CheckBox notificationToggleBtn;

	private EditText phoneNoText;
	private UserAccountManager userAccMan;
	private UserInfoPersist userInfo;
	private ProgressDialog currentDialog;
	protected CareFactorDialogs cfDialog = new CareFactorDialogs();
	private EditText addressText;
	private EditText cityText;
	private EditText countryText;
	private CheckBox overrideChkbx;
	private EditText webSiteText;
	private EditText facebookPageText;
	private EditText otherContactDetails;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.producer_settings);

		userAccMan = new UserAccountManager();

		userInfo = ((UserInfoPersist) getApplicationContext());

		cancelBtn = (Button) findViewById(R.id.btn_producer_cancel_settings);
		saveBtn = (Button) findViewById(R.id.btn_producer_save_settings);
		notificationToggleBtn = (CheckBox) findViewById(R.id.notification_toggle);
		phoneNoText = (EditText) findViewById(R.id.txt_phone_no);
		addressText = (EditText) findViewById(R.id.txt_settings_address_no);
		cityText = (EditText) findViewById(R.id.txt_settings_city_no);
		countryText = (EditText) findViewById(R.id.txt_settings_country_no);
		overrideChkbx = (CheckBox) findViewById(R.id.chkbx_location_overide);
		webSiteText = (EditText) findViewById(R.id.txt_settings_web_site);
		facebookPageText = (EditText) findViewById(R.id.txt_settings_facebook_link);
		otherContactDetails = (EditText) findViewById(R.id.txt_settings_other_contact);

		initUserPreference();

		saveBtn.setOnClickListener(new OnClickListener() {

		
			public void onClick(View v) {
				// TODO Auto-generated method stub

				userInfo.setPhoneNo(phoneNoText.getText().toString());
				userInfo.setNotificationActive(notificationToggleBtn
						.isChecked());
				userInfo.setOverrideAutoLocation(overrideChkbx.isChecked());
				userInfo.setWebSite(webSiteText.getText().toString());
				userInfo.setFacebookPageLink(facebookPageText.getText().toString());
				userInfo.setOtherContactDetails(otherContactDetails.getText().toString());
				
				currentDialog = cfDialog.showBusy(ProducerSettingsActivity.this, "Saving settings...");

				userAccMan.updateUserProfile(ProducerSettingsActivity.this,
						new updateUserProfileHandler(), userInfo.getUserId(),
						userInfo.getLat(), userInfo.getLon(), userInfo
								.getAddress().getAddressLine(0), userInfo
								.getAddress().getLocality(), userInfo
								.getAddress().getCountryName(), userInfo
								.getPhoneNo(), userInfo.isNotificationActive(),
								userInfo.isOverrideAutoLocation(), userInfo.getWebSite(),
								userInfo.getFacebookPageLink(), userInfo.getOtherContactDetails(),
								userInfo.getProducerChoiceList());
				
			}

		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void initUserPreference() {
		// TODO Auto-generated method stub
		phoneNoText.setText(userInfo.getPhoneNo());
		notificationToggleBtn.setChecked(userInfo.isNotificationActive());
		addressText.setText(userInfo.getAddress().getAddressLine(0));
		cityText.setText(userInfo.getAddress().getLocality());
		countryText.setText(userInfo.getAddress().getCountryName());
		overrideChkbx.setChecked(userInfo.isOverrideAutoLocation());
		webSiteText.setText(userInfo.getWebSite());
		facebookPageText.setText(userInfo.getFacebookPageLink());
		otherContactDetails.setText(userInfo.getOtherContactDetails());
		

	}

	private class updateUserProfileHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			
			cfDialog.endBusy(ProducerSettingsActivity.this, currentDialog);

			new CareFactorDialogs().PopIt(ProducerSettingsActivity.this,
					"User Preference", "Settings saved!");
			
			Bundle bundle = message.getData();

			String response = "";

			response = bundle.getString("response");

			//Log.d("CAREFACTOR", "user info updated!" + response);

		}
	}
}