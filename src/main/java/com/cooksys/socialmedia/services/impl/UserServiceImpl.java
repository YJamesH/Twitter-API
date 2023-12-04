package com.cooksys.socialmedia.services.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.customException.NotAuthorizedException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.*;
import com.cooksys.socialmedia.mappers.CredentialsMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.HttpClientErrorException;

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
	public List<UserResponseDto> getAllUsers() {
		List<User> usersNotDeleted = new ArrayList<>();
		for (User user : userRepository.findAll()) {
			if(!user.isDeleted()) {
				usersNotDeleted.add(user);
			}
		}
		return userMapper.entitiesToUserDtos(usersNotDeleted);
	}
	
	@Override
	public UserResponseDto getUser(String username) {
		return userMapper.entityToDto(getUserWithUsername(username));
	}
    
  @Override
	public List<TweetResponseDto> getTweets(String username) {
		User user = getUserWithUsername(username);
		List<Tweet> allTweets = tweetRepository.findAll();
		ArrayList<TweetResponseDto> tweetsByUser = new ArrayList<>();
		for (Tweet tweet : allTweets) {
			if (tweet.getAuthor().equals(user) && !tweet.isDeleted()) {
				tweetsByUser.add(tweetMapper.entityToDto(tweet));
			}
		}
		return tweetsByUser;
	}
    
	
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
	public List<UserResponseDto> getFollowing(String username) {
		return userMapper.entitiesToUserDtos(getUserWithUsername(username).getFollowing());
	}
	
	@Override
	public void unfollowUser(String username, CredentialsDto credentialsDto) {
		Credentials credentials = credentialsMapper.dtoToEntity(credentialsDto);
		User following = getUserWithUsername(username);

		// check if credentials match a user in the database
		User userToCheck = getUserWithUsername(credentialsDto.getUsername());
		if (!credentials.equals(userToCheck.getCredentials())) {
			throw new BadRequestException("Cannot unfollow yourself");
		}

		// check if users are following each other
		if (userToCheck.getFollowing().contains(following)) {
			userToCheck.getFollowing().remove(following);
			following.getFollowers().remove(userToCheck);
			userRepository.saveAndFlush(userToCheck);
			userRepository.saveAndFlush(following);
		} else {
			throw new BadRequestException("Not following user");
		}
	}
	
	
	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		User user = getUserAndCheckCredentials(username, credentialsDto);
		user.setDeleted(true);
		userRepository.saveAndFlush(user);
		return userMapper.entityToDto(user);
	}

	
	// HELPER METHODS //
	private User getUserWithUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		User user = optionalUser.get();
		if (user.isDeleted()) {
			throw new BadRequestException("User deleted");
		}
		return user;
	}

	private User getUserAndCheckCredentials(String username, CredentialsDto credentialsDto) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		User user = optionalUser.get();
		if (!user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new NotAuthorizedException("Password does not match");
		}
		if (user.isDeleted()) {
			throw new BadRequestException("User deleted");
		}
		return user;
	}


  @Override
	public void follow(CredentialsDto credentialsDto, String username) throws NotAuthorizedException, NotFoundException, BadRequestException {
		List<User> users = userRepository.findAll();
		User myUser = new User();
		boolean myUserSet = false;
		boolean myUserDeleted = true;
		
		User userToFollow = new User();
		boolean userToFollowSet = false;
		boolean userToFollowDeleted = true;
		
		
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
			// This runs when userToFollow is not found.
			throw new NotFoundException("Username of user to follow not found");
		}
		
		if (userToFollowDeleted) {
			throw new BadRequestException("Username of user to follow is deleted");
		}

		List<User> myUserCurrFollowing = myUser.getFollowing();
		List<User> userToFollowCurrFollowers = userToFollow.getFollowers();
		
		if (userToFollowCurrFollowers.contains(myUser)) {
			throw new BadRequestException("Your user follows the specified user already.");
		}
		
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
	public UserResponseDto updateUserProfile(String username, CredentialsDto credentialsDto) {
		User user = userRepository.findByCredentialsUsername(username);

		if(user== null){
			throw new NotFoundException("user not found");
		}
		// validate active or not
		if(user.isDeleted()){
			throw new BadRequestException("User not Active");
		}
		// validate credentials
		if (!user.getCredentials().getUsername().equals(credentialsDto.getUsername())) {
			throw new NotAuthorizedException("Credentials not matching");
		}
		user.setCredentials(credentialsMapper.requestDtoToEntity(credentialsDto));
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
	
	// Helper method to check credentials
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
