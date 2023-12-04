package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

//The username must be unique.
//The joined timestamp should be assigned when the user is first created, and must never be updated.
//A user’s profile information. Only the email property is required.
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @ManyToMany(mappedBy="followers", fetch = FetchType.EAGER)
    private List<User> following;

    @ManyToMany(fetch = FetchType.EAGER)
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