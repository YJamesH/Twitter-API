package com.cooksys.socialmedia.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	
	@Override
	public boolean validateHashtagExists(String label) {
		if (hashtagRepository.findByLabel(label).isEmpty()) {
			return false;
		}
		return true;
	}

}
