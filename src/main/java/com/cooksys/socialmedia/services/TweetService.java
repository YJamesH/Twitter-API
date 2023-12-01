package com.cooksys.socialmedia.services;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;

public interface TweetService {

	TweetResponseDto postTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto findTweetById(Long id);

	TweetResponseDto postReply(TweetRequestDto tweetRequestDto);

}
