package com.cooksys.socialmedia.controllers;

import java.util.List;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

	@GetMapping("/{username}/mentions")
	public List<TweetResponseDto> getMentions(@PathVariable(value = "username") String username) {
		return userService.getMentions(username);
	}
	@GetMapping
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/@{username}")
	public UserResponseDto getUser(@PathVariable(value = "username") String username) {
		return userService.getUser(username);
	}

	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getTweets(@PathVariable(value = "username") String username) {
		return userService.getTweets(username);
	}

	@GetMapping("/@{username}/following")
	public List<UserResponseDto> getFollowing(@PathVariable(value = "username") String username) {
		return userService.getFollowing(username);
	}

	@PostMapping("/@{username}/unfollow")
	@ResponseStatus(HttpStatus.CREATED)
	public void unfollowUser(@PathVariable(value = "username") String username,
			@RequestBody CredentialsDto credentialsDto) {
		userService.unfollowUser(username, credentialsDto);
	}

	@DeleteMapping("/@{username}")
	public UserResponseDto deleteCustomer(@PathVariable(value = "username") String username,
			@RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}
	
	@PostMapping
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) throws BadRequestException, NotAuthorizedException {
		System.out.println(userRequestDto.getCredentials().getUsername());
		return userService.createUser(userRequestDto);
	}
	
	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getMentions(@PathVariable(value="username") String username ) {

	@PostMapping("/@{username}/follow")
	public void follow(@RequestBody CredentialsDto credentialsDto, @PathVariable(value = "username") String username) {
		userService.follow(credentialsDto, username);
	}

	//**************************
	//***********	//GET users/@{username}/followers#104
	@GetMapping("/{username}/followers")
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponseDto> getUsersWithActiveFollowers(@PathVariable(value = "username") String username) {
		if (username == null) {
			throw new BadRequestException("Username is null");
		} else {
			List<UserResponseDto> activeFollowers = userService.getUsersWithActiveFollowers(username);
			return activeFollowers;
		}
	}

	//*******************
	//PATCH users/@{username}#81
	//Request
	// {
	//  credentials: 'CredentialsDto',
	//  profile: 'ProfileDto'
	//}
	//response 'User'
	@PatchMapping("/{username}")
	@ResponseStatus(HttpStatus.OK)
	public UserResponseDto updateUserProfile(@PathVariable("username") String username, @RequestBody CredentialsDto credentialsDto) {
		if (username == null || credentialsDto == null) {
			throw new BadRequestException( "Username or Credentials are null");
		}
		UserResponseDto userResponseDto = userService.updateUserProfile(username, credentialsDto);
		return userResponseDto;
	}

	//*************
	//GET users/@{username}/feed#107
	//Response ['Tweet']
	@GetMapping("/{username}/feed")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> getTweetsByUsername(@PathVariable ("username") String username) {
		if(username == null) {
			throw new BadRequestException( "Username is null");
		}
		List<TweetResponseDto> tweetResponseDtos = userService.getTweetsByUsername(username);
		return tweetResponseDtos;
	}

}
