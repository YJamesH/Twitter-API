package com.cooksys.socialmedia.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	
	@Override
	public TweetResponseDto postTweet(TweetRequestDto tweetRequestDto) {
		Tweet currTweet = tweetMapper.dtoToEntity(tweetRequestDto);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(currTweet));
	}

	@Override
	public TweetResponseDto findTweetById(Long id) {
		Optional<Tweet> currTweet = tweetRepository.findById(id);
		
		if (currTweet.isPresent()) {
			Tweet myTweet = currTweet.get();
			
			return tweetMapper.entityToDto(myTweet);
		}
		// Implement exception here later
		return null;
	}

	@Override
	public TweetResponseDto postReply(TweetRequestDto tweetRequestDto) {
		Tweet currTweet = tweetMapper.dtoToEntity(tweetRequestDto);
		
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(currTweet));
	}

}
