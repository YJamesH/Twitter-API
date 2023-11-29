package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    
    @Column(/*nullable = false,*/updatable = false)
    private Timestamp joined;

    private boolean deleted;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

    @ManyToMany(mappedBy="users")
    private List<Tweet> userLikes;
    
    @ManyToMany(mappedBy="followers")
    private List<User> following;
    
    @ManyToMany
    private List<User> followers;
    
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
