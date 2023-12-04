package com.cooksys.socialmedia.controllers.advice;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.customException.NotAuthorizedException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class socialmediaAdvice {
    @RestControllerAdvice(basePackages = { "com.cooksys.socialmedia.controllers"})
    @ResponseBody
    public class ControllerAdvice {
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(BadRequestException.class)
        public ErrorDto handleBadRequestException (HttpServletRequest request, BadRequestException badRequestException) {
            return new ErrorDto(badRequestException.getMessage());
        }
        @ResponseStatus(HttpStatus.NOT_FOUND)
        @ExceptionHandler(NotFoundException.class)
        public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException notFoundException) {
            return new ErrorDto(notFoundException.getMessage());
        }
        @ExceptionHandler(NotAuthorizedException.class)
        public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotAuthorizedException notAuthorizedException) {
            return new ErrorDto(notAuthorizedException.getMessage());
        }
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(IllegalArgumentException.class)
        public ErrorDto handleIllegalArugment(IllegalArgumentException ex) {
              return new ErrorDto(ex.getMessage());
        }

    }
}
