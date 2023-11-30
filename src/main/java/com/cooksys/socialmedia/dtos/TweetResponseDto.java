package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;

import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;

    private UserResponseDto author;// need to clarify datatype

    private Timestamp posted;

    private String content;

    private TweetResponseDto inReplyTo;// need to clarify datatype

    private TweetResponseDto repostOf;// need to clarify datatype
}
