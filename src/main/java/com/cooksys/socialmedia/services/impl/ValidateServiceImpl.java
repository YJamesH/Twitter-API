package com.cooksys.socialmedia.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;
	
	@Override
	public boolean validateHashtagExists(String label) {
		if (hashtagRepository.findByLabel(label).isEmpty()) {
			return false;
		}
		return true;
  }
	
	@Override
	public boolean validateUsername(String username) {
		List<User> users = userRepository.findAll();
		
		for (User user : users) {
			if (user.getCredentials().getUsername().equals(username))
				return true;
		}
		return false;
	}

}
