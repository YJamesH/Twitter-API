package com.cooksys.socialmedia.mappers;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

  @Mapping(target = "username", source = "credentials.username")
  UserResponseDto entityToDto(User user);

}
