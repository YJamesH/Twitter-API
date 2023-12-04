package com.cooksys.socialmedia.mappers;

import java.util.List;

import com.cooksys.socialmedia.dtos.UserDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {
  
	List<UserResponseDto> entitiesToResponseDtos(List<User> users);
	
	List<User> requestDtosToEntities(List<UserRequestDto> userRequestDtos);
	
    UserDto entityToResponseDto(User user);

	@Mapping(target = "username", source = "credentials.username")
	UserResponseDto entityToDto(User user);

	List<TweetResponseDto> entitiesToTweetDtos(List<User> entities);

	List<UserResponseDto> entitiesToUserDtos(List<User> entities);

	User dtoToEntity(UserRequestDto userRequestDto);
 
}
