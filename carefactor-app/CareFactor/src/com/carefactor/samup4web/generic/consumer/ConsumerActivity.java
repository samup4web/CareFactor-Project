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
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.cf.NoticationItem;
import com.carefactor.samup4web.cf.WishListItem;

import com.carefactor.samup4web.generic.LoginActivity;
import com.carefactor.samup4web.generic.NotificationActivity;

import com.carefactor.samup4web.generic.producer.ProducerActivity;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorException;
import com.carefactor.samup4web.utils.PushConnector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumerActivity extends Activity {

	private TextView welcomeText;
	public static TextView locationText;
	private UserInfoPersist userInfo;
	private String pushServerAppName;
	private ListView homelist;
	private ProgressDialog m_ProgressDialog = null;
	private ListHomeAdapter not_adapter;
	private ArrayList<NoticationItem> notifications;
	private Runnable seekNotifications;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumer_home);
		
		userInfo = ((UserInfoPersist) getApplicationContext());

		pushServerAppName = "CareFactor";		
		if (!userInfo.getPhoneNo().equals("")) {
			try{
			new PushConnector().connectToPushServer(ConsumerActivity.this,
					pushServerAppName);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		welcomeText = (TextView) findViewById(R.id.txt_welcome_user);
		locationText = (TextView) findViewById(R.id.txt_consumer_location);

		welcomeText.setText("Welcome " + userInfo.getUsername() + " ("
				+ userInfo.getUserType() + ")");

		locationText.setText(userInfo.getAddress().getLocality() + ", "
				+ userInfo.getAddress().getCountryName());

		// //
		//locationText.setText("Nara, Japan");

		homelist = (ListView) findViewById(R.id.consumer_home_list);

		notifications = new ArrayList<NoticationItem>();

		this.not_adapter = new ListHomeAdapter(this,
				R.layout.consumer_homelist_row, notifications);
		homelist.setAdapter(this.not_adapter);

		//
		seekNotifications = new Runnable() {
			// @Override
			public void run() {
				// getOrders();

				try {
					getNotifications();
					// connectionErrorState = false;
				} catch (CareFactorException e) {
					// TODO Auto-generated catch block
					//Log.d("CAREFACTOR", "Error fetching...2");
					// connectionErrorState = true;
					// errorOccuredInThread();
				}
			}
		};
		Thread thread = new Thread(null, seekNotifications, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(ConsumerActivity.this,
				"Please wait...", "Retrieving data ...", true);

	}

	public void refresh() {
		seekNotifications = new Runnable() {
			// @Override
			public void run() {
				// getOrders();

				try {
					getNotifications();
					// connectionErrorState = false;
				} catch (CareFactorException e) {
					// TODO Auto-generated catch block
					//Log.d("CAREFACTOR", "Error fetching...2");
					// connectionErrorState = true;
					// errorOccuredInThread();
				}
			}
		};
		Thread thread = new Thread(null, seekNotifications, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(ConsumerActivity.this,
				"Please wait...", "Retrieving data ...", true);
	}

	private static ConsumerActivity instance;

	public static ConsumerActivity getInstance() {
		return instance;
	}

	private void getNotifications() throws CareFactorException {
		JSONObject notification_json;

		notifications = new ArrayList<NoticationItem>();

		try {

			notification_json = new FoodBankService().getNotifications(
					ConsumerActivity.this, userInfo.getUserId());

			int count = Integer.parseInt(notification_json.getString("count"));

			if (count > 0) {

				JSONArray not_json = notification_json.getJSONArray("not");
				for (int i = 0; i < count; i++) {
					JSONObject per_notif = not_json.getJSONObject(i);
					NoticationItem tempNotif = new NoticationItem();
					tempNotif.setBody(per_notif.getString("not_body"));
					tempNotif.setType(per_notif.getString("not_type"));
					tempNotif.setHeading(per_notif.getString("not_heading"));
					notifications.add(tempNotif);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		runOnUiThread(returnRes);

	}

	private Runnable returnRes = new Runnable() {

		// @Override
		public void run() {
			if (notifications != null && notifications.size() > 0) {
				not_adapter.notifyDataSetChanged();
				not_adapter.clear();

				for (int i = 0; i < notifications.size(); i++) {
					not_adapter.add(notifications.get(i));

				}
				try {
					m_ProgressDialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				not_adapter.notifyDataSetChanged();
				not_adapter.clear();
				m_ProgressDialog.dismiss();

				// Toast.makeText(ConsumerRequestActivity.this,
				// "No result found! ", Toast.LENGTH_SHORT).show();

			}
			try {
				m_ProgressDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			not_adapter.notifyDataSetChanged();

			/**
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
			// Intent trigger = new Intent(ConsumerActivity.this,
			// NotificationActivity.class);
			// trigger.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//
			// // Store some custom data in the Intent.
			// trigger.putExtra("sms",
			// "New update on carefactor # Available food from demo-producer-1");
			//
			// // All data set, start the Activity.
			// ConsumerActivity.this.startActivity(trigger);
			/**
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */

		}
	};

	private class ListHomeAdapter extends ArrayAdapter<NoticationItem> {

		private ArrayList<NoticationItem> items;

		public ListHomeAdapter(Context context, int textViewResourceId,
				ArrayList<NoticationItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.consumer_homelist_row, null);
			}
			NoticationItem notificationItem = items.get(position);
			if (notificationItem != null) {
				TextView heading_txt = (TextView) v
						.findViewById(R.id.notification_heading_txt);

				TextView body_txt = (TextView) v
						.findViewById(R.id.notification_body_txt);

				ImageView img_view = (ImageView) v
						.findViewById(R.id.notification_image);

				if (heading_txt != null) {
					heading_txt.setText("" + notificationItem.getHeading());
				}

				if (body_txt != null) {
					body_txt.setText("" + notificationItem.getBody());
				}
				if (notificationItem.getType().equals("advert")) {
					img_view.setImageResource(R.drawable.advert);
				}

			}
			return v;
		}
	}

}