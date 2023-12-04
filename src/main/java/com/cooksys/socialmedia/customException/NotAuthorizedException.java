package com.cooksys.socialmedia.customException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{
    private static long serialversionUID = 1L;
    private String message;
}
