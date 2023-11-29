package com.cooksys.socialmedia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Credentials {
	@Column(nullable=false)
	private String userName;
	
	@Column(nullable=false)
	private String password;
}
