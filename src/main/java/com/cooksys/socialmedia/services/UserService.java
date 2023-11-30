package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;

public interface UserService {
	
	List<TweetResponseDto> getMentions(UserRequestDto currUser);
}
