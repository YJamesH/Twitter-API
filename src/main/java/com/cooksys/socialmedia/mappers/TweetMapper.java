package com.cooksys.socialmedia.mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

}
