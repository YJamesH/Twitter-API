package com.cooksys.socialmedia.customException;

import com.cooksys.socialmedia.dtos.ErrorDto;

public class NotFoundException extends RuntimeException{
    private static long serialversionUID = 1L;

    public NotFoundException() {
        super();
    }
    public NotFoundException(String message)  {
        super(message);
    }
}
