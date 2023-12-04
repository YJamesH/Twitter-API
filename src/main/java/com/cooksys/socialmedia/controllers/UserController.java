package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.customexceptions.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
import com.cooksys.socialmedia.customexceptions.NotFoundException;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	
	@PostMapping
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) throws BadRequestException, NotAuthorizedException {
		System.out.println(userRequestDto.getCredentials().getUsername());
		return userService.createUser(userRequestDto);
	}
	
	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getMentions(@PathVariable(value="username") String username ) {
		return userService.getMentions(username);
	}
	
	@PostMapping("/@{username}/follow")
	public void follow(@RequestBody CredentialsDto credentialsDto, @PathVariable(value="username") String username) throws NotAuthorizedException, NotFoundException, BadRequestException {
		System.out.println("User1: " + credentialsDto.getUsername());
		userService.follow(credentialsDto, username);
	}
	
}
