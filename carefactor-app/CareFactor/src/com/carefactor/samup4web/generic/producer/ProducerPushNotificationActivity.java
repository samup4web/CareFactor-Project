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
import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.cf.FoodBankService;
import com.carefactor.samup4web.generic.SignUpActivity;
import com.carefactor.samup4web.utils.CareFactorDialogs;
import com.carefactor.samup4web.utils.Validator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ProducerPushNotificationActivity extends Activity {
    private EditText advertTitleText;
	private EditText advertBodyText;
	private RadioButton radioWithinRegion;
	private RadioButton radioWithinCountry;
	private Button sendAdvertBtn;
	private Button clearAdvertBtn;

	private CareFactorDialogs cfDialog = new CareFactorDialogs();; 
	private ProgressDialog currentDialog;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_advertise);
        
        advertTitleText = (EditText) findViewById(R.id.txt_advert_title);
        advertBodyText = (EditText) findViewById(R.id.txt_advert_body);
        radioWithinRegion = (RadioButton) findViewById(R.id.radio_region);
        radioWithinCountry = (RadioButton) findViewById(R.id.radio_country);
        
        sendAdvertBtn = (Button) findViewById(R.id.btn_producer_send_advert);
        clearAdvertBtn = (Button) findViewById(R.id.btn_producer_clear_advert);
        
        sendAdvertBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String title = advertTitleText.getText().toString();
				String body = advertBodyText.getText().toString();
				String recipient = "region";
				
				StringBuilder errorMsg = new StringBuilder();
				
				boolean valid = true;
				
				if(!new Validator().checkNonEmptyField(title)){
					valid = false;
					errorMsg.append("* Error: Empty title field \n");
				}
				if(!new Validator().checkNonEmptyField(body)){
					valid = false;
					errorMsg.append("* Error: Empty body field \n");
				}
				
				if(radioWithinCountry.isChecked()){
					recipient = "country";
				}else{
					recipient = "region";
				}
				
				if(valid){
					//send to server...
					
					
					currentDialog = cfDialog.showBusy(ProducerPushNotificationActivity.this, "Sending advert to target consumers");
					
					new FoodBankService().sendAdvertToServer(ProducerPushNotificationActivity.this, title, body, recipient, new SendAdvertHandler() );
					
					
					
				}else{
					//notify
					Toast.makeText(ProducerPushNotificationActivity.this, errorMsg.toString(),
							Toast.LENGTH_LONG).show();
				}

			}

		});
        
        clearAdvertBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				advertBodyText.setText("");
				advertTitleText.setText("");
				radioWithinCountry.setChecked(false);
				radioWithinRegion.setChecked(true);
			}

		});

       
    }
    
    private class SendAdvertHandler extends Handler {
		@Override
		public void handleMessage(Message message) {

			cfDialog.endBusy(ProducerPushNotificationActivity.this, currentDialog);
			cfDialog.PopIt(ProducerPushNotificationActivity.this,
					"CareFactor Advert ", "Advert sent!");
			
			advertBodyText.setText("");
			advertTitleText.setText("");
			radioWithinCountry.setChecked(false);
			radioWithinRegion.setChecked(true);
			
			
			Bundle bundle = message.getData();

			String response = "";

			response = bundle.getString("response");

			//Log.d("CAREFACTOR", "advert sent...!" + response);

		}
	}
}