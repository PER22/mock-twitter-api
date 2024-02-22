package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

  Optional<Hashtag> findByLabel(String label);

  boolean existsHashtagByLabel(String label);

  Set<Hashtag> findHashtagsByTweetsId(Long id);

}

