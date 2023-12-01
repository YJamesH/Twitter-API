package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.HashtagMapper;
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
	public List<TweetResponseDto> getAllTweets() {
		List<Tweet> tweets = tweetRepository.findAll();
		List<TweetResponseDto> toReturn = new ArrayList<>();
		
		for (Tweet tweet : tweets) {
			toReturn.add(tweetMapper.entityToDto(tweet));
		}
		
		return toReturn;
	}
	
	@Override
	public TweetResponseDto postTweet(TweetRequestDto tweetRequestDto) {
		if (authenticate(tweetRequestDto.getCredentials())) {
			Tweet currTweet = tweetMapper.dtoToEntity(tweetRequestDto);
			User myUser = getUser(tweetRequestDto.getCredentials());
			
			myUser.getTweets().add(currTweet);
			addHashtags(currTweet.getContent(), currTweet);
			userRepository.saveAndFlush(myUser);
			currTweet.setAuthor(myUser);
			
			return tweetMapper.entityToDto(tweetRepository.saveAndFlush(currTweet));
		} else {
			// Authentication error exception
			return null;
		}
	}

	@Override
	public TweetResponseDto findTweetById(Long id) {
		Optional<Tweet> currTweet = tweetRepository.findById(id);
		
		if (currTweet.isPresent()) {
			Tweet myTweet = currTweet.get();
			
			return tweetMapper.entityToDto(myTweet);
		}
		// Implement exception here later
		System.out.println("No Tweet");
		return null;
	}

	@Override
	public TweetResponseDto postReply(TweetRequestDto tweetRequestDto, Long id) {
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
			}
			else {
				System.out.println("Tweet not found");
				// Catch no tweet exception here
				return null;
			}
		} else {
			System.out.println("Auth Error");
			// catch authentication error exception here
			return null;
		}
	}
	
	@Override
	public List<TweetResponseDto> getReplies(Long id) {
		List<Tweet> tweets = tweetRepository.findAll();
		List<TweetResponseDto> toReturn = new ArrayList<>();
		Optional<Tweet> currTweet = tweetRepository.findById(id);
		
		if (currTweet.isPresent()) {
			for (Tweet tweet : tweets) {
				if (tweet.getInReplyTo() != null && tweet.getInReplyTo().getId() == id) {
					toReturn.add(tweetMapper.entityToDto(tweet));
				}
			}
			
			return toReturn;
		}
		
		
		return null;
	}
	
	@Override
	public List<UserResponseDto> getLikes(Long id) {
		List<User> users = userRepository.findAll();
		List<UserResponseDto> toReturn = new ArrayList<>();
		Optional<Tweet> currTweet = tweetRepository.findById(id);
		
		if (currTweet.isPresent()) { 
			// Can be re-implemented later to just return Tweet.getUserLikes() after it's been mapped.
			// Current implementation based on the way the DB is seeded.
			for (User user : users) {
				boolean flagLikesContainTweet = false;
				List<Tweet> likedTweets = user.getTweets();
				if (likedTweets.size() > 0)
					System.out.println(likedTweets.get(0).getContent());
				for (Tweet tweet : likedTweets) {
					if (tweet.getId() == id) {
						flagLikesContainTweet = true;
						break;
					}
				}
				if (flagLikesContainTweet) {
					toReturn.add(userMapper.entityToDto(user));
				}
			}
			
			return toReturn;
		}
		
		// no tweet found exception
		return null;
		
	}

	// Private helper method
	private void addHashtags(String content, Tweet tweet) {
		// Splits the string into an array of strings, with the first word of each entry (besides the first entry)
		// being a new hashtag.
		String[] tags = content.split("#");
		
		if (tags.length == 1) {
			return;
		}
		else if (tags.length > 1) {
			// Starting at i = 1 since tags[0] isn't delimited by '#' in the original string, but the rest are.
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
			if (tag.getLabel().equals(label)) return tag;
		}
		// If code reaches this point, no tag with this label is found in the db so a new one is created
		
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
