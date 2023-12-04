package com.cooksys.socialmedia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByCredentialsUsername(String username);

    //List<User> findByFollowers(Long followers_id);
    
	Optional<User> findByCredentialsUsername(String username);

//	User dtoToEntity(UserRequestDto userRequestDto);
}
