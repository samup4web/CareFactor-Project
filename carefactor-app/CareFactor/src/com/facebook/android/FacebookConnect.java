package com.facebook.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

import com.easy.facebook.android.apicall.GraphApi;
import com.easy.facebook.android.data.User;
import com.easy.facebook.android.error.EasyFacebookError;
import com.easy.facebook.android.facebook.FBLoginManager;
import com.easy.facebook.android.facebook.Facebook;
import com.easy.facebook.android.facebook.LoginListener;
import com.carefactor.sam.samup4web.R;
import com.carefactor.samup4web.generic.consumer.ConsumerFoodDetailActivity;
import com.carefactor.samup4web.generic.consumer.ConsumerSearchGroupActivity;
import com.carefactor.samup4web.user.UserInfoPersist;
import com.carefactor.samup4web.utils.CareFactorDialogs;

public class FacebookConnect extends Activity implements LoginListener {

	private FBLoginManager fbManager;
	Intent intentResult;
	String message;
	private UserInfoPersist userInfo;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shareFacebook();
		
		userInfo = ((UserInfoPersist) getApplicationContext());
	}

	
	
	
	public void shareFacebook() {

		String permissions[] = { "read_stream", "publish_stream",
				"offline_access" };

		// change the parameters with those of your application
		fbManager = new FBLoginManager(this, R.layout.black, "232183553535806",
				permissions);

		if (fbManager.existsSavedFacebook()) {
			fbManager.loadFacebook();
		} else {

			fbManager.login();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		fbManager.loginSuccess(data);
	}

	public void loginFail() {
		fbManager.displayToast("Login failed!");

	}

	public void logoutSuccess() {
		fbManager.displayToast("Logout success!");
		Log.d("CAREFACTOR", "LOGIN success");
	}

	public void loginSuccess(Facebook facebook) {

		// library use example
		GraphApi graphApi = new GraphApi(facebook);

		String response = "";
		
		User user = new User();
		try {
			user = graphApi.getMyAccountInfo();

			// update your status if logged in
			message = ""+user.getFirst_name()+" just discovered food items for free/cheap price at CareFactor."+ userInfo.getShare_message();
			response = graphApi.setStatus(message, "http://samuelidowu.com/" );
		} catch (EasyFacebookError e) {
			e.toString();
		}
				
	
		
		//String pkg=getPackageName(); 
		//intentResult.putExtra(pkg+".myMessage",response);
    	//startActivity(intentResult);
		
		//fbManager.displayToast("Hej, " + user.getFirst_name() + "! You have shared data via CareFactor!");
		
		
		
		new AlertDialog.Builder(this).setTitle( "Hi, "+user.getFirst_name()).setMessage(" You have successfully updated your facebook status via CareFactor!")
		.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
				arg0.cancel();
			}
		}).show();
		

		//finish();
	}

}