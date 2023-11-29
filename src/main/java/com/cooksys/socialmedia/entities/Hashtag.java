package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

//The label property must be unique, but is case-insensitive.
//The firstUsed timestamp should be assigned on creation, and must never be updated.
//The lastUsed timestamp should be updated every time a new tweet is tagged with the hashtag.

@NoArgsConstructor
@Data
@Entity
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String label;

    // should not have setter ?
    // @Column(updatable = false) enough ?
    // need to clarify
    @Column(updatable = false)
    private Timestamp firstUsed;

    private Timestamp lastUsed;

    public Hashtag(Timestamp firstUsed) {
        this.firstUsed = firstUsed;
    }
    
    @ManyToMany(mappedBy="hashtags")
    private List<Tweet> tweets;
}
