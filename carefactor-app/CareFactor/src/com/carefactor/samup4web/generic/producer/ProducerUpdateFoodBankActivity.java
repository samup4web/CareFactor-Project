package com.carefactor.samup4web.generic.producer;

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
import java.lang.Character.UnicodeBlock;
import java.util.Calendar;

import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.generic.LoginActivity;
import com.carefactor.samup4web.user.UserAccountManager;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorDialogs;
import com.carefactor.samup4web.utils.Validator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProducerUpdateFoodBankActivity extends Activity implements
		Runnable {

	private int mYear;
	private int mMonth;
	private int mDay;

	private enum DateSwitch {
		ALL, EXPIRY, AVAILABLE_1, AVAILABLE_2
	};

	private DateSwitch currentSwitch = DateSwitch.ALL;

	private UserInfoPersist userInfo;

	private Spinner foodCategory;
	private EditText foodName;
	private EditText foodDescription;
	private EditText expiryDate; // expiryDate
	private EditText availDateDisplay1;
	private EditText availDateDisplay2;
	private RadioButton radio_free;
	private RadioButton radio_offer;
	private EditText price;
	private Spinner currency;
	private Button submitBtn;
	private boolean priceIsFree_flag = true;
	
	private EditText quantity;
	private int addToFoodBankResponseCode;
	private ScrollView mainScrollView;
	private Spinner qtyWeightUnit;
	protected CareFactorDialogs cfDialog = new CareFactorDialogs();
	private ProgressDialog currentDialog;

	static final int DATE_DIALOG_ID = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.producer_update_resource);

		userInfo = ((UserInfoPersist) getApplicationContext());

		Button btnSelectDate = (Button) findViewById(R.id.btn_new_change_date);
		Button btnselectAvailableDate1 = (Button) findViewById(R.id.btn_new_available_date_1);
		Button btnselectAvailableDate2 = (Button) findViewById(R.id.btn_new_available_date_2);
		expiryDate = (EditText) findViewById(R.id.txt_new_expiry_date);

		submitBtn = (Button) findViewById(R.id.btn_submit);

		availDateDisplay1 = (EditText) findViewById(R.id.txt_new_available_date_1);
		availDateDisplay2 = (EditText) findViewById(R.id.txt_new_available_date_2);

		foodName = (EditText) findViewById(R.id.txt_new_product_name);
		foodDescription = (EditText) findViewById(R.id.txt_new_description);
		foodCategory = (Spinner) findViewById(R.id.txt_new_category);
		price = (EditText) findViewById(R.id.txt_price);
		currency = (Spinner) findViewById(R.id.spinner_currency);
		quantity = (EditText) findViewById(R.id.txt_new_quantity);
		qtyWeightUnit = (Spinner) findViewById(R.id.spinner_unit);

		mainScrollView = (ScrollView) findViewById(R.id.updateFoodBankScrollView);

		expiryDate.setKeyListener(null);
		availDateDisplay1.setKeyListener(null);
		availDateDisplay2.setKeyListener(null);

		currentSwitch = DateSwitch.ALL;

		radio_free = (RadioButton) findViewById(R.id.radio_free);
		radio_offer = (RadioButton) findViewById(R.id.radio_offer);
		radio_free.setOnClickListener(radio_listener);
		radio_offer.setOnClickListener(radio_listener);

		price = (EditText) findViewById(R.id.txt_price);
		currency = (Spinner) findViewById(R.id.spinner_currency);

		price.setEnabled(false);
		currency.setEnabled(false);

		// Perfect styling

		ArrayAdapter<CharSequence> foodCategory_adapter = ArrayAdapter
				.createFromResource(this, R.array.food_category,
						R.layout.spinner_layout);
		foodCategory_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		foodCategory.setAdapter(foodCategory_adapter);

		// Perfect styling

		ArrayAdapter<CharSequence> currency_adapter = ArrayAdapter
				.createFromResource(this, R.array.currencies,
						R.layout.spinner_layout);
		currency_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currency.setAdapter(currency_adapter);

		// Perfect styling

		ArrayAdapter<CharSequence> qtyWeightUnit_adapter = ArrayAdapter
				.createFromResource(this, R.array.qty_weight_units,
						R.layout.spinner_layout);
		qtyWeightUnit_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		qtyWeightUnit.setAdapter(qtyWeightUnit_adapter);

		
		////////////////
		
		btnSelectDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
				currentSwitch = DateSwitch.EXPIRY;
			}
		});

		btnselectAvailableDate1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
				currentSwitch = DateSwitch.AVAILABLE_1;
			}
		});

		btnselectAvailableDate2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
				currentSwitch = DateSwitch.AVAILABLE_2;
			}
		});

