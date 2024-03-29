package com.cooksys.socialmedia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.User;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

    Hashtag findHashtagByLabel(String label);
	
    Optional<Hashtag> findByLabel(String label);

}
