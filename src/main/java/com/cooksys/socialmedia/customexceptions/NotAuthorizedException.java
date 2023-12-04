package com.cooksys.socialmedia.customexceptions;

public class NotAuthorizedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1480599881154056806L;
	
	public NotAuthorizedException(String message) {
		super(message);
	}
}
