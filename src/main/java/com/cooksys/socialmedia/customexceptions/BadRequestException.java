package com.cooksys.socialmedia.customexceptions;

public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9114432327541717042L;
	
	public BadRequestException(String message) {
		super(message);
	}

}
