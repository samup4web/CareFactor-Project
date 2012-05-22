package com.carefactor.samup4web.generic;

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
import com.carefactor.samup4web.user.UserAccountManager;
import com.carefactor.samup4web.utils.CareFactorDialogs;
import com.carefactor.samup4web.utils.Validator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUpActivity extends Activity implements Runnable {
	/** Called when the activity is first created. */

	private Button btn_signup;
	private EditText txt_username;
	private EditText txt_email;
	private EditText txt_password;
	private EditText txt_password_2;
	private Spinner spnr_user_type;
	private ProgressDialog pd;
	protected String resp;
	protected String username;
	protected String email;
	protected String user_type;
	protected String password;
	protected String password_2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		btn_signup = (Button) findViewById(R.id.signup_button);
		txt_username = (EditText) findViewById(R.id.txt_signup_username);
		txt_email = (EditText) findViewById(R.id.txt_signup_email);
		txt_password = (EditText) findViewById(R.id.txt_signup_password);
		txt_password_2 = (EditText) findViewById(R.id.txt_signup_password_2);
		spnr_user_type = (Spinner) findViewById(R.id.txt_signup_user_type);

		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Connecting to CareFactor service...");
		pd.setIndeterminate(true);
		pd.setCancelable(true);

		// Perfect styling

		ArrayAdapter<CharSequence> userTypeSpnr_adapter = ArrayAdapter
				.createFromResource(this, R.array.usertypelist,
						R.layout.spinner_layout);
		userTypeSpnr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnr_user_type.setAdapter(userTypeSpnr_adapter);

		btn_signup.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				username = txt_username.getText().toString().trim();
				email = txt_email.getText().toString().trim();
				user_type = spnr_user_type.getSelectedItem().toString().trim();
				password = txt_password.getText().toString().trim();
				password_2 = txt_password_2.getText().toString().trim();

				Validator validator = new Validator();
				boolean valid_flag = true;

				StringBuilder ErrorMessage = new StringBuilder();
				if (!validator.checkEmail(email)) {
					valid_flag = false;
					ErrorMessage.append("* Invalid email address \n");
				}
				if (!validator.checkUsername(username)) {
					valid_flag = false;
					ErrorMessage
							.append("* username must be at least lenght of 5 \n");
				}
				if (!validator.checkUserType(user_type)) {
					valid_flag = false;
					ErrorMessage.append("* select valid user type \n");
				}
				if (!validator.checkPassword(password, password_2)) {
					valid_flag = false;

					ErrorMessage.append("* passwords are not same\n");
				}
				if (!validator.checkPasswordLen(password)) {
					valid_flag = false;
					ErrorMessage
							.append("* password must be at least of lenght 5 \n");

				}

				if (valid_flag) {

					pd.show();

					Thread thread = new Thread(SignUpActivity.this);
					thread.start();

				} else {

					Toast.makeText(SignUpActivity.this, ErrorMessage,
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	public void run() {
		// TODO Auto-generated method stub
		UserAccountManager userAccMan = new UserAccountManager();
		resp = userAccMan.userSignUp(email, username, user_type, password_2);

		handler.sendEmptyMessage(0);

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			
			if (resp.equals("success")) {

				new AlertDialog.Builder(SignUpActivity.this)
						.setTitle("Congrats!")
						.setMessage("Registration successful.")
						.setNeutralButton("Sign In",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										switchToLoginActivity();
									}
								}).show();

			}
			if (resp.trim().equals("")) {
				new CareFactorDialogs().PopIt(SignUpActivity.this, "Connection Error!", "Something went wrong.");
			} else {
				Toast.makeText(SignUpActivity.this, resp, Toast.LENGTH_LONG)
						.show();
			}

		}
	};

	public void switchToLoginActivity() {
		finish();
		Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.loginmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login_menu_login:
			//Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();
			switchToLoginActivity();
			break;
		case R.id.login_menu_signup:
			//Toast.makeText(this, "Sign Up", Toast.LENGTH_SHORT).show();

			break;
		case R.id.login_menu_about:
			Toast.makeText(this, "About CareFactor ", Toast.LENGTH_SHORT)
					.show();
			break;
		}
		return true;
	}
}