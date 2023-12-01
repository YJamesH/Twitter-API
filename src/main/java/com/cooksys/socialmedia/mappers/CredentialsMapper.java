package com.cooksys.socialmedia.mappers;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.CredentialsRequestDto;
import com.cooksys.socialmedia.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

	Credentials dtoToEntity(CredentialsRequestDto credentialsRequestDto);


}
