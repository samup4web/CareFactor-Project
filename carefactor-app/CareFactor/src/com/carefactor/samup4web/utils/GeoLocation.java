package com.carefactor.samup4web.utils;

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

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GeoLocation {

	private static LocationManager locationManager;
	private static String provider;
	protected static Location location2;

	static double lat = 0;
	static double lon = 0;

	public static void processLocation(final Context context,
			final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				MobileLocation userLocation = new MobileLocation();
				userLocation.MobileLocationInit(context);
				userLocation.updateCell();
				LocationCF location = userLocation.getLocation();

				if (true) {

					// Get the location manager
					locationManager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);
					// Define the criteria how to select the locatioin provider
					// -> use
					// default
					Criteria criteria = new Criteria();
					provider = locationManager.getBestProvider(criteria, false);
					location2 = locationManager.getLastKnownLocation(provider);

					// ///

					// ////

					// Initialize the location fields
					if (location2 != null) {
						System.out.println("Provider " + provider
								+ " has been selected.");
						lat = (location2.getLatitude());
						lon = (location2.getLongitude());

						//Log.d("CAREFACTOR--", new Double(lat).toString());
						//Log.d("CAREFACTOR--", new Double(lon).toString());
					} else {
						// latituteField.setText("Provider not available");
						// longitudeField.setText("Provider not available");
						Log.d("CAREFACTOR", "Provider not available");
					}
				}

				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
				String result_locality = null;
				String result_addressLine = null;
				String result_country = null;

				/**
				 * 
				 * 
				 * 
				 * @Todo Geocoder returns null ....
				 * 
				 *       FIX IT...
				 * 
				 * 
				 */

				// Log.e("CAREFACTOR***", new
				// Double(location.getLat()).toString());
				// Log.e("CAREFACTOR***", new
				// Double(location.getLon()).toString());
				try {

					List<Address> list = null;

					try {

						list = geocoder.getFromLocation(location.getLat(),
								location.getLon(), 1);
						
						//Log.d("XXX-", new Double(location.getLat()).toString() + new Double(location.getLon()).toString());
					} catch (Exception e) {
						
						list = geocoder.getFromLocation(
								location2.getLatitude(),
								location2.getLongitude(), 1);
						
						//Log.d("YYY-", new Double(lon).toString()+ new Double(lat).toString());
					}
					
					
					
					if (list != null && list.size() > 0) {
						Address address = list.get(0);
						// sending back first address line and locality
						result_locality = address.getLocality();
						result_addressLine = address.getAddressLine(0);
						result_country = address.getCountryName();
						//Log.e("CAREFACTOR", address.toString());
					}
					Message msg = Message.obtain();
					msg.setTarget(handler);
					if (true) { // result_locality != null
						msg.what = 1;
						Bundle bundle = new Bundle();

						bundle.putString("addressLine", result_addressLine);
						bundle.putString("locality", result_locality);
						bundle.putString("country", result_country);
						bundle.putString("lat", new Double(lat).toString());
						bundle.putString("lon", new Double(lon).toString());

						msg.setData(bundle);
					}
					msg.sendToTarget();
				} catch (Exception e) {
					Log.e("CAREFACTOR", "Impossible to connect to Geocoder", e);
				} finally {

				}
			}
		};
		thread.start();
	}
}
