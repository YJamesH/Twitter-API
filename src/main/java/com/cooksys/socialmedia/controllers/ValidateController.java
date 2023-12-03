package com.cooksys.socialmedia.controllers;

import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Boolean> getAvailableUsername(@PathVariable("username") String username) {
        if (username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean available = validateService.getAvailableUsername(username);
        return new ResponseEntity<>(available, HttpStatus.OK);
    }

}
