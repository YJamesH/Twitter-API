package com.cooksys.socialmedia.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	private final TweetMapper tweetMapper;
	
	@Override
	public List<HashtagResponseDto> getAllTags() {
		return hashtagMapper.entitiesToHashtagDtos(hashtagRepository.findAll());
	}
	//*****************************GET tags/{label}#101
		//Retrieves all (non-deleted) tweets tagged with the given hashtag label.
		// The tweets should appear in reverse-chronological order.
		// If no hashtag with the given label exists, an error should be sent in lieu of a response.
		//A tweet is considered "tagged" by a hashtag if the tweet has content and the hashtag's label appears in that content following a #
		//Response ['Tweet']

	@Override
	public List<TweetResponseDto> getTweetsByLabel(String label) {
		if( label ==  null) {
			throw new IllegalArgumentException("label is null");
		}

		//validating hashtag with given label
		Hashtag hashtag = hashtagRepository.findHashtagByLabel(label);

		if( hashtag == null ) {
			throw new NotFoundException("Not exist");
		}

		List<Tweet> tweets = hashtag.getTweets();

		//validating tagged ,undeleted and in reverse-chronological order
		List<Tweet> taggedTweets = tweets.stream().filter(t->t.getContent().contains(label) && !t.isDeleted()).sorted(Comparator.comparing(Tweet::getId).reversed()).collect(Collectors.toList());
		return tweetMapper.entitiesToResponseDtos(taggedTweets);
	}

}
