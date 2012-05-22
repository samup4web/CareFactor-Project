package com.carefactor.samup4web.user;

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

import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.carefactor.samup4web.generic.LoginActivity;
import com.carefactor.samup4web.net.NetParam;
import com.carefactor.samup4web.net.RequestMethod;
import com.carefactor.samup4web.net.RestClient;
import com.carefactor.samup4web.utils.CareFactorException;
import com.carefactor.samup4web.utils.LocationCF;
import com.carefactor.samup4web.utils.MobileLocation;

public class UserAccountManager {

	public boolean userSignIn(String login, String password) throws CareFactorException{

		boolean authResponse = false;
		String signInUrl = "api/cf_auth/sign_in";

		RestClient client = new RestClient(NetParam.httpWebServerAddress
				+ signInUrl);
		client.AddParam("login", login);
		client.AddParam("password", password);
		client.AddHeader("Content-type", "application/x-www-form-urlencoded");
		client.AddHeader("User-Agent", "CareFactor-Android_based");

		try {
			client.Execute(RequestMethod.GET);
			// String responseCode = String.valueOf(client.getResponseCode());

			//Log.d("CAREFACTOR", client.getResponse().trim());
			
			if (client.getResponse().trim().equals("1")) {
				authResponse = true;
			} else {
				authResponse = false;
			}
		} catch (Exception e) {
			//Log.d("CAREFACTOR", "error connecting to server!");
			throw new CareFactorException();
			 
		}
		return authResponse;
	}

	public String userSignUp(String email, String username, String user_type,
			String password) {

		String authResponse = "";
		String signUpUrl = "api/cf_auth/sign_up";

		RestClient client = new RestClient(NetParam.httpWebServerAddress
				+ signUpUrl);
		client.AddParam("email", email);
		client.AddParam("username", username);
		client.AddParam("user_type", user_type);
		client.AddParam("password", password);
		client.AddHeader("Content-type", "application/x-www-form-urlencoded");
		client.AddHeader("User-Agent", "CareFactor-Android_based");

		try {
			client.Execute(RequestMethod.GET);
			// String responseCode = String.valueOf(client.getResponseCode());

			
			
			if (client.getResponse().trim().equals("1")) {
				authResponse = "success";
			} else {
				JSONObject errorJSON = new JSONObject(client.getResponse()
						.trim());

				if (client.getResponse().trim().contains("email")) {
					authResponse = "email error: "
							+ errorJSON.getString("email");

				} else if (client.getResponse().trim().contains("username")) {
					authResponse = "username error: "
							+ errorJSON.getString("username");
				} else {
					authResponse = "Unknown error occured!";
				}

			}

		} catch (Exception e) {

		}
		return authResponse;
	}

	public String fetchUserData(String username) {
		String userdata = "";
		String signInUrl = "api/cf_auth/fetch_user_info";
		RestClient client = new RestClient(NetParam.httpWebServerAddress
				+ signInUrl);
		client.AddParam("username", username);
		client.AddHeader("Content-type", "application/x-www-form-urlencoded");
		client.AddHeader("User-Agent", "CareFactor-Android_based");

		try {
			client.Execute(RequestMethod.GET);
			// String responseCode = String.valueOf(client.getResponseCode());

			userdata = client.getResponse().trim();

		} catch (Exception e) {

		}

		return userdata;
	}

	public void updateUserProfile(final Context context, final Handler handler,
			final int user_id, final double lat, final double lon,
			final String address_line, final String locality,
			final String country, final String phoneNo,
			final boolean isNotificationActive, final boolean isOverrideAutoLocation,
			final String webSite, final String facebookPageLink,
			final String otherContactDetails, final String producerChoiceList) {

		Thread thread = new Thread() {
			@Override
			public void run() {
				String updateUserUrl = "api/cf_auth/update_user_profile";
				RestClient client = new RestClient(
						NetParam.httpWebServerAddress + updateUserUrl);
				client.AddParam("user_id", new Integer(user_id).toString());
				client.AddParam("lat", new Double(lat).toString());
				client.AddParam("lon", new Double(lon).toString());
				client.AddParam("address_line", address_line);
				client.AddParam("locality", locality);
				client.AddParam("country", country);
				client.AddParam("phone_no", phoneNo);
				client.AddParam("is_notification_active", new Boolean(isNotificationActive).toString());
				client.AddParam("is_override_auto_location", new Boolean(isOverrideAutoLocation).toString());
				client.AddParam("web_site", webSite);
				client.AddParam("facebook_page_link", facebookPageLink);
				client.AddParam("other_contact_details", otherContactDetails);
				client.AddParam("choice_producer_list", producerChoiceList);
				
				client.AddHeader("Content-type",
						"application/x-www-form-urlencoded");
				client.AddHeader("User-Agent", "CareFactor-Android_based");

				try {
					client.Execute(RequestMethod.POST);				

					client.getResponse().trim();

				} catch (Exception e) {

				} finally {
					Message msg = Message.obtain();
					msg.setTarget(handler);
					if (true) { // result_locality != null
						msg.what = 1;
						Bundle bundle = new Bundle();
						bundle.putString("response", client.getResponse().trim());
						
						msg.setData(bundle);
					}
					msg.sendToTarget();
				}

			}
		};
		thread.start();
	}

	public static void processLocation(final Context context,
			final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				MobileLocation userLocation = new MobileLocation();
				userLocation.MobileLocationInit(context);
				userLocation.updateCell();
				LocationCF location = userLocation.getLocation();

				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
				String result_locality = null;
				String result_addressLine = null;
				String result_country = null;
				try {
					List<Address> list = geocoder.getFromLocation(
							location.getLat(), location.getLon(), 1);
					if (list != null && list.size() > 0) {
						Address address = list.get(0);
						// sending back first address line and locality
						result_locality = address.getLocality();
						result_addressLine = address.getAddressLine(0);
						result_country = address.getCountryName();
						//Log.e("CAREFACTOR", address.toString());
					}
				} catch (Exception e) {
					//Log.e("CAREFACTOR", "Impossible to connect to Geocoder", e);
				} finally {
					Message msg = Message.obtain();
					msg.setTarget(handler);
					if (true) { // result_locality != null
						msg.what = 1;
						Bundle bundle = new Bundle();
						bundle.putString("addressLine", result_addressLine);
						bundle.putString("locality", result_locality);
						bundle.putString("country", result_country);
						msg.setData(bundle);
					}
					msg.sendToTarget();
				}
			}
		};
		thread.start();
	}
}
