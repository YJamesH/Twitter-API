package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;
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
    
    @Column(/*nullable = false,*/updatable = false)
    private Timestamp joined;

    private boolean deleted;
            //**** user - Tweeter mapping
    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

            //*****user_likes
    @ManyToMany//(mappedBy="users")
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> likes;

            //******* followers following join table
    @ManyToMany(mappedBy="followers")
    private List<User> following;
    
    @ManyToMany
    @JoinTable(
            name = "followers_following",
            joinColumns = @JoinColumn(name = "followers_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<User> followers;

        // **** ( user_mentiones ) table  (mentionedUsers for the tweet)

    @ManyToMany(mappedBy="mentionedUsers")
    private List<Tweet> mentionedTweets;
    

    @Embedded
    @AttributeOverrides({
    	@AttributeOverride(name="firstName", column=@Column(name = "user_firstName")),
    	@AttributeOverride(name="lastName", column=@Column(name = "user_lastName")),
    	@AttributeOverride(name="email", column=@Column(name = "user_email")),
    	@AttributeOverride(name="phone", column=@Column(name = "user_phone"))
    })
    private Profile profile;
    
    @Embedded
    private Credentials credentials;
    
    public User(Timestamp joined) {
        this.joined = joined;
    }
}
