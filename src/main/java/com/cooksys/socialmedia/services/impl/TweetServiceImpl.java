package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.customexceptions.NotFoundException;
import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;

	private final HashtagRepository hashtagRepository;
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


	@Override
	public List<TweetResponseDto> getAllTweets() {
		List<Tweet> tweets = tweetRepository.findAll();
		List<TweetResponseDto> toReturn = new ArrayList<>();

		for (Tweet tweet : tweets) {
			toReturn.add(tweetMapper.entityToDto(tweet));
		}

		return toReturn;
	}

	@Override
	public TweetResponseDto postTweet(TweetRequestDto tweetRequestDto) throws NotAuthorizedException {
		if (authenticate(tweetRequestDto.getCredentials())) {
			
			Tweet currTweet = tweetMapper.dtoToEntity(tweetRequestDto);
			User myUser = getUser(tweetRequestDto.getCredentials());

			myUser.getTweets().add(currTweet);
			addHashtags(currTweet.getContent(), currTweet);
			userRepository.saveAndFlush(myUser);
			currTweet.setAuthor(myUser);

			return tweetMapper.entityToDto(tweetRepository.saveAndFlush(currTweet));
		} else {
			throw new NotAuthorizedException("Invalid Username/Password provided");
		}
	}

	@Override
	public TweetResponseDto findTweetById(Long id) throws BadRequestException, NotFoundException {
		Optional<Tweet> currTweet = tweetRepository.findById(id);

		if (currTweet.isPresent()) {
			Tweet myTweet = currTweet.get();

			if (myTweet.isDeleted()) {
				throw new BadRequestException("The tweet you are looking for has been deleted");
			}
			
			return tweetMapper.entityToDto(myTweet);
		} else {
			throw new NotFoundException("The tweet you are looking for was not found");
		}
	}

	@Override
	public TweetResponseDto postReply(TweetRequestDto tweetRequestDto, Long id) throws BadRequestException, NotFoundException, NotAuthorizedException {
		if (authenticate(tweetRequestDto.getCredentials())) {
			Tweet currTweet = tweetMapper.dtoToEntity(tweetRequestDto);
			User myUser = getUser(tweetRequestDto.getCredentials());

			Optional<Tweet> ogTweet = tweetRepository.findById(id);
			if (ogTweet.isPresent()) {
				Tweet myTweet = ogTweet.get();

				if (myTweet.isDeleted()) {
					throw new BadRequestException("The original tweet was deleted.");
				}
				
				currTweet.setInReplyTo(myTweet);
				myUser.getTweets().add(currTweet);
				addHashtags(currTweet.getContent(), currTweet);
				userRepository.saveAndFlush(myUser);
				currTweet.setAuthor(myUser);

				return tweetMapper.entityToDto(tweetRepository.saveAndFlush(currTweet));
			} else {
				throw new NotFoundException("Tweet not found");
			}
		} else {
			throw new NotAuthorizedException("Invalid username/password provided");
			
		}
	}

	@Override
	public List<TweetResponseDto> getReplies(Long id) throws BadRequestException {
		List<Tweet> tweets = tweetRepository.findAll();
		List<TweetResponseDto> toReturn = new ArrayList<>();
		Optional<Tweet> currTweet = tweetRepository.findById(id);

		if (currTweet.isPresent()) {
			for (Tweet tweet : tweets) {
				if (tweet.getInReplyTo() != null && tweet.getInReplyTo().getId() == id && !tweet.isDeleted()) {
					toReturn.add(tweetMapper.entityToDto(tweet));
				}
			}

			return toReturn;
		} else {
			throw new BadRequestException("Tweet not found");
		}
	}

	@Override
	public List<UserResponseDto> getLikes(Long id) throws NotFoundException {
		List<User> usersThatLiked = new ArrayList<>();
		List<UserResponseDto> toReturn = new ArrayList<>();
		Optional<Tweet> currTweet = tweetRepository.findById(id);

		if (currTweet.isPresent()) {
			Tweet myTweet = currTweet.get();

			usersThatLiked = myTweet.getUserLikes();

			for (User user : usersThatLiked) {
				toReturn.add(userMapper.entityToDto(user));
			}

			return toReturn;
		} else {
			throw new NotFoundException("Tweet not found in repository");
		}
	}

	// Private helper method to flush all new hashtags
	private void addHashtags(String content, Tweet tweet) {
		// Splits the string into an array of strings, with the first word of each entry
		// (besides the first entry)
		// being a new hashtag.
		String[] tags = content.split("#");

		if (tags.length == 1) {
			return;
		} else if (tags.length > 1) {
			// Starting at i = 1 since tags[0] isn't delimited by '#' in the original
			// string, but the rest are.
			for (int i = 1; i < tags.length; i++) {
				String[] tagArray = tags[i].split(" ");
				Hashtag myTag = getTag(tagArray[0]);
				myTag.setLabel(tagArray[0]);
				if (myTag.getTweets() == null) {
					List<Tweet> tweetsToAdd = new ArrayList<>();
					tweetsToAdd.add(tweet);
					myTag.setTweets(tweetsToAdd);
				} else {
					myTag.getTweets().add(tweet);
				}
				hashtagRepository.saveAndFlush(myTag);
			}
		}
	}

	// Private helper method to construct a new hashtag object or find existing
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

	// Returns user object based on provided credentials
	private User getUser(CredentialsDto credentials) {
		List<User> users = userRepository.findAll();
		String myUsername = credentials.getUsername();
		for (User user : users) {
			if (user.getCredentials().getUsername().equals(myUsername)) {
				return user;
			}
		}

		// If code reaches here, then the user is not found in the DB
		throw new NotFoundException("No users exist with specified credentials.");

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

	public Tweet getTweetWithId(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet not found");
		}
		Tweet tweet = optionalTweet.get();
		if (tweet.isDeleted()) {
			throw new BadRequestException("Tweet deleted");
		}
		return tweet;
	}

	public User getUserAndCheckCredentials(UserRequestDto userRequestDto) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(userRequestDto.getCredentialsDto().getUsername());
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User not found");
		} 
		User user = optionalUser.get();
		
		String userPassword = user.getCredentials().getPassword();
		String userInRepoPassword = userRequestDto.getCredentialsDto().getPassword();
		if (!userPassword.equals(userInRepoPassword)) {
			throw new NotAuthorizedException("Password does not match");
		}
		if (user.isDeleted()) {
			throw new BadRequestException("User deleted");
		}
		return user;
	}
  
}
