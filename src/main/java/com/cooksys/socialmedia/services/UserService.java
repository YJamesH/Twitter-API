package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.customexceptions.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface UserService {
	
	List<UserResponseDto> getAllUsers();
	
	UserResponseDto getUser(String username);

	List<TweetResponseDto> getTweets(String username);
	
	List<TweetResponseDto> getMentions(String username);

	List<UserResponseDto> getUsersWithActiveFollowers(String username);

	boolean getAvailableUsername(String username);

	UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto);

    List<TweetResponseDto> getTweetsByUsername(String username);
	
	List<UserResponseDto> getFollowing(String username);

	void unfollowUser(String username, CredentialsDto credentialsDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	void follow(CredentialsDto credentialsDto, String username) throws NotAuthorizedException, NotFoundException, BadRequestException;

	UserResponseDto createUser(UserRequestDto userRequestDto) throws BadRequestException, NotAuthorizedException;
}
