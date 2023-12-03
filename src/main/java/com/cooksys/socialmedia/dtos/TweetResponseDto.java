package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;
import java.util.List;

import com.cooksys.socialmedia.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TweetResponseDto {
    private Long id;

    private UserResponseDto author;

    private Timestamp posted;

    private String content;

    private TweetResponseDto inReplyTo;

    private TweetResponseDto repostOf;

    private List<User> userMentions;
}
