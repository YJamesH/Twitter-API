package com.cooksys.socialmedia.customException;

import com.cooksys.socialmedia.dtos.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4168501462692829314L;
    private String message;

}
