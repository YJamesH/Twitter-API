package com.cooksys.socialmedia.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.repositories.HashtagRepository;
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

	// ******************************* //GET
	// validate/username/available/@{username}#85
	// Checks whether or not a given username is available.
	// Response 'boolean'
	@Override
	public boolean getAvailableUsername(String username) {
		if (username == null) {
			throw new BadRequestException("Username is NULL");
		}
		Optional<User> myUser = userRepository.findByCredentialsUsername(username);
		return myUser.isEmpty();
	}

}
