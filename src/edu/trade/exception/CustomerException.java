package edu.trade.exception;

public class CustomerException extends Exception{

	private static final long serialVersionUID = -3062451019551966094L;
	
	@SuppressWarnings("unused")
	private String message ;
	
	public CustomerException(String message){
		this.message =message; 
	}
	
}
