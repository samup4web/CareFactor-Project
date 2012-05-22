package com.carefactor.samup4web.generic.consumer;

import org.json.JSONException;
import org.json.JSONObject;

import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.utils.CareFactorDialogs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProducerDetailActivity extends Activity {

	public String producer_id;
	private Button locateOnMapBtn;
	
	private String phone_no;
	private String country;
	private String locality;
	private String address_line;
	private String lat;
	private String lon;
	private String ratings_value;
	private String ratings_user_no;
	private String web_site;
	private String facebook_page_link;
	private String other_contact_details;
	private Button facebookBtn;
	private TextView name;
	private TextView phoneNo;
	private TextView address3;
	private TextView address2;
	private TextView address1;
	private TextView otherContactDetails;
	private TextView address_2_3;
	private Button backBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.producer_detail_for_consumer);

		producer_id = this.getIntent().getExtras().getString("producer_id");

		locateOnMapBtn = (Button) findViewById(R.id.pro_detail_locateBtn);
		facebookBtn = (Button) findViewById(R.id.facebook_btn);
		backBtn = (Button) findViewById(R.id.pro_btn_food_detail);
		
		otherContactDetails = (TextView) findViewById(R.id.other_contact_Details);
		address1 = (TextView) findViewById(R.id.pro_detail_address01);
		address2 = (TextView) findViewById(R.id.pro_detail_address02);
		address3 = (TextView) findViewById(R.id.pro_detail_address03);
		phoneNo = (TextView) findViewById(R.id.pro_detail_phone_no);
		name = (TextView) findViewById(R.id.pro_detail_name);
		address_2_3 = (TextView) findViewById(R.id.pro_address_2_3);

		// fetch info for producer from central systems...

		
		getDetails();
		
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ConsumerSearchGroupActivity.group.back();
			}

		});

		locateOnMapBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//String uri = "geo:"+lon+","+lat+"?q="+address1+address2;
				String uri = "geo:"+lat+","+lon;
				startActivity(new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(uri)));
				;
			}

		});
		
		facebookBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//Log.d("FACEBOOK", facebook_page_link);
				String url = facebook_page_link;
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				
			}

		});

	}
	
	private void getDetails(){
		try {
			new FoodBankService().getProducerDetails(
					ProducerDetailActivity.this, producer_id,
					new ProducerDetailHandler());
		} catch (Exception e) {

		}
		
	}

	private class ProducerDetailHandler extends Handler {
		@Override
		public void handleMessage(Message message) {

			// cfDialog.endBusy(ConsumerRequestActivity.this, currentDialog);

			// new CareFactorDialogs().PopIt(ConsumerSearchGroupActivity.group,
			// "Producer details", "Get it all");
			Bundle bundle = message.getData();

			String response = "";

			response = bundle.getString("response");

			try {
				
				
				JSONObject json_result = new JSONObject(response);
				
				//Log.d("CAREFACTOR%", json_result.toString(3));
				
				phone_no = json_result.getString("phone_no");
				country = json_result.getString("country"); 
				locality = json_result.getString("locality");
				address_line = json_result.getString("address_line");
				lat = json_result.getString("lat");
				lon = json_result.getString("lon");
				ratings_value = json_result.getString("ratings_value");
				ratings_user_no = json_result.getString("ratings_user_no");
				web_site= json_result.getString("web_site");
				facebook_page_link = json_result.getString("facebook_page_link");
				other_contact_details = json_result.getString("other_contact_details");
				
				//Log.d("CAREFACTOR", json_result.toString(3));
				
				phoneNo.setText(phone_no);
				address1.setText(address_line);
				address2.setText(locality);
				address3.setText(country);
				address_2_3.setText(locality+", "+country);
				name.setText(producer_id);
				otherContactDetails.setText(other_contact_details);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
