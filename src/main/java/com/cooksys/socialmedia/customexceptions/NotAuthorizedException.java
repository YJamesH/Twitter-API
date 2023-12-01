package com.cooksys.socialmedia.customexceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException /*extends RuntimeException*/ {/**
	 * 
	 */
	private static final long serialVersionUID = 1480599881154056806L;

	private String message;
	
}
