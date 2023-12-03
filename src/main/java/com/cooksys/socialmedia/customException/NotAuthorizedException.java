package com.cooksys.socialmedia.customException;

public class NotAuthorizedException extends RuntimeException{
    private static long serialversionUID = 1L;
    public NotAuthorizedException() {
        super();
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
