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
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.cf.FoodBankItem;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.utils.CareFactorDialogs;
import com.carefactor.samup4web.utils.CareFactorException;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

public class ConsumerSearchActivity extends Activity {
	private Spinner spnrRegion;
	private Spinner spnrProducer;
	private Spinner spnrCategory;

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<FoodBankItem> m_orders = null;
	private OrderAdapter m_adapter;
	private Runnable viewSearchFoodItems;
	private ArrayList<FoodBankItem> foodBankItem;

	private CheckBox chkboxExpiryDate;
	private CheckBox chkboxProducer;
	private CheckBox chkboxSubmittedDate;
	private CheckBox chkboxProximity;
	private Runnable searchFoodBank;
	private Button searchFoodBankBtn;
	private SlidingDrawer slidingDrawerSearchFilter;

	private final CareFactorDialogs cfDialog = new CareFactorDialogs();
	protected boolean connectionErrorState = false;
	private TextView searchSectionTitle;
	private int result_count=0;
	private String _recent;
	private String _qry_producer;
	private String _qry_category;
	private String _qry_region;
	private boolean _sort_expiry;
	private boolean _sort_producer;
	private boolean _sort_proxmity;
	private boolean _sort_submitted;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View contentView = LayoutInflater.from(getParent()).inflate(
				R.layout.consumer_search, null);
		setContentView(contentView);
		
		
		
		searchSectionTitle = (TextView)findViewById(R.id.search_section_title); 

		searchFoodBankBtn = (Button) findViewById(R.id.btn_foodbank_search);
		spnrRegion = (Spinner) findViewById(R.id.spnr_regions);
		spnrCategory = (Spinner) findViewById(R.id.spnr_category);
		spnrProducer = (Spinner) findViewById(R.id.spnr_producers);

		chkboxExpiryDate = (CheckBox) findViewById(R.id.chkbox_expiry_date);
		chkboxProducer = (CheckBox) findViewById(R.id.chkbox_producer);
		chkboxProximity = (CheckBox) findViewById(R.id.chkbox_proximity);
		chkboxSubmittedDate = (CheckBox) findViewById(R.id.chkbox_submitted_date);
		slidingDrawerSearchFilter = (SlidingDrawer) findViewById(R.id.slidingDrawer_search_filter);

		final ListView searchListView = (ListView) findViewById(R.id.searchList);
		// searchListView.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, myList));

		searchListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				// Intent showContent = new Intent(getApplicationContext(),
				// ConsumerFoodDetailActivity.class);
				// //showContent.setData(Uri.parse(content));
				// startActivity(showContent);

				Intent i = new Intent(getApplicationContext(),
						ConsumerFoodDetailActivity.class);
				// String city_name = (String)
				// getListAdapter().getItem(position);
				Bundle b = new Bundle();
				b.putSerializable("1L", foodBankItem.get(position));

				i.putExtras(b);

