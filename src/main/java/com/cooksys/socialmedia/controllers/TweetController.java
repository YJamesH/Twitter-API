package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping
	public TweetResponseDto postTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.postTweet(tweetRequestDto);
	}
	
	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
	}
	
	@GetMapping("/{id}")
	public TweetResponseDto findTweetById(@PathVariable(name="id") Long id) {
		return tweetService.findTweetById(id);
	}
	
	@PostMapping("/{id}/reply")
	public TweetResponseDto postReply(@RequestBody TweetRequestDto tweetRequestDto, @PathVariable(value="id") Long id) {
		return tweetService.postReply(tweetRequestDto, id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getLikes(@PathVariable(value="id") Long id) {
		return tweetService.getLikes(id);
	}
	
	@GetMapping("/{id}/replies") 
	public List<TweetResponseDto> getReplies(@PathVariable(value="id") Long id) {
		return tweetService.getReplies(id);
	}
}
