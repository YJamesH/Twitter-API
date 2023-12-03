package com.cooksys.socialmedia.customexceptions;

public class NotAuthorizedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1480599881154056806L;
	
	public NotAuthorizedException(String message) {
		super(message);
	}
}
