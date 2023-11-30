package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	@Override
	public List<TweetResponseDto> getMentions(UserRequestDto currUser) {
		List<Tweet> allTweets = tweetRepository.findAll();
		ArrayList<TweetResponseDto> mentionedTweets = new ArrayList<TweetResponseDto>();
		
		for (Tweet tweet : allTweets) {
			String currContent = tweet.getContent();
			if (currContent.contains("@" + currUser.getCredentialsDto().getUsername())) {
				mentionedTweets.add(tweetMapper.entityToDto(tweet));
			}
		}
		
		return mentionedTweets;
	}
}
