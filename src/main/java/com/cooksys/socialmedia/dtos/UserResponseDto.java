package com.cooksys.socialmedia.dtos;

import java.security.Timestamp;

import com.cooksys.socialmedia.entities.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
	private String username; //req
	private ProfileDto profile; //req
	private Timestamp joined; //req
}
