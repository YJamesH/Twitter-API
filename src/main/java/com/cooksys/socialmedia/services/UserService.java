package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface UserService {
	
	List<TweetResponseDto> getMentions(String username);

	void follow(CredentialsRequestDto credentialsRequestDto, String username);

	UserResponseDto createUser(UserRequestDto userRequestDto);
}
