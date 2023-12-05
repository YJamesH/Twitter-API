package com.cooksys.socialmedia.customException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7743396920323926186L;
	private String message;

}
