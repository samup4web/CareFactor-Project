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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class MobileLocation {

	private final static String API_KEY = "kbVK0auycYgYHaHIr47Nmiuq2kH5i4nhhUwN1XVv";

	private TelephonyManager tm;
	private GsmCellLocation location;
	private int cid, lac, mcc, mnc, cellPadding;
	private Context context;

	public void MobileLocationInit(Context context_) {
		context = context_;
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

	}

	/*
	 * 
	 * fetch the current cell info from the phone.
	 */
	public void updateCell() {

		try {
			location = (GsmCellLocation) tm.getCellLocation();
			cid = location.getCid();
			lac = location.getLac();
		} catch (Exception e) {
			// TODO: handle exception
		}

		/*
		 * Mcc and mnc is concatenated in the networkOperatorString. The first 3
		 * chars is the mcc and the last 2 is the mnc.
		 */
		String networkOperator = tm.getNetworkOperator();
		if (networkOperator != null && networkOperator.length() > 0) {
			try {
				mcc = Integer.parseInt(networkOperator.substring(0, 3));
				mnc = Integer.parseInt(networkOperator.substring(3));
			} catch (Exception e) {
			}
		}

		/*
		 * Check if the current cell is a UMTS (3G) cell. If a 3G cell the cell
		 * id padding will be 8 numbers, if not 4 numbers.
		 */
		if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
			cellPadding = 8;
		} else {
			cellPadding = 4;
		}

		/*
		 * Update the GUI with the current cell's info
		 */
		// Log.d("CAREFACTOR", ("CellID: " + getPaddedHex(cid, cellPadding)));
		// Log.d("CAREFACTOR", ("Lac: " + getPaddedHex(lac, 4)));
		// Log.d("CAREFACTOR", ("Mcc: " + getPaddedInt(mcc, 3)));
		// Log.d("CAREFACTOR", ("Mnc: " + getPaddedInt(mnc, 2)));
		// Log.d("CAREFACTOR", ("cid: " + getPaddedInt(cid, 2)));

	}

	/*
	 * Setup a listener for the GetPositionButton. When pressing this button the
	 * cell info is sent to the server and hopefully we will get a longitude and
	 * latitude back.
	 */

	public LocationCF getLocation() {
		// String strResult;
		LocationCF location = null;

		/**
		 * Seems that cid and lac shall be in hex. Cid should be padded with
		 * zero's to 8 numbers if UMTS (3G) cell, otherwise to 4 numbers. Mcc
		 * padded to 3 numbers. Mnc padded to 2 numbers.
		 */
		try {
			// Update the current location
			location = updateLocation(getPaddedHex(cid, cellPadding),
					getPaddedHex(lac, 4), getPaddedInt(mnc, 2),
					getPaddedInt(mcc, 3));
			// strResult = "Position updated!";
		} catch (IOException e) {
			// strResult = "Error!\n" + e.getMessage();
		}

		// Show an info Toast with the results of the updateLocation
		// call.
		// Toast t = Toast.makeText(context.getApplicationContext(), strResult,
		// Toast.LENGTH_LONG);
		// t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		// t.show();
		return location;
	}

	/**
	 * Convert an int to an hex String and pad with 0's up to minLen.
	 */
	String getPaddedHex(int nr, int minLen) {
		String str = Integer.toHexString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * Convert an int to String and pad with 0's up to minLen.
	 */
	String getPaddedInt(int nr, int minLen) {
		String str = Integer.toString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}

	private LocationCF updateLocation(String cid, String lac, String mnc,
			String mcc) throws IOException {

		//Log.d("CAREFACTOR",("cid: " + cid + " , " + lac + " , " + mnc + " , " + mcc));

		LocationCF location = new LocationCF();
		InputStream is = null;
		ByteArrayOutputStream bos = null;
		byte[] data = null;

		try {

			// Build the url
			StringBuilder uri = new StringBuilder(
					"http://cellid.labs.ericsson.net/");
			// Set this param to xml to get the server response in XML instead
			// of json
			uri.append("json");
			uri.append("/lookup?cellid=").append(cid);
			uri.append("&mnc=").append(mnc);
			uri.append("&mcc=").append(mcc);
			uri.append("&lac=").append(lac);
			uri.append("&key=").append(API_KEY);

			// Create an HttpGet request
			HttpGet request = new HttpGet(uri.toString());

			// Send the HttpGet request
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			// Check the response status
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpURLConnection.HTTP_OK) {

				switch (status) {
				case HttpURLConnection.HTTP_NO_CONTENT:
					throw new IOException("The cell could not be "
							+ "found in the database");
				case HttpURLConnection.HTTP_BAD_REQUEST:
					throw new IOException("Check if some parameter "
							+ "is missing or misspelled");
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					throw new IOException("Make sure the API key is "
							+ "present and valid");
				case HttpURLConnection.HTTP_FORBIDDEN:
					throw new IOException("You have reached the limit"
							+ "for the number of requests per day. The "
							+ "maximum number of requests per day is "
							+ "currently 500.");
				case HttpURLConnection.HTTP_NOT_FOUND:
					throw new IOException("The cell could not be found"
							+ "in the database");
				default:
					throw new IOException("HTTP response code: " + status);
				}
			}

			// The response was ok (HTTP_OK) so lets read the data
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			bos = new ByteArrayOutputStream();
			byte buf[] = new byte[256];
			while (true) {
				int rd = is.read(buf, 0, 256);
				if (rd == -1)
					break;
				bos.write(buf, 0, rd);
			}
			bos.flush();
			data = bos.toByteArray();
			if (data != null) {
				try {
					// Parse the Json data
					JSONObject position = new JSONObject(new String(data))
							.getJSONObject("position");
					// update the GUI items with the received position info
//					Log.d("CAREFACTOR",
//							("Longitude: " + position.getDouble("longitude")));
//					Log.d("CAREFACTOR",
//							("Latitude: " + position.getDouble("latitude")));
//
//					Log.d("CAREFACTOR",
//							("Accuracy: " + position.getDouble("accuracy")));
					// Log.d("CAREFACTOR", ("Name: " +
					// position.getString("name")));

					location.setLat(position.getDouble("latitude"));
					location.setLon(position.getDouble("longitude"));
					location.setAccuracy(position.getDouble("accuracy"));

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (MalformedURLException e) {
			Log.e("ERROR", e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IOException(
					"URL was incorrect. Did you forget to set the API_KEY?");
		} finally {
			// make sure we clean up after us
			try {
				if (bos != null)
					bos.close();
			} catch (Exception e) {
			}
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}
		return location;
	}
}
