package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//A tweet posted by a user.

@NoArgsConstructor
@Data
@Entity
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private User author;

    private Timestamp posted;

    private boolean deleted;

    private String content;

    @ManyToMany
    private List<User> mentionedUsers;

    // Users who have liked the tweet
    @ManyToMany
    private List<User> users;
    
    @ManyToOne
    @JoinColumn
    private Tweet inReplyTo;

    @ManyToOne
    @JoinColumn
    private Tweet repostOfOriginal;
    
    //@OneToMany(mappedBy="repostOfOriginal")
    //private List<Tweet> repostOfList;

    @ManyToMany
    private List<Hashtag> hashtags;



}
