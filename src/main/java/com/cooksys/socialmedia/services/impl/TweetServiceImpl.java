package com.cooksys.socialmedia.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserRepository userRepository;
	private final HashtagMapper hashtagMapper;
	
	@Override
	public List<HashtagResponseDto> getHashtags(Long id) {
		return hashtagMapper.entitiesToHashtagDtos(getTweetWithId(id).getHashtags());
	}
	
	@Override
	public List<TweetResponseDto> getReposts(Long id) {
		return tweetMapper.entitiesToTweetDtos(getTweetWithId(id).getReposts());
	}

	@Override
	public void likeTweet(Long id, UserRequestDto userRequestDto) {
		Tweet tweet = getTweetWithId(id);
		User user = getUserAndCheckCredentials(userRequestDto);
		
		tweet.getUserLikes().add(user);
		user.getTweetLikes().add(tweet);
		
		tweetRepository.saveAndFlush(tweet);
		userRepository.saveAndFlush(user);
	}

	public Tweet getTweetWithId(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		if (optionalTweet.isEmpty()) {
//			throw new NotFoundException("Tweet not found");
		}
		Tweet tweet = optionalTweet.get();
		if (tweet.isDeleted()) {
//			throw new BadRequestException("Tweet deleted");
		}
		return tweet;
	}

	public User getUserAndCheckCredentials(UserRequestDto userRequestDto) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(userRequestDto.getCredentialsDto().getUsername());
		if (optionalUser.isEmpty()) {
//			throw new NotFoundException("User not found");
		} 
		User user = optionalUser.get();
		
		String userPassword = user.getCredentials().getPassword();
		String userInRepoPassword = userRequestDto.getCredentialsDto().getPassword();
		if (!userPassword.equals(userInRepoPassword)) {
//			throw new NotAuthorizedException("Password does not match");
		}
		if (user.isDeleted()) {
//			throw new BadRequestException("User deleted");
		}
		return user;
	}



}
