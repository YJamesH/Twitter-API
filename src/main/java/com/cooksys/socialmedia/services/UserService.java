package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface UserService {
	
	List<UserResponseDto> getAllUsers();
	
	UserResponseDto getUser(String username);

	List<TweetResponseDto> getTweets(String username);

	List<UserResponseDto> getFollowing(String username);

	void unfollowUser(String username, CredentialsRequestDto credentialsRequestDto);

	UserResponseDto deleteUser(String username, CredentialsRequestDto credentialsRequestDto);

//	List<TweetResponseDto> getMentions(String username);

//	void follow(CredentialsRequestDto credentialsRequestDto, String username);





}
