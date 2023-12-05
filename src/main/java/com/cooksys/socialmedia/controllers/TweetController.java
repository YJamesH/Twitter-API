package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.dtos.ContextDto;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    //***********************
    //************GET tweets#100
    //****************['Tweet'] response
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllTweets();
    }
    
    //******************
    //GET tweets/{id}/mentions#88
    //Retrieves the users mentioned in the tweet with the given id.
    // If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

    //Deleted users should be excluded from the response.

    //IMPORTANT Remember that tags and mentions must be parsed by the server!
    //Response ['User']
    @GetMapping("/{id}/mentions")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getMentionedUsers(@PathVariable("id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Tweet id is NULL");
        }
        List<UserResponseDto> tweetResponseDtos = tweetService.getMentionedUsers(id);
        return tweetResponseDtos;
    }


    //********************
    //GET tweets/{id}/context#91
    //Response Context'
    @GetMapping("/{id}/context")
    @ResponseStatus(HttpStatus.OK)
    public ContextDto getTweetContextById(@PathVariable("id") Long id) {
        System.out.println("GOT HERE 339");

    	ContextDto contextDto = tweetService.getTweetContextById(id);
        
        return contextDto;
    }

    //***********************
    //*****************POST tweets/{id}/repost#94
    //*************The response should contain the newly-created tweet.
    //************Request 'Credentials'
    //Response 'Tweet'
    @PostMapping("/{id}/repost")
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto addRepostToTweet(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto) {
        if ((id == null) || (credentialsDto == null)) {
            throw new BadRequestException("Tweet id  or credentials missing");
        }
        TweetResponseDto tweetResponseDto = tweetService.addRepostToTweet(id, credentialsDto);
        return tweetResponseDto;
    }

    //**********************
    //**************DELETE tweets/{id}#97
    //*************Response 'Tweet'
    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweetById(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto) {
        TweetResponseDto tweetResponseDto = tweetService.deleteTweetById(id, credentialsDto);
        return tweetResponseDto;
    }
	

	@GetMapping("/{id}/tags")
	public List<HashtagResponseDto> getHashtags(@PathVariable Long id) {
		System.out.println("GOT HERE 15");
		return tweetService.getHashtags(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getReposts(@PathVariable Long id) {
		return tweetService.getReposts(id);
	}
	
	@PostMapping
	public TweetResponseDto postTweet(@RequestBody TweetRequestDto tweetRequestDto) throws NotAuthorizedException {
		return tweetService.postTweet(tweetRequestDto);
	}

	@GetMapping("/{id}")
	public TweetResponseDto findTweetById(@PathVariable(name="id") Long id) throws BadRequestException {
		return tweetService.findTweetById(id);
	}
	
	@PostMapping("/{id}/reply")
	public TweetResponseDto postReply(@RequestBody TweetRequestDto tweetRequestDto, @PathVariable(value="id") Long id) throws BadRequestException, NotAuthorizedException {
		return tweetService.postReply(tweetRequestDto, id);
	}
	
	@PostMapping("/{id}/like")
	@ResponseStatus(HttpStatus.CREATED)
	public void likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.likeTweet(id, credentialsDto);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getLikes(@PathVariable(value="id") Long id) throws BadRequestException {
		return tweetService.getLikes(id);
	}
	
	@GetMapping("/{id}/replies") 
	public List<TweetResponseDto> getReplies(@PathVariable(value="id") Long id) throws BadRequestException {
		return tweetService.getReplies(id);
	}
}
