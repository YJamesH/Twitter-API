package com.cooksys.socialmedia.services.impl;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.customException.NotAuthorizedException;
import com.cooksys.socialmedia.customException.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.ErrorDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.TweetRepository;
import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final UserMapper userMapper;

    //**************************************
    //GET tweets#100
    //Retrieves all (non-deleted) tweets. The tweets should appear in reverse-chronological order.
    //['Tweet'] response
    @Override
    public List<TweetResponseDto> getSortedTweets() {
        List<TweetResponseDto> tweets = tweetMapper.entitiesToResponseDtos(tweetRepository.findAll());

        //validation for existed tweets
        if( tweets == null ) {
            throw new NotFoundException("Tweets not found");
        }
        // for reverse-chronological order
        Comparator<TweetResponseDto> comp = (t1, t2)-> t1.getId().compareTo(t2.getId());
        return tweets.stream().sorted(comp.reversed()).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getMenstionedUsers(Long id) {
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
    public TweetResponseDto addRepostToTweet(Long id, CredentialsRequestDto credentialsRequestDto) throws NotAuthorizedException, NotFoundException {
        // validation for null
        if ((id == null) || (credentialsRequestDto == null)) {
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
            if (!tweet.getAuthor().getCredentials().getUsername().equals(credentialsRequestDto.getUsername())) {
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
    public TweetResponseDto deleteTweetById(Long id, CredentialsRequestDto credentialsRequestDto) {
        // validation for null
        if(id == null || credentialsRequestDto == null){
            throw new IllegalArgumentException("check....Tweet id / Crendentials  are null");
        }

        //validation for exist
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty() ){
            throw new NotFoundException("Tweet not Found");
         }
        Tweet tweetToDelete = optionalTweet.get();

        // validation for matching credentionls
        if (!tweetToDelete.getAuthor().getCredentials().getUsername().equals(credentialsRequestDto.getUsername())) {
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
    //**********************************
}
