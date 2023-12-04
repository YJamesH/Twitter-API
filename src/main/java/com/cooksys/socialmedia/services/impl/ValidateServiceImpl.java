package com.cooksys.socialmedia.services.impl;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;

    //*******************************	//GET validate/username/available/@{username}#85
    //Checks whether or not a given username is available.
    //Response 'boolean'
    @Override
    public boolean getAvailableUsername(String username) {
        if (username == null) {
            throw new BadRequestException("Username is NULL");
        }
        User user = userRepository.findByCredentialsUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }

}

