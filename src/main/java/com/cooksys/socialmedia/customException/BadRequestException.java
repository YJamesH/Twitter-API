package com.cooksys.socialmedia.customException;

public class BadRequestException extends RuntimeException{
    private static long serialversionUID = 1L;

    public BadRequestException() {
        super();
    }
    public BadRequestException(String message)  {
        super(message);
    }
}
