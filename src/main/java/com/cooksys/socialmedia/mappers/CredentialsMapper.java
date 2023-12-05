package com.cooksys.socialmedia.mappers;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	Credentials dtoToEntity(CredentialsDto currUserCreds);
}
