package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface TweetService {

	TweetResponseDto postTweet(TweetRequestDto tweetRequestDto) throws NotAuthorizedException;

	TweetResponseDto findTweetById(Long id) throws BadRequestException;

	TweetResponseDto postReply(TweetRequestDto tweetRequestDto, Long id) throws BadRequestException, NotAuthorizedException;

	List<TweetResponseDto> getAllTweets();

	List<UserResponseDto> getLikes(Long id) throws BadRequestException;

	List<TweetResponseDto> getReplies(Long id) throws BadRequestException;

	void likeTweet(Long id, UserRequestDto userRequestDto);

}
