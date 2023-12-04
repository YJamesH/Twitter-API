package com.cooksys.socialmedia.services.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.customException.NotAuthorizedException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
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
    //**********************************
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;

	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;

    //**************************************
    //GET tweets#100
    //Retrieves all (non-deleted) tweets. The tweets should appear in reverse-chronological order.
    //['Tweet'] response
    @Override
    public List<TweetResponseDto> getSortedTweets() {
        List<Tweet> tweets = tweetRepository.findAll();
        if( tweets == null ) {
            throw new NotFoundException("Tweets not found");
        }
        Comparator<Tweet> comp = (t1, t2)-> t1.getId().compareTo(t2.getId());
        List<Tweet> sortedTweets = tweets.stream().filter(t->t.isDeleted()).sorted(comp.reversed()).collect(Collectors.toList());

        List<TweetResponseDto> tweetDtos = tweetMapper.entitiesToResponseDtos(sortedTweets);
        return tweetDtos;
    }

    @Override
    public List<UserResponseDto> getMentionedUsers(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if(optionalTweet.isEmpty()) {
            throw new IllegalArgumentException("Tweet id not found");
        }
        List<User> userMentions = optionalTweet.get().getUserMentions();
        return userMapper.entitiesToResponseDtos(userMentions);
    }


    //****************************
   //********************************* POST tweets/{id}/repost#94
    //Creates a repost of the tweet with the given id.
    // The author of the repost should match the credentials provided in the request body.
    // If the given tweet is deleted or otherwise doesn't exist, or the given credentials do not match an active user in the database, an error should be sent in lieu of a response.

    //Because this creates a repost tweet, content is not allowed.

    // Additionally, notice that the repostOf property is not provided by the request.
    // The server must create that relationship.

    //The response should contain the newly-created tweet.
    //Request 'Credentials'
    //Response 'Tweet'

    @Override
    public TweetResponseDto addRepostToTweet(Long id, CredentialsDto credentialsDto) throws NotAuthorizedException, NotFoundException {
        // validation for null
        if ((id == null) || (credentialsDto == null)) {
            throw new IllegalArgumentException("Tweet id  or credentials missing......");
        }

        //Get tweet by id
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        Tweet tweet = null;

        //validation for  tweet exist
        if (!optionalTweet.isEmpty()) {
            tweet= optionalTweet.get();

            //      validation for author exist,  otherwise author is active
            //      otherwise If the given tweet is deleted

            if ( tweet.getAuthor() == null || tweet.getAuthor().isDeleted() || tweet.isDeleted()) {
                throw new NotFoundException("Author not found or not active  or Tweet deleted");
            }

            //      validation for the given credentials do not match an active user in the database,
            if (!tweet.getAuthor().getCredentials().getUsername().equals(credentialsDto.getUsername())) {
                throw new NotAuthorizedException("User not authorized");
            }
        } else {
            throw new NotFoundException("Given Tweet id not found");
        }
        Tweet newTweet = new Tweet();
        newTweet.setAuthor(tweet.getAuthor());
        newTweet.getAuthor().setCredentials(tweet.getAuthor().getCredentials());
        newTweet.getAuthor().setProfile(tweet.getAuthor().getProfile());
        newTweet.setPosted(Timestamp.from(Instant.now()));
        newTweet.setRepostOf(tweet);
        tweetRepository.saveAndFlush(newTweet);
        return tweetMapper.entityToDto(newTweet);
    }

    //   ***********************************************************************
    //****************************    //DELETE tweets/{id}#97
    //"Deletes" the tweet with the given id.
    // If no such tweet exists or the provided credentials do not match author of the tweet, an error should be sent in lieu of a response.
    // If a tweet is successfully "deleted", the response should contain the tweet data prior to deletion.
    //
    //IMPORTANT: This action should not actually drop any records from the database! Instead,
    // develop a way to keep track of "deleted" tweets so that even if a tweet is deleted,
    // data with relationships to it (like replies and reposts) are still intact.
    // Request 'Credentials'
    //Response 'Tweet'

    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
        // validation for null
        if(id == null || credentialsDto == null){
            throw new IllegalArgumentException("check....Tweet id / Crendentials  are null");
        }

        //validation for exist
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty() ){
            throw new NotFoundException("Tweet not Found");
         }
        Tweet tweetToDelete = optionalTweet.get();

        // validation for matching credentionls
        if (!tweetToDelete.getAuthor().getCredentials().getUsername().equals(credentialsDto.getUsername())) {
            throw new BadRequestException("Credential Not matching");
        }

        // to keep track of "deleted" tweets
        tweetToDelete.setDeleted(true);
        tweetToDelete.setContent("This is deleted tweet "+ "(Tweet "+tweetToDelete.getId()+ ")" + "(user "+tweetToDelete.getAuthor().getId()+ ")");
        tweetRepository.saveAndFlush(tweetToDelete);
        return tweetMapper.entityToDto(tweetToDelete);
    }
    //*****************************
    //GET tweets/{id}/context#91
    //Retrieves the context of the tweet with the given id.
    // If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.
    //
    //IMPORTANT: While deleted tweets should not be included in the before and after properties of the result, transitive replies should.
    // What that means is that if a reply to the target of the context is deleted, but there's another reply to the deleted reply,
    // the deleted reply should be excluded but the other reply should remain.
    //Response Context'
    @Override
    public TweetResponseDto getTweetContextById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("check....Tweet id is  null");
        }
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty() ){
            throw new NotFoundException("Not found");
        }
        Tweet tweet = optionalTweet.get();

        if(tweet.isDeleted()) {
            throw new BadRequestException("deleted");
        }
        boolean exist = false;
        while(tweet.getInReplyTo() != null) {
            if(tweet.isDeleted()){
                exist = true;
                break;
            }
            tweet = tweet.getInReplyTo();
        }
        if (exist) {
            deleteTweet(tweet);
        }

        return tweetMapper.entityToDto(tweet);
    }
	
	@Override
	public List<HashtagResponseDto> getHashtags(Long id) {
		return hashtagMapper.entitiesToHashtagDtos(getTweetWithId(id).getHashtags());
	}
	
	@Override
	public List<TweetResponseDto> getReposts(Long id) {
		return tweetMapper.entitiesToResponseDtos(getTweetWithId(id).getReposts());
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
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User not found");
		} 
		User user = optionalUser.get();
		
		String userPassword = user.getCredentials().getPassword();
		String userInRepoPassword = userRequestDto.getCredentials().getPassword();
		if (!userPassword.equals(userInRepoPassword)) {
			throw new NotAuthorizedException("Password does not match");
		}
		if (user.isDeleted()) {
			throw new BadRequestException("User deleted");
		}
		return user;
	}

    private void deleteTweet(Tweet tweetToDelete) {
        if (tweetToDelete == null) {
            return;
        }
        Tweet previousTweet = null;
        Tweet currentTweet = tweetToDelete;
        Tweet nextTweet = currentTweet.getInReplyTo();

        while (nextTweet != null) {
            if(nextTweet.isDeleted()) {
                // Resetting the relation
                currentTweet.setInReplyTo(null);
                currentTweet = nextTweet;
                nextTweet = currentTweet.getInReplyTo();

                // Updating the relationship
                if (previousTweet != null) {
                    previousTweet.setInReplyTo(currentTweet);
                }

                previousTweet = currentTweet;
            } else {
                nextTweet = nextTweet.getInReplyTo();
            }
        }
    }
  
}
