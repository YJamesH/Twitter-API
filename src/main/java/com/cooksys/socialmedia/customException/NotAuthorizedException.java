package com.cooksys.socialmedia.customException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 3063659356259871197L;
    private String message;
}
