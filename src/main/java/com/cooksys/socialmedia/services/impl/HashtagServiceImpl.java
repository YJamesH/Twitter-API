package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	private final TweetRepository tweetRepository;

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
		Hashtag hashtag = getTag(label);
        if(hashtag == null){
            throw new IllegalArgumentException("Tweet id not found");
        }
        List<Tweet> tweets = tweetRepository.findAll();
		List<Tweet> toReturn = new ArrayList<>();
        for (Tweet tweet : tweets) {
    		String content = tweet.getContent();
    		if (content != null) {
    			if (content.contains("#" + label)) {
        			toReturn.add(tweet);
        		}
    		}
        }
        return tweetMapper.entitiesToResponseDtos(toReturn);
	}
	
	private Hashtag getTag(String label) {
		List<Hashtag> allTags = hashtagRepository.findAll();

		for (Hashtag tag : allTags) {
			if (tag.getLabel().equals(label))
				return tag;
		}
		// If code reaches this point, no tag with this label is found in the db so a
		// new one is created

		Hashtag myTag = new Hashtag();

		myTag.setLabel(label);

		return myTag;
	}

}
