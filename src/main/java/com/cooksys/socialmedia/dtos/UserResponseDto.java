package com.cooksys.socialmedia.dtos;

import java.security.Timestamp;

import com.cooksys.socialmedia.entities.Profile;

public class UserResponseDto {
	private String username; //req
	private Profile profile; //req
	private Timestamp joined; //req
}
