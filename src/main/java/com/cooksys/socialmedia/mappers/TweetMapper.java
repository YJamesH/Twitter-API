package com.cooksys.socialmedia.mappers;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.entities.User;
import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper { 

	TweetResponseDto entityToDto(Tweet tweet);
	List<TweetResponseDto> entitiesToResponseDtos (List<Tweet> tweet);
	User requestDtoToEntity(TweetRequestDto tweetRequestDto);
	List<Tweet> requestDtosToEntities(List<TweetRequestDto> tweetRequestDtos);

	List<TweetResponseDto> entitiesToTweetDtos(List<Tweet> entities);

	Tweet dtoToEntity(TweetRequestDto tweetRequestDto);
}
