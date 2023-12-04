package com.cooksys.socialmedia.customexceptions;

public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7777165571573377274L;

	public NotFoundException(String message) {
		super(message);
	}
	
}
