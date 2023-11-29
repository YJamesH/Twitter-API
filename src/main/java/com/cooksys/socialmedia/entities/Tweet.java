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

    @JoinColumn
    private User author;// need to clarify datatype

    private Timestamp posted;

    private boolean deleted;

    private String content;

    @ManyToMany
    private List<User> mentionedUsers;

    @ManyToMany
	@JoinTable(name="user_likes",
	joinColumns ={@JoinColumn(name ="tweet_id")},
	inverseJoinColumns ={@JoinColumn(name="user_id")})
    private List<User> users;
    
    
    private Tweet inReplyTo;// need to clarify datatype

    @ManyToOne
    private Tweet repostOfOriginal;// need to clarify datatype
    
    @OneToMany(mappedBy="repostOfOriginal")
    private List<Tweet> repostOfList;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;
    
    private List<Hashtag> hashtags;



}
