package com.cooksys.socialmedia.dtos;

import java.util.List;

import com.cooksys.socialmedia.entities.Tweet;

public class ContextDto {
	private Tweet target;
	private List<Tweet> before;
	private List<Tweet> after;
}
