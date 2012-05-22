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
import java.util.Arrays;

import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.user.UserInfoPersist;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ChoiceProducerDialog extends Activity {
	private ListView choiceProducerList;
	private UserInfoPersist userInfo;
	private ArrayList<String> selectedItems = new ArrayList<String>();
	private Button saveChoiceListBtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumer_sel_choice_producer);

		
		userInfo = ((UserInfoPersist) getApplicationContext());
		
		saveChoiceListBtn = (Button) findViewById(R.id.consumer_producer_save_choice);

		final String[] producers = new String[] { "demo-producer-1", "demo-producer-2", "demo-producer-3",
				"demo-producer-4", "demo-producer-5", "samup4web" };
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, producers);

		choiceProducerList = (ListView) findViewById(R.id.choice_producer);
		choiceProducerList.setAdapter(ad);
		choiceProducerList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		choiceProducerList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				// TODO Auto-generated method stub

			}
		});
		
		saveChoiceListBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ConsumerSettingsGroupActivity.group.back();
				SaveSelections();
			}
		});
		
		LoadSelections();

	}

	@Override
	protected void onPause() {

		// always handle the onPause to make sure selections are saved if user
		// clicks back button

		SaveSelections();

		super.onPause();

	}

	@Override
	protected void onResume() {

		// always handle the onPause to make sure selections are saved if user
		// clicks back button

		LoadSelections();

		super.onResume();

	}

	private void LoadSelections() {

		// if the selections were previously saved load them

		if (!userInfo.getProducerChoiceList().equals("")) {

			String savedItems = userInfo.getProducerChoiceList();

			this.selectedItems.addAll(Arrays.asList(savedItems.split(",")));

			int count = this.choiceProducerList.getAdapter().getCount();

			for (int i = 0; i < count; i++) {

				String currentItem = (String) this.choiceProducerList
						.getAdapter().getItem(i);

				if (this.selectedItems.contains(currentItem)) {

					this.choiceProducerList.setItemChecked(i, true);

				}

			}

		}

	}

	private void SaveSelections() {

		// save the selections in the shared preference in private mode for the
		// user

		String savedItems = getSavedItems();

		userInfo.setProducerChoiceList(savedItems);

	}

	private String getSavedItems() {

		String savedItems = "";

		int count = this.choiceProducerList.getAdapter().getCount();

		for (int i = 0; i < count; i++) {

			if (this.choiceProducerList.isItemChecked(i)) {

				if (savedItems.length() > 0) {

					savedItems += ","
							+ this.choiceProducerList.getItemAtPosition(i);

				} else {

					savedItems += this.choiceProducerList.getItemAtPosition(i);

				}

			}

		}

		return savedItems;

	}

}