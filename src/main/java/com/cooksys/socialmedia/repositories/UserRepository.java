package com.cooksys.socialmedia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialmedia.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByCredentialsUsername(String username);

}
