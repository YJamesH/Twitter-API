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

    @ManyToOne
    //@JoinColumn
    private User author;

    private Timestamp posted;

    private boolean deleted;

    private String content;

            //  ( user_mentiones ) table  (mentionedUsers for the tweet)

    @ManyToMany
    @JoinTable(
            name = "user_mentiones",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> mentionedUsers;

                // Users who have liked the tweet

    @ManyToMany(mappedBy = "likes")
    private List<User> users;

                // Tweet  - (Tweet) inReplyTo;
    @ManyToOne
    //@JoinColumn
    private Tweet inReplyTo;

            // Tweet - (Tweet) repostOfOriginal
    @ManyToOne
    //@JoinColumn
    private Tweet repostOfOriginal;
    
    //@OneToMany(mappedBy="repostOfOriginal") // not reequired
    //private List<Tweet> repostOfList;// not required

                // (tweet - hashtag) join table
    @ManyToMany
    private List<Hashtag> hashtags;

}
