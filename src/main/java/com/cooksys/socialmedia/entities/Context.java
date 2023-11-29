package com.cooksys.socialmedia.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Context {
	
	@Column(nullable=false)
	private Tweet currTweet;
	
	@Column(nullable=false)
	private List<Tweet> beforeTweets;
	
	@Column(nullable=false)
	private List<Tweet> afterTweets;
}
