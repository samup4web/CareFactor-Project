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
import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Address;

public class UserInfoPersist extends Application {

	private String username = "";
	private String email = "";
	private String user_type = "";
	private int userid;
	private boolean userIsLoggedIn = false;
	public SharedPreferences cfUserSetting;
	private Address address;
	private double lat;
	private double lon;
	private String phoneNo = "";
	private boolean isNotificationActive = false;
	private boolean isOverrideAutoLocation = false;
	private String webSite = "";
	private String facebookPageLink = "";
	private String otherContactDetails = "";
	private String producerChoiceList = "";
	
	private String share_message;

	public void prepareSharedPreference() {

		cfUserSetting = getSharedPreferences("cfPreferences", MODE_PRIVATE);

	}

	public void clearPreference() {
		setEmail("");
		setUserId(-1);
		setUserIsLoggedIn(false);
		setUsername("");
		setUserType("");
		setAddress(null);
		setPhoneNo("");
		setNotificationActive(false);
		setOverrideAutoLocation(false);
		setWebSite("");
		setFacebookPageLink("");
		setOtherContactDetails("");
		setProducerChoiceList("");
	}

	public int getUserId() {
		// return userid;
		return cfUserSetting.getInt("userid", -1);
	}

	public boolean getUserIsLoggedIn() {
		// return userIsLoggedIn;
		return cfUserSetting.getBoolean("userIsLoggedIn", false);
	}

	public String getUsername() {
		// return username;
		return cfUserSetting.getString("username", "");
	}

	public String getEmail() {
		// return email;
		return cfUserSetting.getString("email", "");
	}

	public String getUserType() {
		// return user_type;
		return cfUserSetting.getString("user_type", "");
	}

	public void setUsername(String username) {
		this.username = username;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("username", username);
		prefEditor.commit();
	}

	public void setEmail(String email) {
		this.email = email;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("email", email);
		prefEditor.commit();
	}

	public void setUserType(String user_type) {
		this.user_type = user_type;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("user_type", user_type);
		prefEditor.commit();
	}

	public void setUserId(int user_id) {
		this.userid = user_id;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putInt("userid", userid);
		prefEditor.commit();
	}

	public void setUserIsLoggedIn(boolean flag) {
		this.userIsLoggedIn = flag;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putBoolean("userIsLoggedIn", userIsLoggedIn);
		prefEditor.commit();
	}

	public Address getAddress() {
		if (this.address == null) {
			this.address = new Address(Locale.getDefault());
		}
		address.setCountryName(cfUserSetting.getString("country", ""));
		address.setLocality(cfUserSetting.getString("locality", ""));
		address.setAddressLine(0, cfUserSetting.getString("addressLine", ""));

		return address;
	}

	public void setAddress(Address address) {
		this.address = address;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		if (address != null) {
			prefEditor.putString("country", address.getCountryName());
			prefEditor.putString("locality", address.getLocality());
			prefEditor.putString("addressLine", address.getAddressLine(0));
		} else {
			prefEditor.putString("country", "");
			prefEditor.putString("locality", "");
			prefEditor.putString("addressLine", "");
		}
		prefEditor.commit();
	}

	public double getLat() {
		return Double.parseDouble(cfUserSetting.getString("lat", ""));
	}

	public void setLat(double lat) {
		this.lat = lat;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("lat", new Double(lat).toString());
		prefEditor.commit();
	}

	public double getLon() {
		return Double.parseDouble(cfUserSetting.getString("lon", ""));
	}

	public void setLon(double lon) {
		this.lon = lon;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("lon", new Double(lon).toString());
		prefEditor.commit();
	}

	public String getPhoneNo() {
		// return phoneNo;
		return cfUserSetting.getString("phoneNo", "");
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("phoneNo", phoneNo);
		prefEditor.commit();
	}

	public boolean isNotificationActive() {
		// return isNotificationActive;
		return cfUserSetting.getBoolean("isNotificationActive", false);
	}

	public void setNotificationActive(boolean isNotificationActive) {
		this.isNotificationActive = isNotificationActive;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putBoolean("isNotificationActive", isNotificationActive);
		prefEditor.commit();
	}

	public boolean isOverrideAutoLocation() {
		// return isOverrideAutoLocation;
		return cfUserSetting.getBoolean("isOverrideAutoLocation", false);
	}

	public void setOverrideAutoLocation(boolean isOverrideAutoLocation) {
		this.isOverrideAutoLocation = isOverrideAutoLocation;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putBoolean("isOverrideAutoLocation", isOverrideAutoLocation);
		prefEditor.commit();
	}

	public String getWebSite() {
		// return webSite;
		return cfUserSetting.getString("webSite", "");
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("webSite", webSite);
		prefEditor.commit();
	}

	public String getFacebookPageLink() {
		// return facebookPageLink;
		return cfUserSetting.getString("facebookPageLink", "");
	}

	public void setFacebookPageLink(String facebookPageLink) {
		this.facebookPageLink = facebookPageLink;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("facebookPageLink", facebookPageLink);
		prefEditor.commit();
	}

	public String getOtherContactDetails() {
		// return otherContactDetails;
		return cfUserSetting.getString("otherContactDetails", "");
	}

	public void setOtherContactDetails(String otherContactDetails) {
		this.otherContactDetails = otherContactDetails;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("otherContactDetails", otherContactDetails);
		prefEditor.commit();
	}

	public String getProducerChoiceList() {
		// return producerChoiceList;
		return cfUserSetting.getString("producerChoiceList", "");
	}

	public void setProducerChoiceList(String producerChoiceList) {
		this.producerChoiceList = producerChoiceList;

		SharedPreferences.Editor prefEditor = cfUserSetting.edit();
		prefEditor.putString("producerChoiceList", producerChoiceList);
		prefEditor.commit();
	}

	public String getShare_message() {
		return share_message;
	}

	public void setShare_message(String share_message) {
		this.share_message = share_message;
	}
}