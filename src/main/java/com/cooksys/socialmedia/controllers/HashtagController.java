package com.cooksys.socialmedia.controllers;

import java.util.List;

import com.cooksys.socialmedia.dtos.TweetResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {

	private final HashtagService hashtagService;
	
	@GetMapping
	public List<HashtagResponseDto> getAllTags() {
		return hashtagService.getAllTags();
	}

	//*******GET tags/{label}#101*************
	//*******Response ['Tweet']**********
	@GetMapping("/{label}")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> getTweetsByLabel(@PathVariable ("label") String label) {
		List<TweetResponseDto> tweetResponseDto = hashtagService.getTweetsByLabel(label);
		return  tweetResponseDto;
	}
}
