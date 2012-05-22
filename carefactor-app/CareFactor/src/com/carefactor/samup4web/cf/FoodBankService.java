package com.carefactor.samup4web.cf;

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

import com.carefactor.samup4web.net.NetParam;
import com.carefactor.samup4web.net.RequestMethod;
import com.carefactor.samup4web.net.RestClient;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.LocationCF;
import com.carefactor.samup4web.utils.MobileLocation;

public class FoodBankService {

	public int submitToFoodBank(String category, String foodname,
			String description, String expiryDate, String availableDate1,
			String availableDate2, boolean isFree, double price,
			String currency, int quantity, String producer, int user_id,
			String units) {

		int responseCode = 0;

		String submit_item_Url = "api/submit/add_to_foodbank";

		RestClient client = new RestClient(NetParam.httpWebServerAddress
				+ submit_item_Url);

		client.AddParam("category", category);
		client.AddParam("foodname", foodname);
		client.AddParam("description", description);
		client.AddParam("expiryDate", expiryDate);
		client.AddParam("availableDate1", availableDate1);
		client.AddParam("availableDate2", availableDate2);
		client.AddParam("isFree", new Boolean(isFree).toString());
		client.AddParam("price", new Double(price).toString());
		client.AddParam("currency", currency);
		client.AddParam("quantity", new Integer(quantity).toString());
		client.AddParam("producer", producer);
		client.AddParam("user_id", new Integer(user_id).toString());
		client.AddParam("units", units);

		client.AddHeader("Content-type", "application/x-www-form-urlencoded");
		client.AddHeader("User-Agent", "CareFactor-Android_based");

		try {
			client.Execute(RequestMethod.POST);
			responseCode = Integer.parseInt(String.valueOf(client
					.getResponseCode()));
			//Log.d("CAREFACTOR", String.valueOf(client.getResponseCode()));
			//Log.d("CAREFACTOR", client.getResponse().trim());

		} catch (Exception e) {

		}

		return responseCode;
	}

	public JSONObject fetchFromFoodBank(String recent, String qry_producer,
			String qry_category, String qry_region, boolean sort_expiry,
			boolean sort_producer, boolean sort_proxmity, boolean sort_submitted) {

		int responseCode;
		JSONObject responseObject = null;

		String get_food_item_url = "api/request/foodbank";

		RestClient client = new RestClient(NetParam.httpWebServerAddress
				+ get_food_item_url);

		client.AddParam("recent", recent);
		client.AddParam("query_producer", qry_producer);
		client.AddParam("query_category", qry_category);
		client.AddParam("query_region", qry_region);
		client.AddParam("sort_expiry_date", new Boolean(sort_expiry).toString());
		client.AddParam("sort_producer", new Boolean(sort_producer).toString());
		client.AddParam("sort_sumitted_date",
				new Boolean(sort_proxmity).toString());
		client.AddParam("sort_proximity",
				new Boolean(sort_submitted).toString());
		//
		client.AddHeader("Content-type", "application/x-www-form-urlencoded");
		client.AddHeader("User-Agent", "CareFactor-Android_based");

		try {
			client.Execute(RequestMethod.GET);
			responseCode = Integer.parseInt(String.valueOf(client
					.getResponseCode()));

			//Log.d("CAREFACTOR", client.getResponse().trim());

			responseObject = new JSONObject(client.getResponse().trim());
			//Log.d("CAREFACTOR", client.getResponse().trim());

		} catch (Exception e) {
			//Log.d("CAREFACTOR", "ERRRORConnecting");
		}

		return responseObject;
	}

	public JSONObject fetchWishList(int userId) {

		int responseCode;
		JSONObject responseObject = null;

		String get_wishlist_url = "api/request/wishlist";

		RestClient client = new RestClient(NetParam.httpWebServerAddress
				+ get_wishlist_url);

		client.AddParam("user_id", new Integer(userId).toString());

		//
		client.AddHeader("Content-type", "application/x-www-form-urlencoded");
		client.AddHeader("User-Agent", "CareFactor-Android_based");

		try {
			client.Execute(RequestMethod.GET);
			responseCode = Integer.parseInt(String.valueOf(client
					.getResponseCode()));

			// Log.d("CAREFACTOR", client.getResponse().trim());

			responseObject = new JSONObject(client.getResponse().trim());
			// Log.d("CAREFACTOR", client.getResponse().trim());

		} catch (Exception e) {
			//Log.d("CAREFACTOR", client.getResponse().trim());
		}

		return responseObject;
	}

