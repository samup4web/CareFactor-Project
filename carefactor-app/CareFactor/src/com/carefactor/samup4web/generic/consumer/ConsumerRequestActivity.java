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
import com.carefactor.samup4web.cf.FoodBankItem;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.cf.WishListItem;

import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorDialogs;
import com.carefactor.samup4web.utils.CareFactorException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ConsumerRequestActivity extends Activity {
	private ArrayList<WishListItem> wishes;
	private ListWishAdapter m_adapter;
	private Runnable listwishRunnable;
	private ProgressDialog m_ProgressDialog = null;
	private UserInfoPersist userInfo;
	private ListView wishListView;
	private SlidingDrawer uiSlideDrawer;
	private TextView headingText;
	private Button removeFromWishBtn;
	private WishListItem currentWishlistItem = new WishListItem();
	private int currentWishlistIndex;
	private TextView foodNameTxt;
	private TextView categoryTxt;
	private TextView descriptionTxt;
	private TextView pickupDateTxt;
	private TextView producerTxt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumer_request);

		userInfo = ((UserInfoPersist) getApplicationContext());

		wishListView = (ListView) findViewById(R.id.wishList);
		uiSlideDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer_wishlist);
		headingText = (TextView) findViewById(R.id.heading_wishlist_section_title);
		removeFromWishBtn = (Button) findViewById(R.id.btn_picked_remove);
		
		foodNameTxt = (TextView) findViewById(R.id.wish_food_name_txt);
		categoryTxt = (TextView) findViewById(R.id.wish_food_category_txt);
		descriptionTxt = (TextView) findViewById(R.id.wish_food_description_txt);
		pickupDateTxt = (TextView) findViewById(R.id.wish_food_pickup_date_txt);
		producerTxt = (TextView) findViewById(R.id.wish_food_producer_txt);

		
		
		removeFromWishBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				new FoodBankService().removeFoodFromWishList(
						ConsumerRequestActivity.this,
						currentWishlistItem.getWishlist_id(),
						new RemoveFromWishListHandler());
				//Log.d("CAREFACTOR", currentWishlistItem.getWishlist_id());

				m_adapter.remove(wishes.get(currentWishlistIndex));
				
				wishes.remove(currentWishlistIndex);

				m_adapter.notifyDataSetChanged();
				
				headingText.setText("Your wish list (" + wishes.size() + ")");
				
				uiSlideDrawer.animateClose();

			}

		});
		
		//Log.d("CAREFACTOR","request activity...");

		wishListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				uiSlideDrawer.animateToggle();

				// Intent showContent = new Intent(getApplicationContext(),
				// ConsumerFoodDetailActivity.class);
				// //showContent.setData(Uri.parse(content));
				// startActivity(showContent);

				// Intent i = new Intent(getApplicationContext(),
				// ConsumerFoodDetailActivity.class);
				// // String city_name = (String)
				currentWishlistIndex = position;
				currentWishlistItem = wishes.get(position);
				// Bundle b = new Bundle();
				// b.putSerializable("1L", foodBankItem.get(position));

				// i.putExtras(b);

				// Create the view using FirstGroup's LocalActivityManager
				// View view = ConsumerSearchGroupActivity.group
				// .getLocalActivityManager()
				// .startActivity("search view",
				// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				// .getDecorView();

				// Again, replace the view
				// ConsumerSearchGroupActivity.group.replaceView(view);
				
				foodNameTxt.setText(currentWishlistItem.getFoodName());
				categoryTxt.setText("Category: "+currentWishlistItem.getCategory());
				descriptionTxt.setText("Description: "+currentWishlistItem.getDescription());
				pickupDateTxt.setText("Pickup date: "+ currentWishlistItem.getPickUpDateStart() +" to "+currentWishlistItem.getPickUpDateEnd());
				producerTxt.setText("Price: "+currentWishlistItem.getPrice() +" "+currentWishlistItem.getCurrency());
				
			}
		});

		wishes = new ArrayList<WishListItem>();
		this.m_adapter = new ListWishAdapter(this, R.layout.wishlist_row,
				wishes);
		wishListView.setAdapter(this.m_adapter);

		listwishRunnable = new Runnable() {
			// @Override
			public void run() {
				// getOrders();

				try {
					// get items...

					// feed data into wishes variable
					getWishes();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					//Log.d("CAREFACTOR", "Error fetching...2");
				}
			}
		};
		Thread thread = new Thread(null, listwishRunnable, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(ConsumerRequestActivity.this,
				"Please wait...", "Retrieving data ...", true);

		
		
	}
	
	public void refresh(){
		listwishRunnable = new Runnable() {
			// @Override
			public void run() {
				// getOrders();

				try {
					// get items...

					// feed data into wishes variable
					getWishes();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					//Log.d("CAREFACTOR", "Error fetching...2");
				}
			}
		};
		Thread thread = new Thread(null, listwishRunnable, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(ConsumerRequestActivity.this,
				"Please wait...", "Retrieving data ...", true);
	}

	private void getWishes() throws CareFactorException {

		// wishes.add(new WishListItem());

		JSONObject json_result = new FoodBankService().fetchWishList(userInfo
				.getUserId());

		wishes = new ArrayList<WishListItem>();

		try {

			//Log.d("CAREFACTOR", json_result.toString(3));

			int count = Integer.parseInt(json_result.getString("count"));
			if (count > 0) {
				JSONArray wishlist_json = json_result.getJSONArray("wishlist");

				for (int i = 0; i < count; i++) {
					JSONObject per_wishItem = wishlist_json.getJSONObject(i);
					WishListItem tempWish = new WishListItem();

					// tempFood.setCategory(per_foodItem.getString("category"));
					tempWish.setWishlist_id(per_wishItem
							.getString("wishlist_id"));
					tempWish.setBookedQuantity(per_wishItem
							.getInt("wishlist_quantity"));
					tempWish.setCategory(per_wishItem.getString("category"));
					tempWish.setCurrency(per_wishItem.getString("currency"));
					tempWish.setDateBooked(per_wishItem
							.getString("date_booked"));
					tempWish.setDescription(per_wishItem
							.getString("description"));
					tempWish.setExpiryDate(per_wishItem
							.getString("expiry_date"));
					tempWish.setFoodId(per_wishItem
							.getString("wishlist_food_id"));
					tempWish.setFoodName(per_wishItem.getString("foodname"));
					tempWish.setFree(new Boolean(per_wishItem.getString(
							"is_free").toLowerCase()));
					tempWish.setPickUpDateEnd(per_wishItem
							.getString("pick_up_date_end"));
					tempWish.setPickUpDateStart(per_wishItem
							.getString("pick_up_date_start"));
					tempWish.setPrice(per_wishItem.getDouble("price"));
					tempWish.setProducer(per_wishItem.getString("producer_id"));
					tempWish.setProducer_id(per_wishItem.getString("user_id"));
					tempWish.setQuantity(per_wishItem.getInt("quantity"));
					tempWish.setUnit(per_wishItem.getString("unit"));
					tempWish.setUploadDate(per_wishItem
							.getString("update_timestamp"));

					wishes.add(tempWish);
				}
			} else {

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

			throw new CareFactorException();
		}

		runOnUiThread(returnRes);
	}

	private Runnable returnRes = new Runnable() {

		// @Override
		public void run() {
			if (wishes != null && wishes.size() > 0) {
				m_adapter.notifyDataSetChanged();
				m_adapter.clear();
				for (int i = 0; i < wishes.size(); i++)
					m_adapter.add(wishes.get(i));
				m_ProgressDialog.dismiss();
			} else {
				m_adapter.notifyDataSetChanged();
				m_adapter.clear();
				m_ProgressDialog.dismiss();

				Toast.makeText(ConsumerRequestActivity.this,
						"No result found! ", Toast.LENGTH_SHORT).show();

			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
			headingText.setText("Your wish list (" + wishes.size() + ")");
		}
	};

	private class ListWishAdapter extends ArrayAdapter<WishListItem> {

		private ArrayList<WishListItem> items;

		public ListWishAdapter(Context context, int textViewResourceId,
				ArrayList<WishListItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.wishlist_row, null);
			}
			WishListItem wishItem = items.get(position);
			if (wishItem != null) {
				TextView food_txt = (TextView) v
						.findViewById(R.id.wish_row_foodname_txt);
				TextView expiry_txt = (TextView) v
						.findViewById(R.id.wish_row_expirydate_date);
				TextView producer_txt = (TextView) v
						.findViewById(R.id.wish_row_producer_txt);

				if (food_txt != null) {
					food_txt.setText("Name: " + wishItem.getFoodName());
				}
				if (expiry_txt != null) {
					expiry_txt.setText("Expiry date: "
							+ wishItem.getExpiryDate());

				}
				if (producer_txt != null) {
					producer_txt.setText(wishItem.getProducer());
				}

			}
			return v;
		}
	}

	private class RemoveFromWishListHandler extends Handler {
		@Override
		public void handleMessage(Message message) {

			// cfDialog.endBusy(ConsumerRequestActivity.this, currentDialog);

			new CareFactorDialogs().PopIt(ConsumerRequestActivity.this,
					"Wish list", "Item removed!");
			Bundle bundle = message.getData();

			String response = "";

			response = bundle.getString("response");

			//Log.d("CAREFACTOR", "item deleted!" + response);

		}
	}

}