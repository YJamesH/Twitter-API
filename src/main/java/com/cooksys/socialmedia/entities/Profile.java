package com.cooksys.socialmedia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Profile {

	private String firstName;

	private String lastName;

	@Column(nullable=false)
	private String email;
	
	private String phone;
	
}