//		pd = new ProgressDialog(this);
//		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		pd.setMessage("Connecting to CareFactor service...");
//		pd.setIndeterminate(true);
//		pd.setCancelable(true);
		/**
		 * Submit details to food bank....
		 */
		submitBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// hide keyboard..
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						mainScrollView.getApplicationWindowToken(), 0);

				Validator validator = new Validator();
				boolean valid_flag = true;

				StringBuilder ErrorMessage = new StringBuilder();
				if (!validator.checkNonEmptyField(foodName.getText().toString()
						.trim())) {
					valid_flag = false;
					ErrorMessage
							.append("* Food product cannot be empty field \n");
				}
				if (!validator.checkNonEmptyField(foodDescription.getText()
						.toString().trim())) {
					valid_flag = false;
					ErrorMessage
							.append("* Food Description cannot be empty field \n");
				}
				if (!validator.checkQuantity(Integer.parseInt(quantity
						.getText().toString().trim()))) {
					valid_flag = false;
					ErrorMessage.append("* Quantity must be at least 1 \n");
				}
				if (!validator.checkPrice(Double.parseDouble(price.getText()
						.toString().trim()))
						&& !priceIsFree_flag) {
					valid_flag = false;
					ErrorMessage
							.append("* Specify valid price or select 'free' option \n");
				}
				if (valid_flag) {
					//pd.show();
					currentDialog = cfDialog.showBusy(ProducerUpdateFoodBankActivity.this, "Sending food details to CareFactor");
					Thread thread = new Thread(
							ProducerUpdateFoodBankActivity.this);
					thread.start();
				} else {
					Toast.makeText(ProducerUpdateFoodBankActivity.this,
							ErrorMessage, Toast.LENGTH_LONG).show();
				}

			}
		});

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateTimeDisplay(DateSwitch.ALL);
	}

	public void run() {
		// TODO Auto-generated method stub

		addToFoodBankResponseCode = new FoodBankService().submitToFoodBank(
				foodCategory.getSelectedItem().toString(), foodName.getText()
						.toString().trim(), foodDescription.getText()
						.toString().trim(), expiryDate.getText().toString(),
				availDateDisplay1.getText().toString(), availDateDisplay2
						.getText().toString(), priceIsFree_flag,
				(double) new Double(price.getText().toString().trim()),
				currency.getSelectedItem().toString(), Integer
						.parseInt(quantity.getText().toString().trim()),
				userInfo.getUsername(), userInfo.getUserId(), qtyWeightUnit.getSelectedItem()
						.toString().trim());

		handler.sendEmptyMessage(0);

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			cfDialog.endBusy(ProducerUpdateFoodBankActivity.this, currentDialog);
			
			cfDialog.PopIt(ProducerUpdateFoodBankActivity.this,
					"Food bank updated! ", "Thanks for sharing.");

			if (addToFoodBankResponseCode == 200) {
				// success
//				Toast.makeText(ProducerUpdateFoodBankActivity.this,
//						"Food bank updated! Thanks for sharing!",
//						Toast.LENGTH_SHORT).show();
				clearProducerUpdateFields();
			} else {
				// fail to submit
				Toast.makeText(ProducerUpdateFoodBankActivity.this,
						":( Error submitting data. Try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View v) {
			// Perform action on clicks
			RadioButton rb = (RadioButton) v;
			String radio_value = (String) rb.getText();
			if (radio_value.equals("Free")) {
				// Toast.makeText(ProducerActivity.this, "free",
				// Toast.LENGTH_SHORT).show();
				priceIsFree_flag = true;
				price.setEnabled(false);
				currency.setEnabled(false);
			} else {
				// Toast.makeText(ProducerActivity.this, "offer",
				// Toast.LENGTH_SHORT).show();
				priceIsFree_flag = false;
				price.setEnabled(true);
				currency.setEnabled(true);
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	protected void clearProducerUpdateFields() {

		// TODO Auto-generated method stub

		foodCategory.setSelection(0);
		foodName.setText("");
		foodDescription.setText("");

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateTimeDisplay(DateSwitch.ALL);

		quantity.setText("1");
		price.setText("0");

		mainScrollView.fullScroll(ScrollView.FOCUS_UP);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateTimeDisplay(currentSwitch);
		}
	};

	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private void updateTimeDisplay(DateSwitch switcher) {
		StringBuilder selDate = new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(mMonth + 1).append("-")
				.append(mDay).append(" ");
		switch (switcher) {
		case ALL:
			expiryDate.setText(selDate);
			availDateDisplay1.setText(selDate);
			availDateDisplay2.setText(selDate);
			break;
		case EXPIRY:
			expiryDate.setText(selDate);
			break;

		case AVAILABLE_1:
			availDateDisplay1.setText(selDate);
			break;
		case AVAILABLE_2:
			availDateDisplay2.setText(selDate);
			break;
		default:

			break;
		}

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.producermenu, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.producer_menu_logout:
	// // Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();
	// logOut();
	// // Toast.makeText(this, new
	// // Boolean(userInfo.getUserIsLoggedIn()).toString(),
	// // Toast.LENGTH_SHORT).show();
	// break;
	// case R.id.login_menu_signup:
	// Toast.makeText(this, "Sign Up", Toast.LENGTH_SHORT).show();
	// break;
	// case R.id.login_menu_about:
	// Toast.makeText(this, "About CareFactor", Toast.LENGTH_SHORT).show();
	// break;
	// }
	// return true;
	// }
	//
	// public void logOut(){
	// userInfo.clearPreference();
	// Intent i = new Intent(getApplicationContext(), LoginActivity.class);
	// startActivity(i);
	// finish();
	// }

}