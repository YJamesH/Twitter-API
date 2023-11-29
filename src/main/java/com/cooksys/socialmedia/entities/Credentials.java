package com.cooksys.socialmedia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Credentials {

    @Column(unique = true, nullable=false)
	private String username;
	
	//@Column(nullable=false)
	private String password;
}
