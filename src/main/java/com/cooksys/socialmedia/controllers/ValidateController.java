package com.cooksys.socialmedia.controllers;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
    private final ValidateService validateService;


    //******************************************
    //******************************	//GET validate/username/available/@{username}#85
    //Response 'boolean'
    @GetMapping("/available/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean getAvailableUsername(@PathVariable("username") String username) {
        if (username == null) {
            throw new BadRequestException("Username is null");
        }
        boolean available = validateService.getAvailableUsername(username);
        return available;
    }
	
	@GetMapping("/tag/exists/{label}")
	public boolean validateHashtagExists(@PathVariable(value = "label") String label) {
		return validateService.validateHashtagExists(label);
  }
  
	@GetMapping("/username/exists/@{username}")
	public boolean validateUsername(@PathVariable(value="username") String username) {
		return validateService.validateUsername(username);
	}
	
}
