package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private User author;

    @CreationTimestamp
    private Timestamp posted;

    private boolean deleted;

    private String content;

    @ManyToMany
    private List<User> mentionedUsers;

    // Users who have liked the tweet
    @ManyToMany
    private List<User> userLikes;
    
    @ManyToOne
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> replies = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private Tweet repostOfOriginal;
    
    @OneToMany(mappedBy = "repostOfOriginal")
    private List<Tweet> reposts = new ArrayList<>();

    @ManyToMany
    private List<Hashtag> hashtags;



}
