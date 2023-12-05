package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.customException.BadRequestException;
import com.cooksys.socialmedia.customexceptions.NotAuthorizedException;
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

	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getMentions(@PathVariable(value = "username") String username) {
		System.out.println("TEST2");
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
		return userService.createUser(userRequestDto);
	}
	

	@PostMapping("/@{username}/follow")
	public void follow(@RequestBody CredentialsDto credentialsDto, @PathVariable(value = "username") String username) {
		if (credentialsDto == null) { 
			throw new BadRequestException("Credentials needed");
		}
		if (credentialsDto.getPassword() == null || credentialsDto.getUsername() == null) {
			throw new BadRequestException("Username/Password needed");
		}
		System.out.println("GOT HERE 19");
		userService.follow(credentialsDto, username);
	}

	//**************************
	//***********	//GET users/@{username}/followers#104
	@GetMapping("/@{username}/followers")
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
	@PatchMapping("/@{username}")
	@ResponseStatus(HttpStatus.OK)
	public UserResponseDto updateUserProfile(@PathVariable("username") String username, @RequestBody UserRequestDto userRequestDto) {
		if (username == null || userRequestDto == null || userRequestDto.getProfile() == null || userRequestDto.getCredentials() == null) {
			throw new BadRequestException( "Profile or Credentials is null");
		} 
		if (userRequestDto.getCredentials().getUsername() == null || userRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException( "Username or Password is null");

		}
		UserResponseDto userResponseDto = userService.updateUserProfile(username, userRequestDto);
		return userResponseDto;
	}

	//*************
	//GET users/@{username}/feed#107
	//Response ['Tweet']
	@GetMapping("/@{username}/feed")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> getTweetsByUsername(@PathVariable ("username") String username) {
		if(username == null) {
			throw new BadRequestException( "Username is null");
		}
		List<TweetResponseDto> tweetResponseDtos = userService.getTweetsByUsername(username);
		return tweetResponseDtos;
	}

}
