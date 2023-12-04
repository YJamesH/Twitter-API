package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper { 

	TweetResponseDto entityToDto(Tweet tweet);
	List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> tweet);
	User requestDtoToEntity(TweetRequestDto tweetRequestDto);
	List<Tweet> requestDtosToEntities(List<TweetRequestDto> tweetRequestDtos);


	Tweet dtoToEntity(TweetRequestDto tweetRequestDto);
}
