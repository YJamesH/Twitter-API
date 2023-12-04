package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	private final TweetService tweetService;

	@GetMapping("/{id}/tags")
	public List<HashtagResponseDto> getHashtags(@PathVariable Long id) {
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
	
	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
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
	public void likeTweet(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
		tweetService.likeTweet(id, userRequestDto);
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
