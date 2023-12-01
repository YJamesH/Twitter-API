package com.cooksys.socialmedia.customexceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException /*extends RuntimeException*/ {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7777165571573377274L;
	
	private String message;
	
}