	public void sendAdvertToServer(final Context context, final String title,
			final String body, final String recipient, final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				String sendAdvert_url = "api/submit/push_advert";

				RestClient client = new RestClient(
						NetParam.httpWebServerAddress + sendAdvert_url);

				client.AddParam("body", body);
				client.AddParam("title", title);
				client.AddParam("recipient", recipient);				
				client.AddParam(
						"producer_id",
						(new Integer(((UserInfoPersist) context
								.getApplicationContext()).getUserId()))
								.toString());

				client.AddHeader("Content-type",
						"application/x-www-form-urlencoded");
				client.AddHeader("User-Agent", "CareFactor-Android_based");

				try {
					client.Execute(RequestMethod.POST);

					client.getResponse().trim();

					//Log.d("CAREFACTOR", client.getResponse().trim());

				} catch (Exception e) {

				}

				Message msg = Message.obtain();
				msg.setTarget(handler);

				Bundle bundle = new Bundle();
				bundle.putString("response", "");

				msg.setData(bundle);

				msg.sendToTarget();

			}
		};
		thread.start();
	}

	public void addFoodToWishList(final Context context, final String food_id,
			final String user_id, final String unit, final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				String addWishList_url = "api/submit/add_to_wish_list";

				RestClient client = new RestClient(
						NetParam.httpWebServerAddress + addWishList_url);

				client.AddParam("food_id", food_id);
				client.AddParam("user_id", user_id);
				client.AddParam("unit", unit);
				client.AddParam("status", "booked");

				// client.AddParam(
				// "producer_id",
				// (new Integer(((UserInfoPersist) context
				// .getApplicationContext()).getUserId()))
				// .toString());

				client.AddHeader("Content-type",
						"application/x-www-form-urlencoded");
				client.AddHeader("User-Agent", "CareFactor-Android_based");

				try {

					client.Execute(RequestMethod.POST);

					client.getResponse().trim();

					//Log.d("CAREFACTOR", client.getResponse().trim());

				} catch (Exception e) {

				}

				Message msg = Message.obtain();
				msg.setTarget(handler);

				Bundle bundle = new Bundle();
				bundle.putInt("response", client.getResponseCode());

				msg.setData(bundle);

				msg.sendToTarget();

			}
		};
		thread.start();
	}

	public void removeFoodFromWishList(final Context context,
			final String wishlist_id, final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				String removeWishList_url = "api/submit/remove_from_wishlist";

				RestClient client = new RestClient(
						NetParam.httpWebServerAddress + removeWishList_url);

				client.AddParam("wishlist_id", wishlist_id);

				client.AddHeader("Content-type",
						"application/x-www-form-urlencoded");
				client.AddHeader("User-Agent", "CareFactor-Android_based");

				try {

					client.Execute(RequestMethod.POST);

					client.getResponse().trim();

					//Log.d("CAREFACTOR", client.getResponse().trim());

				} catch (Exception e) {

				}

				Message msg = Message.obtain();
				msg.setTarget(handler);

				Bundle bundle = new Bundle();
				bundle.putInt("response", client.getResponseCode());

				msg.setData(bundle);

				msg.sendToTarget();

			}
		};
		thread.start();
	}

	public void getProducerDetails(final Context context,
			final String producer_id, final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {

				String producerDetail_url = "api/request/producer_detail";

				RestClient client = new RestClient(
						NetParam.httpWebServerAddress + producerDetail_url);

				client.AddParam("producer_username", producer_id);

				client.AddHeader("Content-type",
						"application/x-www-form-urlencoded");
				client.AddHeader("User-Agent", "CareFactor-Android_based");

				try {

					client.Execute(RequestMethod.GET);

					
					//Log.d("CAREFACTOR", client.getResponse().trim());

				} catch (Exception e) {

				}

				Message msg = Message.obtain();
				msg.setTarget(handler);

				Bundle bundle = new Bundle();
				bundle.putString("response", client.getResponse().trim());

				msg.setData(bundle);

				msg.sendToTarget();

			}
		};
		thread.start();
	}
	
	public JSONObject getNotifications(Context context,
			 int userid) {

		
			 JSONObject responseObject = null;

			
				String notification_url = "api/request/notifications";

				RestClient client = new RestClient(
						NetParam.httpWebServerAddress + notification_url);

				client.AddParam("userid", new Integer(userid).toString());

				client.AddHeader("Content-type",
						"application/x-www-form-urlencoded");
				client.AddHeader("User-Agent", "CareFactor-Android_based");

				try {

					client.Execute(RequestMethod.GET);

					responseObject = new JSONObject(client.getResponse().trim());
					//Log.d("CAREFACTOR", client.getResponse().trim());

				} catch (Exception e) {

				}

			
				return responseObject;
		
	}
	
	public JSONObject getNotifications_producer(Context context,
			 int userid, String username) {

		
			 JSONObject responseObject = null;

			
				String notification_producer_url = "api/request/notifications_producer";

				RestClient client = new RestClient(
						NetParam.httpWebServerAddress + notification_producer_url);

				client.AddParam("userid", new Integer(userid).toString());
				client.AddParam("username", username);

				client.AddHeader("Content-type",
						"application/x-www-form-urlencoded");
				client.AddHeader("User-Agent", "CareFactor-Android_based");

				try {

					client.Execute(RequestMethod.GET);

					responseObject = new JSONObject(client.getResponse().trim());
					//Log.d("CAREFACTOR", client.getResponse().trim());

				} catch (Exception e) {

				}

			
				return responseObject;
		
	}

}
