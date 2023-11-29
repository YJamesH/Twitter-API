package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;

public class TweetResponseDto {
    private Long id;

    private Integer author;// need to clarify datatype

    private Timestamp posted;

    private String content;

    private Long inReplyTo;// need to clarify datatype

    private Long repostOf;// need to clarify datatype
}
