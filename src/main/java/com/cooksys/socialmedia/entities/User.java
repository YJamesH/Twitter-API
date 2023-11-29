package com.cooksys.socialmedia.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

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
    private String userName;

    private String passWord;

   // @Column(nullable = false,updatable = false)
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Tweet> tweets;
}
