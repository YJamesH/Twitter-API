package com.cooksys.socialmedia;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
	
	private UserRepository userRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
		User user = new User();
		
		user.setFirstName("a");
		user.setLastName("b");
		user.setUserName("user");
		user.setPassWord("pass");
		user.setJoined(null);
		user.setDeleted(false);
		user.setEmail("email");
		user.setPhone("123");
		
		userRepository.saveAndFlush(user);
	}

	

}
