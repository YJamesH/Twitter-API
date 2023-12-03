package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
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
	public TweetResponseDto findTweetById(Long id) throws BadRequestException {
		Optional<Tweet> currTweet = tweetRepository.findById(id);

		if (currTweet.isPresent()) {
			Tweet myTweet = currTweet.get();

			if (myTweet.isDeleted()) {
				throw new BadRequestException("The tweet you are looking for has been deleted");
			}
			
			return tweetMapper.entityToDto(myTweet);
		} else {
			throw new BadRequestException("The tweet you are looking for was not found");
		}
	}

	@Override
	public TweetResponseDto postReply(TweetRequestDto tweetRequestDto, Long id) throws BadRequestException, NotAuthorizedException {
		if (authenticate(tweetRequestDto.getCredentials())) {
			Tweet currTweet = tweetMapper.dtoToEntity(tweetRequestDto);
			User myUser = getUser(tweetRequestDto.getCredentials());

			Optional<Tweet> ogTweet = tweetRepository.findById(id);
			if (ogTweet.isPresent()) {
				Tweet myTweet = ogTweet.get();

				currTweet.setInReplyTo(myTweet);
				myUser.getTweets().add(currTweet);
				addHashtags(currTweet.getContent(), currTweet);
				userRepository.saveAndFlush(myUser);
				currTweet.setAuthor(myUser);

				return tweetMapper.entityToDto(tweetRepository.saveAndFlush(currTweet));
			} else {
				throw new BadRequestException("Tweet not found");
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
	public List<UserResponseDto> getLikes(Long id) throws BadRequestException {
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
			throw new BadRequestException("Tweet not found in repository");
		}
	}

	@Override
	public void likeTweet(Long id, UserRequestDto userRequestDto) {
		Optional<Tweet> currTweet = tweetRepository.findById(id);
		CredentialsDto creds = userRequestDto.getCredentials();
		Tweet myTweet = new Tweet();

		try {
			myTweet = currTweet.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		User user = getUser(userRequestDto.getCredentials());
		if (authenticate(creds)) {

			myTweet.getUserLikes().add(user);
			user.getTweetLikes().add(myTweet);

			tweetRepository.saveAndFlush(myTweet);
			userRepository.saveAndFlush(user);
		} else {
			// Authentication error
		}
	}

	// Private helper method
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
				myTag.getTweets().add(tweet);
				hashtagRepository.saveAndFlush(myTag);
			}
		}
	}

	// Private helper method
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

	private User getUser(CredentialsDto credentials) {
		List<User> users = userRepository.findAll();
		String myUsername = credentials.getUsername();
		for (User user : users) {
			if (user.getCredentials().getUsername().equals(myUsername)) {
				return user;
			}
		}

		// catch user not found exception
		return null;

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
