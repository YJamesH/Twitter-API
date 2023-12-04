package com.cooksys.socialmedia.customException;

import com.cooksys.socialmedia.dtos.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException{
    private static long serialversionUID = 1L;
    private String message;

}
