package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.customexceptions.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.ProfileDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Credentials;
import com.cooksys.socialmedia.entities.Profile;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.CredentialsMapper;
import com.cooksys.socialmedia.mappers.ProfileMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
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
	private final UserMapper userMapper;
	private final CredentialsMapper credentialsMapper;
	private final ProfileMapper profileMapper;
	
	
	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) throws BadRequestException, NotAuthorizedException {
		User newUser = new User();
		List<User> currUsers = userRepository.findAll();
		
		CredentialsDto currUserCreds = userRequestDto.getCredentials();
		ProfileDto currUserProfile = userRequestDto.getProfile();
		
		
		for (User user : currUsers) {
			if (user.getCredentials().getUsername().equals(currUserCreds.getUsername())) {
				if (user.isDeleted() && credentialsMapper.dtoToEntity(currUserCreds).equals(user.getCredentials())) {
					user.setDeleted(false);
					return userMapper.entityToDto(userRepository.saveAndFlush(user));
				} else if (user.isDeleted()) {
					// Only reaches this point if the credentials for the already created and deleted user are incorrect
					throw new NotAuthorizedException("Record of deleted user already exists in database, incorrect credentials provided");
				}
				throw new BadRequestException("Username already exists in DB.");
			}
		}
		
		
		System.out.println(currUserCreds.getUsername());
		
		Credentials creds = credentialsMapper.dtoToEntity(currUserCreds);
		Profile prof = profileMapper.dtoToEntity(currUserProfile);

		
		newUser.setCredentials(creds);
		newUser.setProfile(prof);

		return userMapper.entityToDto(userRepository.saveAndFlush(newUser));
	}
	
	@Override
	public List<TweetResponseDto> getMentions(String username) {
		List<Tweet> allTweets = tweetRepository.findAll();
		ArrayList<TweetResponseDto> mentionedTweets = new ArrayList<TweetResponseDto>();
		
		for (Tweet tweet : allTweets) {
			String currContent = tweet.getContent();
			if (currContent != null && currContent.contains("@" + username)) {
				mentionedTweets.add(tweetMapper.entityToDto(tweet));
			}
		}
		
		return mentionedTweets;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void follow(CredentialsDto credentialsDto, String username) throws NotAuthorizedException, NotFoundException, BadRequestException {
		List<User> users = userRepository.findAll();
		User myUser = new User();
		boolean myUserSet = false;
		boolean myUserDeleted = true;
		
		User userToFollow = new User();
		boolean userToFollowSet = false;
		boolean userToFollowDeleted = true;
		
		System.out.println("User:" + credentialsDto.getUsername());
		
		if (!authenticate(credentialsDto)) {
			throw new NotAuthorizedException("Incorrect username/password provided");
		}
		
		// Populates the myUser and userToFollow with correct values.
		for (User user : users) {
			if (credentialsDto.getUsername().equals(user.getCredentials().getUsername())) {
				myUser = user;
				myUserSet = true;
				if (!myUser.isDeleted())
					myUserDeleted = false;
			}
			
			if (username.equals(user.getCredentials().getUsername())) {
				userToFollow = user;
				userToFollowSet = true;
				if (!userToFollow.isDeleted())
					userToFollowDeleted = false;
			}
		}
		
		// If either variable is not set, there are no usernames with the specified value and will thorw an exception.
		if (!myUserSet) {
			throw new NotFoundException("Username of original user not found");
		}
		
		if (myUserDeleted) {
			throw new BadRequestException("Username of original user is deleted");
		}
		
		if (!userToFollowSet) {
			// This runs when userToFollow is never set.
			throw new NotFoundException("Username of user to follow not found");
		}
		
		if (userToFollowDeleted) {
			throw new BadRequestException("Username of user to follow is deleted");
		}
		
		List<User> myUserCurrFollowing = myUser.getFollowing();
		List<User> userToFollowCurrFollowers = userToFollow.getFollowers();
		
		if (userToFollowCurrFollowers.contains(myUserCurrFollowing)) {
			throw new BadRequestException("Your user follows the specified user already.");
		}
		
		myUserCurrFollowing.add(userToFollow);
		userToFollowCurrFollowers.add(myUser);
		
		myUser.setFollowing(myUserCurrFollowing);
		userToFollow.setFollowers(userToFollowCurrFollowers);
		
		userRepository.saveAndFlush(myUser);
		userRepository.saveAndFlush(userToFollow);
	}
	
	
	private boolean authenticate(CredentialsDto credentials) {
		List<User> users = userRepository.findAll();
		String myUsername = credentials.getUsername();
		for (User user : users) {
			if (user.getCredentials().getUsername().equals(myUsername)) {
				if (user.getCredentials().getPassword().equals(credentials.getPassword())) {
					return true;
				} else {
					// Incorrect password specified
					return false;
				}
			}
		}

		// No user found in database with mathcing username
		return false;
	}
}