				// Create the view using FirstGroup's LocalActivityManager
				View view = ConsumerSearchGroupActivity.group
						.getLocalActivityManager()
						.startActivity("search view",
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();

				// Again, replace the view
				ConsumerSearchGroupActivity.group.replaceView(view);
			}
		});

		prepareSpinners();

		searchFoodBankBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				slidingDrawerSearchFilter.animateClose();

				// final SearchParam newSearchParam = new SearchParam();
				//
				// newSearchParam.setCategory(spnrCategory.getSelectedItem().toString());
				// newSearchParam.setProducer(spnrProducer.getSelectedItem().toString());
				// newSearchParam.setRegion(spnrRegion.getSelectedItem().toString());
				//
				// newSearchParam.setChkExpiryDate(chkboxExpiryDate.isChecked());
				// newSearchParam.setChkProducer(chkboxProducer.isChecked());
				// newSearchParam.setChkProximity(chkboxProximity.isChecked());
				// newSearchParam.setChkSubmittedDate(chkboxSubmittedDate.isChecked());

				searchFoodBank = new Runnable() {
					public void run() {
						// getOrders();
						try {
							// (qry_producer, qry_category, qry_region,
							// sort_expiry, sort_producer, sort_proxmity,
							// sort_submitted)
							getFoodItems("false", spnrProducer
									.getSelectedItem().toString(), spnrCategory
									.getSelectedItem().toString(), spnrRegion
									.getSelectedItem().toString(),
									chkboxExpiryDate.isChecked(),
									chkboxProducer.isChecked(), chkboxProximity
											.isChecked(), chkboxSubmittedDate
											.isChecked());
						} catch (CareFactorException e) {
							// TODO Auto-generated catch block
							//Log.d("CAREFACTOR", "Error fetching...1");
						}
					}
				};
				Thread thread = new Thread(null, searchFoodBank,
						"MagentoBackground2");
				thread.start();
				m_ProgressDialog = ProgressDialog.show(
						ConsumerSearchGroupActivity.group, "Please wait...",
						"Retrieving data ...", true);

			}
		});
		
		
		

		m_orders = new ArrayList<FoodBankItem>();
		this.m_adapter = new OrderAdapter(this, R.layout.search_row, m_orders);
		searchListView.setAdapter(this.m_adapter);

		/**
		 * 
		 * search new....
		 * 
		 */
		viewSearchFoodItems = new Runnable() {
			// @Override
			public void run() {
				// getOrders();

				try {
					getFoodItems("true", "prod", "prod", "prod", false, false,
							false, false);
					_recent = "true";
					_qry_producer = "prod";
					_qry_category = "prod";
					_qry_region =  "prod";
					_sort_expiry = false;
					_sort_producer = false;
					_sort_proxmity = false;
					_sort_submitted = false;
					
					connectionErrorState = false;
				} catch (CareFactorException e) {
					// TODO Auto-generated catch block
					//Log.d("CAREFACTOR", "Error fetching...2");
					connectionErrorState = true;
					errorOccuredInThread();
				}
			}
		};
		Thread thread = new Thread(null, viewSearchFoodItems,
				"MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(
				ConsumerSearchGroupActivity.group, "Please wait...",
				"Retrieving data ...", true);

	}

	public void refresh(){
		viewSearchFoodItems = new Runnable() {
			// @Override
			public void run() {
				// getOrders();

				try {
					getFoodItems(_recent, _qry_producer, _qry_category, _qry_region, _sort_expiry, _sort_producer,
							_sort_proxmity, _sort_submitted);
					connectionErrorState = false;
				} catch (CareFactorException e) {
					// TODO Auto-generated catch block
					//Log.d("CAREFACTOR", "Error fetching...2");
					connectionErrorState = true;
					errorOccuredInThread();
				}
			}
		};
		Thread thread = new Thread(null, viewSearchFoodItems,
				"MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(
				ConsumerSearchGroupActivity.group, "Please wait...",
				"Retrieving data ...", true);
	}
	@Override
	protected void onPause() {

		super.onPause();

	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	public void prepareSpinners() {
		// Perfect styling
		// spnrRegion = (Spinner) findViewById(R.id.spnr_regions);
		ArrayAdapter<CharSequence> region_adapter = ArrayAdapter
				.createFromResource(this.getParent(), R.array.region,
						R.layout.spinner_layout);
		region_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnrRegion.setAdapter(region_adapter);

		// Perfect styling
		// spnrProducer = (Spinner) findViewById(R.id.spnr_producers);
		ArrayAdapter<CharSequence> producer_adapter = ArrayAdapter
				.createFromResource(this.getParent(), R.array.producer,
						R.layout.spinner_layout);
		producer_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnrProducer.setAdapter(producer_adapter);

		// Perfect styling
		// spnrCategory = (Spinner) findViewById(R.id.spnr_category);
		ArrayAdapter<CharSequence> category_adapter = ArrayAdapter
				.createFromResource(this.getParent(), R.array.food_category,
						R.layout.spinner_layout);
		category_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnrCategory.setAdapter(category_adapter);
	}

	protected void getFoodItems(String recent, String qry_producer,
			String qry_category, String qry_region, boolean sort_expiry,
			boolean sort_producer, boolean sort_proxmity, boolean sort_submitted)
			throws CareFactorException {
		// TODO Auto-generated method stub
		JSONObject json_result = new FoodBankService().fetchFromFoodBank(
				recent, qry_producer, qry_category, qry_region, sort_expiry,
				sort_producer, sort_proxmity, sort_submitted);

		_recent = recent;
		_qry_producer = qry_producer;
		_qry_category = qry_category;
		_qry_region =  qry_region;
		_sort_expiry = sort_expiry;
		_sort_producer = sort_producer;
		_sort_proxmity = sort_proxmity;
		_sort_submitted = sort_submitted;
		
		
		foodBankItem = new ArrayList<FoodBankItem>();

		/**
		 * ', '', '', '', '', '', '', '', '', '', '', '', 'pick_up_date_end',
		 * 'update_timestamp'
		 */

		try {

			int count = Integer.parseInt(json_result.getString("count"));
			result_count = count;
			if (count > 0) {
				JSONArray foodbank_json = json_result.getJSONArray("foodbank");

				for (int i = 0; i < count; i++) {
					JSONObject per_foodItem = foodbank_json.getJSONObject(i);
					FoodBankItem tempFood = new FoodBankItem();
					tempFood.setCategory(per_foodItem.getString("category"));
					tempFood.setProducer(per_foodItem.getString("producer_id"));
					tempFood.setDescription(per_foodItem
							.getString("description"));
					tempFood.setExpiryDate(per_foodItem
							.getString("expiry_date"));
					tempFood.setFree(new Boolean(per_foodItem.getString(
							"is_free").toLowerCase()));
					tempFood.setPrice(Double.parseDouble(per_foodItem
							.getString("price")));
					tempFood.setFoodName(per_foodItem.getString("foodname"));
					tempFood.setFoodId(per_foodItem.getString("id"));

					tempFood.setQuantity(Integer.parseInt(per_foodItem
							.getString("quantity")));
					tempFood.setUnit(per_foodItem.getString("unit"));
					tempFood.setCurrency(per_foodItem.getString("currency"));
					tempFood.setPickUpDateStart(per_foodItem
							.getString("pick_up_date_start"));
					tempFood.setPickUpDateEnd(per_foodItem
							.getString("pick_up_date_end"));
					tempFood.setUploadDate(per_foodItem
							.getString("update_timestamp"));

					foodBankItem.add(tempFood);
				}
			} else {

				// FoodBankItem tempFood = new FoodBankItem();
				// tempFood.setCategory("No result");
				// tempFood.setProducer("No result");
				// tempFood.setDescription("No result");
				// tempFood.setExpiryDate("No result");
				// tempFood.setFree(false);
				// tempFood.setPrice(0);
				// tempFood.setFoodName("No result");
				//
				// tempFood.setQuantity(0);
				// tempFood.setUnit("No result");
				// tempFood.setCurrency("No result");
				// tempFood.setPickUpDateStart("No result");
				// tempFood.setPickUpDateEnd("No result");
				// tempFood.setUploadDate("No result");
				//
				// foodBankItem.add(tempFood);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

			throw new CareFactorException();
		}

		runOnUiThread(returnRes);

	}

	private void errorOccuredInThread(){
		runOnUiThread(returnRes);
	}
	private Runnable returnRes = new Runnable() {

		// @Override
		public void run() {
			if (connectionErrorState) {
				cfDialog.PopIt(ConsumerSearchGroupActivity.group, "Connection Error!", "Cannot connect to CareFactor");
				m_ProgressDialog.dismiss();
				return;
			}
			if (foodBankItem != null && foodBankItem.size() > 0) {
				m_adapter.notifyDataSetChanged();
				m_adapter.clear();
				for (int i = 0; i < foodBankItem.size(); i++)
					m_adapter.add(foodBankItem.get(i));
				m_ProgressDialog.dismiss();
				
				
			} else {
				m_adapter.notifyDataSetChanged();
				m_adapter.clear();
				m_ProgressDialog.dismiss();

				Toast.makeText(ConsumerSearchActivity.this,
						"No result found! ", Toast.LENGTH_SHORT).show();

			}

			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
			
			searchSectionTitle.setText("Search Results ("+result_count+")");
			
		}
	};

	private class OrderAdapter extends ArrayAdapter<FoodBankItem> {

		private ArrayList<FoodBankItem> items;

		public OrderAdapter(Context context, int textViewResourceId,
				ArrayList<FoodBankItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		Random ratings = new Random();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.search_row, null);
			}
			FoodBankItem food = items.get(position);
			if (food != null) {
				TextView food_txt = (TextView) v
						.findViewById(R.id.row_foodname);
				TextView expiry_txt = (TextView) v
						.findViewById(R.id.row_expirydate);
				TextView producer_txt = (TextView) v
						.findViewById(R.id.row_producer);
				RatingBar rb = (RatingBar) v
						.findViewById(R.id.row_rating_producer);

				if (food_txt != null) {
					food_txt.setText("Name: " + food.getFoodName());// o.getOrderName()
				}
				if (expiry_txt != null) {
					expiry_txt.setText("Expiry date: " + food.getExpiryDate()); // o.getOrderStatus()
				}
				if (producer_txt != null) {
					producer_txt.setText(food.getProducer());
				}
				
				int rb_val = 0;
				if(food.getProducer().equals("demo-producer-1")) rb_val = 4;
				if(food.getProducer().equals("demo-producer-2")) rb_val = 1;
				if(food.getProducer().equals("demo-producer-3")) rb_val = 2;
				if(food.getProducer().equals("demo-producer-4")) rb_val = 0;
				if(food.getProducer().equals("demo-producer-5")) rb_val = 3;
				
				if (rb != null) {
					rb.setRating(rb_val);
				}
			}
			return v;
		}
	}

}