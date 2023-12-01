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

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
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
			@RequestBody CredentialsRequestDto credentialsRequestDto) {
		userService.unfollowUser(username, credentialsRequestDto);
	}

	@DeleteMapping("/@{username}")
	public UserResponseDto deleteCustomer(@PathVariable(value = "username") String username,
			@RequestBody UserRequestDto userRequestDto) {
		return userService.deleteUser(username, userRequestDto);
	}

	// devon's implement
//	@GetMapping("/@{username}/mentions")
//	public List<TweetResponseDto> getMentions(@PathVariable(value = "username") String username) {
//		return userService.getMentions(username);
//	}

//	@PostMapping("/@{username}/follow")
//	public void follow(@RequestBody CredentialsRequestDto credentialsRequestDto,
//			@PathVariable(value = "username") String username) {
//		userService.follow(credentialsRequestDto, username);
//	}
//

}
