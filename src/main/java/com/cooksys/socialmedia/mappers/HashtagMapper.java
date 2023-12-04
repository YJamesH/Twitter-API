package com.cooksys.socialmedia.mappers;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;


@Mapper(componentModel = "spring", uses = { TweetMapper.class })
public interface HashtagMapper {
	List<HashtagResponseDto> entitiesToHashtagDtos(List<Hashtag> entities);
	List<HashtagResponseDto> entitiesToResponseDtos (List<Hashtag> hashtags);
	Hashtag requestDtoToEntity(HashtagDto hashtagRequestDto);
	List<HashtagResponseDto> requestDtosToEntities(List<HashtagDto> hashtagRequestDtos);

}
