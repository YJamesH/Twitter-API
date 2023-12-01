package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;

public interface TweetService {

	List<HashtagResponseDto> getHashtags(Long id);

	List<TweetResponseDto> getReposts(Long id);

	void likeTweet(Long id, UserRequestDto userRequestDto);

}
