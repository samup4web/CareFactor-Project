package com.carefactor.samup4web.utils;

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
import java.util.regex.Pattern;

public class Validator {

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public boolean checkUsername(String username) {
		return (username.length() > 4);
	}

	public boolean checkUserType(String user_type) {
		return (user_type.equals("producer") || user_type.equals("consumer"));
	}

	public boolean checkPassword(String password, String password_2) {
		return (password.equals(password_2));
	}

	public boolean checkPasswordLen(String password) {
		return (password.length() > 4);
	}
	
	public boolean checkNonEmptyField(String field){
		return (field.length()>0);
	}
	public boolean checkPrice(double field){
		return(field > 0);
	}
	public boolean checkQuantity(int field){
		return(field > 0);
	}

}
