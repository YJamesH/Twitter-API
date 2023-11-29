package com.cooksys.socialmedia.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

//A tweet posted by a user.

@NoArgsConstructor
@Data
@Entity
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    private Integer author;// need to clarify datatype

    private Timestamp posted;

    private boolean deleted;

    private String content;

    private Long inReplyTo;// need to clarify datatype

    private Long repostOf;// need to clarify datatype

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;


}
