package com.cooksys.socialmedia;

import org.springframework.boot.CommandLineRunner;

import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.repositories.UserRepository;

public class SocialmediaSeeder implements CommandLineRunner {
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setFirstName("a");
		user.setLastName("b");
		user.setUsername("asd");
		user.setPassword("123");
		user.setJoined(null);
		user.setEmail("123");
		user.setPhone("1234");
		
		userRepository.saveAndFlush(user);
		System.out.println(userRepository.findAll());
	}
}