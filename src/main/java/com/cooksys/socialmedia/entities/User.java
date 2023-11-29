package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

//The username must be unique.
//The joined timestamp should be assigned when the user is first created, and must never be updated.
//A user’s profile information. Only the email property is required.
@Table(name = "user_table")
@NoArgsConstructor
@Data
@Entity

public class User {
    @Id
    @GeneratedValue
    private Long id;


    @Column(unique = true)
    private String username;

    private String password;

    @Column(nullable = false,updatable = false)
    private Timestamp joined;

    private boolean deleted;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phone;

    public User(Timestamp joined) {
        this.joined = joined;
    }

    
    @ManyToMany
	@JoinTable(name="user_likes",
	joinColumns ={@JoinColumn(name ="user_Id")},
	inverseJoinColumns ={@JoinColumn(name="tweet_id")})
    private List<Tweet> tweets;
    
    private List<Tweet> userLikes;
    
    private List<User> following;
    
    private List<User> followers;
    
    @ManyToMany(mappedBy="mentionedUsers")
    private List<Tweet> mentionedTweets;
    
    @Embedded
    private Profile profile;
    
    @Embedded
    private Credentials credentials;
}
