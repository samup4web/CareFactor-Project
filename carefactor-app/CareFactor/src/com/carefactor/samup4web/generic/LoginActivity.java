package com.carefactor.samup4web.generic;

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
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.cf.FoodBankItem;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.cf.NoticationItem;
import com.carefactor.samup4web.generic.consumer.ConsumerActivity;
import com.carefactor.samup4web.generic.consumer.ConsumerTab;
import com.carefactor.samup4web.generic.producer.ProducerActivity;
import com.carefactor.samup4web.generic.producer.ProducerTab;
import com.carefactor.samup4web.net.NetParam;
import com.carefactor.samup4web.net.NetworkStatus;
import com.carefactor.samup4web.net.RequestMethod;
import com.carefactor.samup4web.net.RestClient;
import com.carefactor.samup4web.user.UserAccountManager;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorDialogs;
import com.carefactor.samup4web.utils.CareFactorException;
import com.carefactor.samup4web.utils.GeoLocation;
import com.carefactor.samup4web.utils.LocationCF;
import com.carefactor.samup4web.utils.MobileLocation;
import com.ericsson.labs.push.android.core.PushHandler;
import com.ericsson.labs.push.android.ConnectionListener.ConnectionListenerAdapter;

public class LoginActivity extends Activity implements Runnable {

	private Button btn_login;
	private TextView btn_signUp;
	private EditText txt_username;
	private EditText txt_password;
	private TextView txt_message;
	private ProgressDialog pd;
	private boolean auth_flag;
	public UserInfoPersist userInfo;
	private UserAccountManager userAccMan;
	private ScrollView mainScrollView;
	private TextView recoverPasswordBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		userAccMan = new UserAccountManager();

		userInfo = ((UserInfoPersist) getApplicationContext());

		userInfo.prepareSharedPreference();

		new GeoLocation();
		GeoLocation.processLocation(LoginActivity.this, new GeocoderHandler());
		
		
		if (userInfo.getUserIsLoggedIn()) {
			if (userInfo.getUserType().equals("consumer")) {

				Intent i = new Intent(getApplicationContext(),
						ConsumerTab.class);
				startActivity(i);
				finish();
				return;
			} else if (userInfo.getUserType().equals("producer")) {
				Intent i = new Intent(getApplicationContext(),
						ProducerTab.class);
				startActivity(i);
				finish();
				return;
			}
		} else {

		}

		btn_login = (Button) findViewById(R.id.login_button);
		btn_signUp = (TextView) findViewById(R.id.sign_up_button);
		txt_username = (EditText) findViewById(R.id.txt_login_username);
		txt_password = (EditText) findViewById(R.id.txt_login_password);
		txt_message = (TextView) findViewById(R.id.message_text);
		recoverPasswordBtn = (TextView) findViewById(R.id.recover_password_button);

		mainScrollView = (ScrollView) findViewById(R.id.loginScreenScrollView);

		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Connecting to CareFactor service...");
		pd.setIndeterminate(true);
		pd.setCancelable(true);

		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				txt_message.setText("");

