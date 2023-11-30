package com.cooksys.socialmedia.mappers;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	HashtagResponseDto entityToDto(Hashtag tag);

}
