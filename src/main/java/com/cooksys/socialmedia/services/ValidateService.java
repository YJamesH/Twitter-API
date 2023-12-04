package com.cooksys.socialmedia.services;

public interface ValidateService {
    boolean getAvailableUsername(String username);
	boolean validateHashtagExists(String label);
	boolean validateUsername(String username);
}