				// hide keyboard..
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						mainScrollView.getApplicationWindowToken(), 0);

				// check for internet connection...
				CareFactorDialogs cfDialog = new CareFactorDialogs();
				if (!new NetworkStatus().isNetworkOnline(LoginActivity.this)) {
					cfDialog.PopIt(LoginActivity.this, "Network Error!",
							"Device is not connected to internet");
					return;
				}

				// show busy...
				pd.show();

				Thread thread = new Thread(LoginActivity.this);
				thread.start();
			}

		});

		btn_signUp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				switchToSignUpActivity();
			}

		});

		recoverPasswordBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(NetParam.httpWebServerAddress
								+ "auth/forgot_password"));
				startActivity(browserIntent);

			}

		});

	}

	public void run() {
		// TODO Auto-generated method stub

		userInfo.setUsername(txt_username.getText().toString());

		Message msg = Message.obtain();
		msg.setTarget(handler);
		Bundle bundle = new Bundle();

		try {
			auth_flag = userAccMan.userSignIn(
					txt_username.getText().toString(), txt_password.getText()
							.toString());
			bundle.putBoolean("isConnectionError", false);
		} catch (CareFactorException e) {
			// TODO Auto-generated catch block
			//Log.d("CAREFACTOR", "connection error!");
			bundle.putBoolean("isConnectionError", true);
		}
		msg.setData(bundle);
		msg.sendToTarget();

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Bundle bundle = msg.getData();
			boolean isConnectionError = bundle.getBoolean("isConnectionError");

			pd.dismiss();

			// Success
			if (auth_flag) {
				pd.dismiss();

				userInfo.setUserIsLoggedIn(true);

				storeUserInfo(userAccMan.fetchUserData(userInfo.getUsername()));

				//Log.d("CareFactor", "Log in - Success");
				txt_message.setTextColor(getResources().getColor(R.color.blue));
				txt_message.setText("Welcome to CareFactor");

				if (userInfo.getUserType().equals("producer")) {

					Intent i = new Intent(getApplicationContext(),
							ProducerTab.class);
					// i.putExtra(key, value);
					startActivity(i);

					finish();
				} else if (userInfo.getUserType().equals("consumer")) {
					Intent i = new Intent(getApplicationContext(),
							ConsumerTab.class);
					// i.putExtra(key, value);
					startActivity(i);

					finish();
				}

				// failed
			} else {
				pd.dismiss();
				//Log.d("CAREFACTOR", "Log in - Failure");
				txt_message
						.setTextColor(getResources().getColor(R.color.error));
				if (!isConnectionError) {
					txt_message.setText("Invalid login details");
					Toast.makeText(LoginActivity.this, "Invalid login details",
							Toast.LENGTH_SHORT).show();
				} else {
					txt_message.setText("Error conneting to server.");
					Toast.makeText(LoginActivity.this,
							"Error conneting to server", Toast.LENGTH_SHORT)
							.show();
				}
				mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);

			}

		}
	};

	public void switchToSignUpActivity() {
		finish();
		Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
		startActivity(i);
	}

	protected void storeUserInfo(String userData) {
		// TODO Auto-generated method stub

		try {
			JSONObject userJson = new JSONObject(userData);

			userInfo.setEmail(userJson.getString("email"));
			userInfo.setUserType(userJson.getString("user_type"));
			userInfo.setUserId(userJson.getInt("user_id"));
			userInfo.setPhoneNo(userJson.getString("phone_no"));
			userInfo.setNotificationActive(userJson
					.getBoolean("is_notification_active"));
			userInfo.setOverrideAutoLocation(userJson
					.getBoolean("is_override_auto_location"));
			userInfo.setWebSite(userJson.getString("web_site"));
			userInfo.setFacebookPageLink(userJson
					.getString("facebook_page_link"));
			userInfo.setOtherContactDetails(userJson
					.getString("other_contact_details"));
			userInfo.setProducerChoiceList(userJson
					.getString("choice_producer"));

			//Log.d("CAREFACTOR", userJson.toString(2));

			userAccMan.updateUserProfile(LoginActivity.this,
					new updateUserProfileHandler(), userInfo.getUserId(),
					userInfo.getLat(), userInfo.getLon(), userInfo.getAddress()
							.getAddressLine(0), userInfo.getAddress()
							.getLocality(), userInfo.getAddress()
							.getCountryName(), userInfo.getPhoneNo(), userInfo
							.isNotificationActive(), userInfo
							.isOverrideAutoLocation(), userInfo.getWebSite(),
					userInfo.getFacebookPageLink(), userInfo
							.getOtherContactDetails(), userInfo
							.getProducerChoiceList());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.loginmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login_menu_login:
			// Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();

			break;
		case R.id.login_menu_signup:
			// Toast.makeText(this, "Sign Up", Toast.LENGTH_SHORT).show();
			switchToSignUpActivity();
			break;
		case R.id.login_menu_about:
			Toast.makeText(this, "About CareFactor", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}

	// handles change in orientation...
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

	private class updateUserProfileHandler extends Handler {
		@Override
		public void handleMessage(Message message) {

			Bundle bundle = message.getData();

			String response = "";

			response = bundle.getString("response");

			//Log.d("CAREFACTOR", "user info updated!" + response);

		}
	}

	// geocoder handler

	private class GeocoderHandler extends Handler {
		@Override
		public void handleMessage(Message message) {

			switch (message.what) {
			case 1:
				Bundle bundle = message.getData();

				Address address = new Address(Locale.getDefault());

				//Log.d("CAREFACTOR", address.toString());

				address.setLocality(bundle.getString("locality"));
				address.setAddressLine(0, bundle.getString("addressLine"));
				address.setCountryName(bundle.getString("country"));

				userInfo.setAddress(address);
				userInfo.setLat(Double.parseDouble(bundle.getString("lat")));
				userInfo.setLon(Double.parseDouble(bundle.getString("lon")));

				break;
			default:
				// result = null;
			}

			//Log.d("CAREFACTOR", userInfo.getAddress().getLocality());
			//Log.d("CAREFACTOR", userInfo.getAddress().getAddressLine(0));
			//Log.d("CAREFACTOR", userInfo.getAddress().getCountryName());

			updateLocationText();

		}
	}
	
	

	
	public void updateLocationText() {

		try {
			if (userInfo.getUserType().equals("consumer"))
				ConsumerActivity.getInstance().locationText.setText(userInfo
						.getAddress().getLocality()
						+ ", "
						+ userInfo.getAddress().getCountryName());
			
			///
			//ConsumerActivity.getInstance().locationText.setText("Nara, Japan");


		} catch (Exception e) {

		}
		try {
			if (userInfo.getUserType().equals("producer"))
				ProducerActivity.getInstance().locationText.setText(userInfo
						.getAddress().getLocality()
						+ ", "
						+ userInfo.getAddress().getCountryName());
			///
			//ProducerActivity.getInstance().locationText.setText("Nara, Japan");
			
			
		} catch (Exception e) {
			//Log.d("Erro  sfsd","Cannot update");
		}

	}

}
