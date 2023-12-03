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

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
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

	@PostMapping("/@{username}/follow")
	public void follow(@RequestBody CredentialsRequestDto credentialsRequestDto, @PathVariable(value = "username") String username) {
		userService.follow(credentialsRequestDto, username);
	}

	//**************************
	//***********	//GET users/@{username}/followers#104
	@GetMapping("/{username}/followers")
	public ResponseEntity<List<UserResponseDto>> getUsersWithActiveFollowers(@PathVariable(value = "username") String username) {
		if (username == null) {
			throw new BadRequestException("Username is null");
		} else {
			List<UserResponseDto> activeFollowers = userService.getUsersWithActiveFollowers(username);
			return new ResponseEntity<>(activeFollowers, HttpStatus.OK);
		}
	}

	//******************* working tested
	//PATCH users/@{username}#81
	//Request
	// {
	//  credentials: 'CredentialsDto',
	//  profile: 'ProfileDto'
	//}
	//response 'User'
	@PatchMapping("/{username}")
	public ResponseEntity<UserResponseDto> updateUserProfile(@PathVariable("username") String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
		if (username == null || credentialsRequestDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		UserResponseDto userResponseDto = userService.updateUserProfile(username, credentialsRequestDto);
		return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
	}

	//*************
	//GET users/@{username}/feed#107
	//Response ['Tweet']
	@GetMapping("/{username}/feed")
	public ResponseEntity<List<TweetResponseDto>> getTweetsByUsername(@PathVariable ("username") String username) {
		if(username == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		List<TweetResponseDto> tweetResponseDtos = userService.getTweetsByUsername(username);
		return new ResponseEntity<>(tweetResponseDtos,HttpStatus.OK);
	}

//********************************
}