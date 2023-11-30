package com.cooksys.socialmedia.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.HashtagResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	
	@Override
	public List<HashtagResponseDto> getAllTags() {
		List<Hashtag> tags = hashtagRepository.findAll();
		List<HashtagResponseDto> toReturn = new ArrayList<HashtagResponseDto>();
		for (Hashtag tag : tags) {
			toReturn.add(hashtagMapper.entityToDto(tag));
		}
		
		return toReturn;
	}
	
}
