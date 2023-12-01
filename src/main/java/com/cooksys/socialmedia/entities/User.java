package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    
    
	@Column(nullable = false)
	@CreationTimestamp
    private Timestamp joined;

    private boolean deleted;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

    @ManyToMany(mappedBy="userLikes")
    private List<Tweet> tweetLikes;
    
    @ManyToMany(mappedBy="followers")
    private List<User> following;
    
    @ManyToMany
    @JoinTable(name="followers_following")
    private List<User> followers;
    
    @ManyToMany
    @JoinTable(name="user_mentions")
    private List<Tweet> tweetMentions;
    

    @Embedded
    private Profile profile;
    
    @Embedded
    private Credentials credentials;
    
    public User(Timestamp joined) {
        this.joined = joined;
    }

}
