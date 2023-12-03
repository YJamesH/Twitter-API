package com.cooksys.socialmedia.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.customException.NotAuthorizedException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.*;
import com.cooksys.socialmedia.mappers.CredentialsMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final CredentialsMapper credentialsMapper;


	@Override
	public List<TweetResponseDto> getMentions(String username) {
		if (username == null) throw new IllegalArgumentException("Username is NULL");

		List<Tweet> allTweets = tweetRepository.findAll();
		List<TweetResponseDto> mentionedTweets = new ArrayList<TweetResponseDto>();
		if (allTweets != null && !allTweets.isEmpty()) {
			for (Tweet tweet : allTweets) {
				String currContent = tweet.getContent();
				if (currContent.contains("@" + username)) {
					mentionedTweets.add(tweetMapper.entityToDto(tweet));
				}
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
	//**********************************
	//***************************************//GET users/@{username}/followers#104
	//Retrieves the followers of the user with the given username. Only active users should be included in the response.
	// If no active user with the given username exists, an error should be sent in lieu of a response.
	@Override
	public List<UserResponseDto> getUsersWithActiveFollowers(String username)  {
		// validate exist or not using derived method
		User user = userRepository.findByCredentialsUsername(username);
		if( user == null){
			throw new NotFoundException("User not found");
		}
		// validate active or not
		if(user.isDeleted()){
			throw new BadRequestException("User not Active");
		}
		//List<User> followers = user.getFollowers();
		//followers.stream().filter(f-> !f.isDeleted()).collect(Collectors.toList());
		return userMapper.entitiesToResponseDtos(user.getFollowers());
	}

//	//*******************************
//	//GET validate/username/available/@{username}#85
//	//Checks whether or not a given username is available.
//	//Response 'boolean'
	@Override
	public boolean getAvailableUsername(String username) {
		if (username == null) {
			throw new IllegalArgumentException("Username is NULL");
		}
		User user = userRepository.findByCredentialsUsername(username);
		if(user == null) {
			return true;
		}
		return false;
	}

//************************************	//PATCH users/@{username}#81
//Updates the profile of a user with the given username.
// If no such user exists, the user is deleted, or the provided credentials do not match the user, an error should be sent in lieu of a response.
// In the case of a successful update, the returned user should contain the updated data.
	//Request
	// {
	//  credentials: 'CredentialsDto',
	//  profile: 'ProfileDto'
	//}
	//response 'User'

	@Override
	public UserResponseDto updateUserProfile(String username, CredentialsRequestDto credentialsRequestDto) {
		User user = userRepository.findByCredentialsUsername(username);

		if(user== null){
			throw new NotFoundException("user not found");
		}
		// validate active or not
		if(user.isDeleted()){
			throw new BadRequestException("User not Active");
		}
		// validate credentials
		if (!user.getCredentials().getUsername().equals(credentialsRequestDto.getUsername())) {
			throw new NotAuthorizedException("Credentials not matching");
		}
		user.setCredentials(credentialsMapper.requestDtoToEntity(credentialsRequestDto));
		userRepository.saveAndFlush(user);
		return userMapper.entityToDto(user);
	}

	//***************************
	////******************GET users/@{username}/feed#107
	//	//Retrieves all (non-deleted) tweets authored by the user with the given username,
	//	// as well as all (non-deleted) tweets authored by users the given user is following.
	//	// This includes simple tweets, reposts, and replies.
	//	// The tweets should appear in reverse-chronological order.
	//	// If no active user with that username exists (deleted or never created), an error should be sent in lieu of a response.

	@Override
	public List<TweetResponseDto> getTweetsByUsername(String username) {
		User user = userRepository.findByCredentialsUsername(username);
		//if user == null or user isDeleted throw exp
		if(user == null || user.isDeleted()){
			throw new NotFoundException("user not found  or deleted");
		}
		//Get all nonDeleted tweets
		List<Tweet> userTweets  = user.getTweets().stream().filter(tweet -> !tweet.isDeleted()).toList();

		List<List<Tweet>> allTweets = new ArrayList<>();
		List<User> following = user.getFollowing();
		for(User f: following) {
			allTweets.add(f.getTweets().stream().flatMap(t->t.getReplies().stream()).collect(Collectors.toList()));
			allTweets.add(f.getTweets().stream().flatMap(t->t.getReposts().stream()).collect(Collectors.toList()));
			allTweets.add(f.getTweets());
		}
		allTweets.add(userTweets);
		List<Tweet> userNonDeleted = allTweets.stream()
				.flatMap(List::stream).filter(tweet -> !tweet.isDeleted()).sorted(Comparator.comparing(Tweet::getId).reversed())
				.collect(Collectors.toList());

		return tweetMapper.entitiesToResponseDtos(userNonDeleted);
	}
}
