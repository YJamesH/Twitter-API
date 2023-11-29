package com.cooksys.socialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTag extends JpaRepository<HashTag,Long> {
    // we can have derived qureis if required
}
