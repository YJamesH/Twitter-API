package com.cooksys.socialmedia.mappers;


import com.cooksys.socialmedia.dtos.UserDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {
  @Mapping(target = "username", source = "credentials.username")

  UserResponseDto entityToDto(User user);
  List<UserResponseDto> entitiesToResponseDtos(List<User> users);
  User requestDtoToEntity(UserRequestDto userRequestDto);
  List<User> requestDtosToEntities(List<UserRequestDto> userRequestDtos);
     UserDto entityToResonseDto(User user);
}
