package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
	
	List<TweetResponseDto> getMentions(String username);

	void follow(CredentialsRequestDto credentialsRequestDto, String username);

	List<UserResponseDto> getUsersWithActiveFollowers(String username);

	boolean getAvailableUsername(String username);


	UserResponseDto updateUserProfile(String username, CredentialsRequestDto credentialsRequestDto);

    List<TweetResponseDto> getTweetsByUsername(String username);
}
