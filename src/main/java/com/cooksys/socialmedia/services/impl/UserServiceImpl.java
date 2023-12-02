package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Credentials;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.CredentialsMapper;
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
	public List<UserResponseDto> getFollowing(String username) {
		return userMapper.entitiesToUserDtos(getUserWithUsername(username).getFollowing());
	}
	
	@Override
	public void unfollowUser(String username, CredentialsRequestDto credentialsRequestDto) {
		Credentials credentials = credentialsMapper.dtoToEntity(credentialsRequestDto);
		User following = getUserWithUsername(username);

		// check if credentials match a user in the database
		User userToCheck = getUserWithUsername(credentialsRequestDto.getUsername());
		if (!credentials.equals(userToCheck.getCredentials())) {
//			throw new BadRequestException("Cannot unfollow yourself");
		}

		// check if users are following each other
		if (userToCheck.getFollowing().contains(following)) {
			userToCheck.getFollowing().remove(following);
			following.getFollowers().remove(userToCheck);
			userRepository.saveAndFlush(userToCheck);
			userRepository.saveAndFlush(following);
		} else {
//			throw new BadRequestException("Not following user");
		}
	}
	
	
	@Override
	public UserResponseDto deleteUser(String username, CredentialsRequestDto credentialsRequestDto) {
		User user = getUserAndCheckCredentials(username, credentialsRequestDto);
		user.setDeleted(true);
		userRepository.saveAndFlush(user);
		return userMapper.entityToDto(user);
	}
	
	
//	@Override
//	public List<TweetResponseDto> getMentions(String username) {
//		List<Tweet> allTweets = tweetRepository.findAll();
//		ArrayList<TweetResponseDto> mentionedTweets = new ArrayList<TweetResponseDto>();
//
//		for (Tweet tweet : allTweets) {
//			String currContent = tweet.getContent();
//			if (currContent.contains("@" + username)) {
//				mentionedTweets.add(tweetMapper.entityToDto(tweet));
//			}
//		}
//
//		return mentionedTweets;
//	}

//	@Override
//	public void follow(CredentialsRequestDto credentialsRequestDto, String username) {
//		List<User> users = userRepository.findAll();
//		User myUser = new User();
//		boolean myUserSet = false;
//
//		User userToFollow = new User();
//		boolean userToFollowSet = false;
//
//		// Populates the myUser and userToFollow with correct values.
//		for (User user : users) {
//			if (credentialsRequestDto.getUsername().equals(user.getCredentials().getUsername())) {
//				myUser = user;
//				myUserSet = true;
//			}
//
//			if (username.equals(user.getCredentials().getUsername())) {
//				userToFollow = user;
//				userToFollowSet = true;
//			}
//		}
//
//		// If either variable is not set, there are no usernames with the specified
//		// value and will throw an exception.
//		if (!myUserSet) {
//			// throw some exception here
//		}
//
//		if (!userToFollowSet) {
//			// throw some exception here
//		}
//
//		List<User> myUserCurrFollowing = myUser.getFollowing();
//		List<User> userToFollowCurrFollowers = userToFollow.getFollowers();
//
//		myUserCurrFollowing.add(userToFollow);
//		userToFollowCurrFollowers.add(myUser);
//
//		myUser.setFollowing(myUserCurrFollowing);
//		userToFollow.setFollowers(userToFollowCurrFollowers);
//
//		userRepository.saveAndFlush(myUser);
//		userRepository.saveAndFlush(userToFollow);
//	}
//

	private User getUserWithUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
//			throw new NotFoundException("User not found");
		}
		User user = optionalUser.get();
		if (user.isDeleted()) {
//			System.out.println("DELETED USER");
//			throw new BadRequestException("User deleted");
		}
		return user;
	}

	private User getUserAndCheckCredentials(String username, CredentialsRequestDto credentialsRequestDto) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
//			throw new NotFoundException("User not found");
		}
		User user = optionalUser.get();
		if (!user.getCredentials().getPassword().equals(credentialsRequestDto.getPassword())) {
//			throw new NotAuthorizedException("Password does not match");
		}
		if (user.isDeleted()) {
//			throw new BadRequestException("User deleted");
		}
		return user;
	}


}
