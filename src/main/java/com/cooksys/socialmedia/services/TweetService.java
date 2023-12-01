package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface TweetService {

	TweetResponseDto postTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto findTweetById(Long id);

	TweetResponseDto postReply(TweetRequestDto tweetRequestDto, Long id);

	List<TweetResponseDto> getAllTweets();

	List<UserResponseDto> getLikes(Long id);

	List<TweetResponseDto> getReplies(Long id);

}
