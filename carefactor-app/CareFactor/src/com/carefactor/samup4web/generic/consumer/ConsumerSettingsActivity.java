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

import com.carefactor.samup4web.generic.producer.ProducerSettingsActivity;
import com.carefactor.samup4web.user.UserAccountManager;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorDialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

public class ConsumerSettingsActivity extends Activity {
	/** Called when the activity is first created. */

	private Button cancelBtn;
	private Button saveBtn;
	private CheckBox notificationToggleBtn;

	private EditText phoneNoText;
	private UserAccountManager userAccMan;
	private UserInfoPersist userInfo;

	private ProgressDialog currentDialog;
	protected CareFactorDialogs cfDialog = new CareFactorDialogs();
	private Button producerChoiceBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View contentView = LayoutInflater.from(getParent()).inflate(
				R.layout.consumer_settings, null);
		setContentView(contentView);
		
		//setContentView(R.layout.consumer_settings);

		userAccMan = new UserAccountManager();

		userInfo = ((UserInfoPersist) getApplicationContext());

		cancelBtn = (Button) findViewById(R.id.btn_consumer_cancel_settings);
		saveBtn = (Button) findViewById(R.id.btn_consumer_save_settings);
		notificationToggleBtn = (CheckBox) findViewById(R.id.notification_toggle);
		phoneNoText = (EditText) findViewById(R.id.txt_phone_no);
		producerChoiceBtn = (Button) findViewById(R.id.btn_select_choice_producers);

		initUserPreference();

		producerChoiceBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(getApplicationContext(),
						ChoiceProducerDialog.class);
				
				
				
				// String city_name = (String)
				// getListAdapter().getItem(position);
//				Bundle b = new Bundle();
//				b.putSerializable("1L", foodBankItem.get(position));
//				
//				i.putExtras(b);

				// Create the view using FirstGroup's LocalActivityManager
				View view = ConsumerSettingsGroupActivity.group
						.getLocalActivityManager()
						.startActivity("settings view",
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();

				// Again, replace the view
				ConsumerSettingsGroupActivity.group.replaceView(view);
			

			}

		});
		
		saveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				userInfo.setPhoneNo(phoneNoText.getText().toString());
				userInfo.setNotificationActive(notificationToggleBtn
						.isChecked());

				
				currentDialog = cfDialog.showBusy(
						ConsumerSettingsGroupActivity.group, "Saving settings...");
				
				

				userAccMan.updateUserProfile(ConsumerSettingsActivity.this,
						new updateUserProfileHandler(), userInfo.getUserId(),
						userInfo.getLat(), userInfo.getLon(), userInfo
								.getAddress().getAddressLine(0), userInfo
								.getAddress().getLocality(), userInfo
								.getAddress().getCountryName(), userInfo
								.getPhoneNo(), userInfo.isNotificationActive(),
						userInfo.isOverrideAutoLocation(), userInfo
								.getWebSite(), userInfo.getFacebookPageLink(),
						userInfo.getOtherContactDetails(), userInfo.getProducerChoiceList());

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

	}

	private class updateUserProfileHandler extends Handler {
		@Override
		public void handleMessage(Message message) {

			cfDialog.endBusy(ConsumerSettingsGroupActivity.group, currentDialog);

			cfDialog.PopIt(ConsumerSettingsGroupActivity.group,
					"User Preference", "Settings saved!");
			Bundle bundle = message.getData();

			String response = "";

			response = bundle.getString("response");

			//Log.d("CAREFACTOR", "user info updated!" + response);

		}
	}

}