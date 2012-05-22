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
import java.io.IOException;
import java.io.Serializable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.cf.FoodBankItem;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorDialogs;

import com.facebook.android.FacebookConnect;

public class ConsumerFoodDetailActivity extends Activity {
	private FoodBankItem foodBankItem;

	private TextView txtDescription;
	private TextView txtExpiryDate;
	private TextView txtPickUpDate;
	private TextView txtProducer;
	private TextView txtQuantity;
	private TextView txtPrice;

	private TextView txtFoodName;

	private Button searchListBtn;

	private Button producerDetails;

	private Button addToWishListBtn;

	private UserInfoPersist userInfo;

	private EditText desiredUnitText;

	private ProgressDialog currentDialog;

	Intent intent;

	protected CareFactorDialogs cfDialog = new CareFactorDialogs();

	private Button shareBtn;

	private Button otherShareBtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_detail_info);
		
		userInfo = ((UserInfoPersist) getApplicationContext());

		searchListBtn = (Button) findViewById(R.id.btn_search_list);
		addToWishListBtn = (Button) findViewById(R.id.btn_Add_to_wish_list);
		producerDetails = (Button) findViewById(R.id.btn_producer_details);
		desiredUnitText = (EditText) findViewById(R.id.txt_desired_unit);

		Bundle b = this.getIntent().getExtras();
		if (b != null)
			foodBankItem = (FoodBankItem) b.getSerializable("1L");

		txtFoodName = (TextView) findViewById(R.id.food_name_txt);
		txtDescription = (TextView) findViewById(R.id.food_description_txt);
		txtExpiryDate = (TextView) findViewById(R.id.food_expiry_date_txt);
		txtPickUpDate = (TextView) findViewById(R.id.food_pickup_date_txt);
		txtProducer = (TextView) findViewById(R.id.food_producer_txt);
		txtQuantity = (TextView) findViewById(R.id.food_quantity_txt);
		txtPrice = (TextView) findViewById(R.id.food_price_txt);

		txtFoodName.setText(foodBankItem.getFoodName());
		txtDescription.setText(foodBankItem.getDescription());
		txtExpiryDate.setText(foodBankItem.getExpiryDate());
		txtPickUpDate.setText(foodBankItem.getPickUpDateStart() + " - to -"
				+ foodBankItem.getPickUpDateEnd());
		txtQuantity.setText(new Integer(foodBankItem.getQuantity()).toString()
				+ " " + foodBankItem.getUnit());
		txtProducer.setText(foodBankItem.getProducer());
		txtPrice.setText(new Double(foodBankItem.getPrice()).toString() + " "
				+ foodBankItem.getCurrency());

		shareBtn = (Button) findViewById(R.id.btn_share);
		
		otherShareBtn = (Button) findViewById(R.id.btn_other_share);

		searchListBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ConsumerSearchGroupActivity.group.back();

			}
		});

		
		// android.widget.ImageView facebookButton =
		// (android.widget.ImageView)findViewById(R.id.facebookbut);

		intent = new Intent(getApplicationContext(), FacebookConnect.class);
		
		shareBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
								
				userInfo.setShare_message(" Food Type: "+foodBankItem.getFoodName()+", Producer: "+foodBankItem.getProducer()+ ", available till "+foodBankItem.getPickUpDateEnd()+" @CareFactor");
				startActivity(intent);

//			
			}
		});
		
		otherShareBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final Intent intent = new Intent(Intent.ACTION_SEND);

				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "CareFactor: ");
				intent.putExtra(Intent.EXTRA_TEXT,
				"I just discovered free/cheap food item via @carefactorApp " + ". Food Type: "+foodBankItem.getFoodName()+", Producer: "+foodBankItem.getProducer());

				startActivity(Intent.createChooser(intent,
				"Select an action for sharing"));
//			
			}
		});

		producerDetails.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(),
						ProducerDetailActivity.class);

				i.putExtra("producer_id", foodBankItem.getProducer());

				// Create the view using FirstGroup's LocalActivityManager
				View view = ConsumerSearchGroupActivity.group
						.getLocalActivityManager()
						.startActivity("producer detail",
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();

				// Again, replace the view
				ConsumerSearchGroupActivity.group.replaceView(view);

			}
		});

		addToWishListBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				// check if desired text is valid....
				int unit = 1;
				try {
					unit = Integer.parseInt(desiredUnitText.getText()
							.toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
					new CareFactorDialogs().PopIt(
							ConsumerSearchGroupActivity.group,
							"Error: wish List ", "Add valid quantity value");

					return;
				}

				if (unit < 1) {

					new CareFactorDialogs().PopIt(
							ConsumerSearchGroupActivity.group,
							"Error: wish List ", "Add valid quantity value");

					return;

				}
				if (unit > foodBankItem.getQuantity()) {
					new CareFactorDialogs().PopIt(
							ConsumerSearchGroupActivity.group,
							"Error: wish List ",
							"Quantity is more than available quantity");
					return;
				}

				userInfo = ((UserInfoPersist) getApplicationContext());

				currentDialog = cfDialog.showBusy(
						ConsumerSearchGroupActivity.group,
						"Adding food item to your wish list...");

				new FoodBankService().addFoodToWishList(
						ConsumerFoodDetailActivity.this,
						foodBankItem.getFoodId(),
						new Integer(userInfo.getUserId()).toString(),
						new Integer(unit).toString(),
						new AddToWishListHandler());

			}
		});
	}

	private class AddToWishListHandler extends Handler {
		@Override
		public void handleMessage(Message message) {

			cfDialog.endBusy(ConsumerSearchGroupActivity.group, currentDialog);

			Bundle bundle = message.getData();

			int response = 0;

			response = bundle.getInt("response");
			if (response == 200) {
				//Log.d("CAREFACTOR", "user info updated!" + response);
				new CareFactorDialogs().PopIt(
						ConsumerSearchGroupActivity.group, "wish List",
						"Item added to your list!");

				TabHost tabHost = (TabHost) ConsumerSearchGroupActivity.group
						.getParent().findViewById(android.R.id.tabhost);
				tabHost.setCurrentTab(2);
			} else {
				new CareFactorDialogs().PopIt(
						ConsumerSearchGroupActivity.group, "Error: wish List ",
						"Cannot add to wish list!");
			}

		}
	}

}
