package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;

public interface HashtagService {


	List<HashtagResponseDto> getAllTags();

    List<TweetResponseDto> getTweetsByLabel(String label);
}
