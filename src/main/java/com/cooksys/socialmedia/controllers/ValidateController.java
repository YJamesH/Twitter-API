package com.cooksys.socialmedia.controllers;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
