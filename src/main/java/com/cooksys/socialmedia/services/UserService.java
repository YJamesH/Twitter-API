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
	
	List<TweetResponseDto> getMentions(String username);

	void follow(CredentialsDto credentialsDto, String username) throws NotAuthorizedException, NotFoundException, BadRequestException;

	UserResponseDto createUser(UserRequestDto userRequestDto) throws BadRequestException, NotAuthorizedException;
}
