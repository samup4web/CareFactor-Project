package com.carefactor.samup4web.utils;

public class CareFactorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String error;
	public Exception innerException;
	
	public CareFactorException(){
		
	}
	public CareFactorException(String message) {
		super(message);
		this.error = message;
	}
	public CareFactorException(Exception e) {
		this.innerException = e;
		
	}
	public String toString(){
		return error;
	}
}
