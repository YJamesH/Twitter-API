package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;


@Mapper(componentModel = "spring", uses = { TweetMapper.class })
public interface HashtagMapper {
	List<HashtagResponseDto> entitiesToHashtagDtos(List<Hashtag> entities);
	List<HashtagResponseDto> entitiesToResponseDtos (List<Hashtag> hashtags);
//	Hashtag requestDtoToEntity(HashtagDto hashtagRequestDto);
//	List<HashtagResponseDto> requestDtosToEntities(List<HashtagDto> hashtagRequestDtos);

}
