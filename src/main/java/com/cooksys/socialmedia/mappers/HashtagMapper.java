package com.cooksys.socialmedia.mappers;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	HashtagResponseDto entityToDto(Hashtag tag);

	List<HashtagResponseDto> entitiesToResponseDtos (List<Hashtag> hashtags);
	Hashtag requestDtoToEntity(HashtagDto hashtagRequestDto);
	List<HashtagResponseDto> requestDtosToEntities(List<HashtagDto> hashtagRequestDtos);

}
