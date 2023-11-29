package com.cooksys.socialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialmedia.entities.HashTag;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag,Long> {
    // we can have derived qureis if required
}
