package com.cooksys.socialmedia.customException;

import com.cooksys.socialmedia.dtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice()
//public class ControllerAdvice {
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorMessage> handlerNotFound(NotFoundException ex) {
//        ErrorMessage errorMessage = new ErrorMessage();
//        errorMessage.setErrorCode(HttpStatus.NOT_FOUND.value());
//        errorMessage.setMessage(ex.getMessage());
//        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
//    }
//    @ExceptionHandler(NotAuthorizedException.class)
//    public ResponseEntity<ErrorMessage> handleNotAuthorized(NotAuthorizedException ex) {
//        ErrorMessage errorMessage = new ErrorMessage();
//        errorMessage.setErrorCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
//        errorMessage.setMessage(ex.getMessage());
//        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorDto> handleIllegalArugment(IllegalArgumentException ex) {
//        ErrorDto dto = new ErrorDto(ex.getMessage());
//        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ErrorMessage> handleBadRequest(BadRequestException ex) {
//        ErrorMessage errorMessage = new ErrorMessage();
//        errorMessage.setErrorCode(HttpStatus.NOT_FOUND.value());
//        errorMessage.setMessage(ex.getMessage());
//        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
//    }
//
//}
