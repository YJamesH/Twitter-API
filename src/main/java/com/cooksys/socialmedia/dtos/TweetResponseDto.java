package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;

import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;

public class TweetResponseDto {
    private Integer id;

    private User author;// need to clarify datatype

    private Timestamp posted;

    private String content;

    private Tweet inReplyTo;// need to clarify datatype

    private Tweet repostOf;// need to clarify datatype
}
