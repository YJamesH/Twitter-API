package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserRepository userRepository;
	
	@Override
	public List<TweetResponseDto> getMentions(String username) {
		List<Tweet> allTweets = tweetRepository.findAll();
		ArrayList<TweetResponseDto> mentionedTweets = new ArrayList<TweetResponseDto>();
		
		for (Tweet tweet : allTweets) {
			String currContent = tweet.getContent();
			if (currContent.contains("@" + username)) {
				mentionedTweets.add(tweetMapper.entityToDto(tweet));
			}
		}
		
		return mentionedTweets;
	}

	@Override
	public void follow(CredentialsRequestDto credentialsRequestDto, String username) {
		List<User> users = userRepository.findAll();
		User myUser = new User();
		boolean myUserSet = false;
		
		User userToFollow = new User();
		boolean userToFollowSet = false;
		
		// Populates the myUser and userToFollow with correct values.
		for (User user : users) {
			if (credentialsRequestDto.getUsername().equals(user.getCredentials().getUsername())) {
				myUser = user;
				myUserSet = true;
			}
			
			if (username.equals(user.getCredentials().getUsername())) {
				userToFollow = user;
				userToFollowSet = true;
			}
		}
		
		// If either variable is not set, there are no usernames with the specified value and will thorw an exception.
		if (!myUserSet) {
			// throw some excpetion here
		}
		
		if (!userToFollowSet) {
			// throw some exception here
		}
		
		List<User> myUserCurrFollowing = myUser.getFollowing();
		List<User> userToFollowCurrFollowers = userToFollow.getFollowers();
		
		myUserCurrFollowing.add(userToFollow);
		userToFollowCurrFollowers.add(myUser);
		
		myUser.setFollowing(myUserCurrFollowing);
		userToFollow.setFollowers(userToFollowCurrFollowers);
		
		userRepository.saveAndFlush(myUser);
		userRepository.saveAndFlush(userToFollow);
	}
}
