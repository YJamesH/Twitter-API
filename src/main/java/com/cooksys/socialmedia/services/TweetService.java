package com.cooksys.socialmedia.services;

import com.cooksys.socialmedia.customException.NotAuthorizedException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;

import java.util.List;

public interface TweetService {

    List<TweetResponseDto> getSortedTweets();

    List<UserResponseDto> getMenstionedUsers(Long id);

    TweetResponseDto addRepostToTweet(Long id, CredentialsRequestDto credentialsRequestDto) throws NotAuthorizedException, NotFoundException;

    TweetResponseDto deleteTweetById(Long id, CredentialsRequestDto credentialsRequestDto);

    TweetResponseDto getTweetContextById(Long id);
}
