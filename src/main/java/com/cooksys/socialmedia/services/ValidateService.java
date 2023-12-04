package com.cooksys.socialmedia.services;

public interface ValidateService {
	boolean validateHashtagExists(String label);
	boolean validateUsername(String username);
}
