package com.cooksys.socialmedia.services;

<<<<<<< HEAD
import com.cooksys.socialmedia.customException.NotAuthorizedException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import java.util.List;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;


public interface TweetService {

    List<TweetResponseDto> getSortedTweets();

    List<UserResponseDto> getMentionedUsers(Long id);

    TweetResponseDto addRepostToTweet(Long id, CredentialsDto credentialsDto) throws NotAuthorizedException, NotFoundException;

    TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

    TweetResponseDto getTweetContextById(Long id);

	TweetResponseDto postTweet(TweetRequestDto tweetRequestDto) throws NotAuthorizedException;

	TweetResponseDto findTweetById(Long id) throws BadRequestException;

	TweetResponseDto postReply(TweetRequestDto tweetRequestDto, Long id) throws BadRequestException, NotAuthorizedException;

	List<TweetResponseDto> getAllTweets();

	List<UserResponseDto> getLikes(Long id) throws BadRequestException;

	List<TweetResponseDto> getReplies(Long id) throws BadRequestException;
  
	List<HashtagResponseDto> getHashtags(Long id);

	List<TweetResponseDto> getReposts(Long id);

	void likeTweet(Long id, UserRequestDto userRequestDto);
}
