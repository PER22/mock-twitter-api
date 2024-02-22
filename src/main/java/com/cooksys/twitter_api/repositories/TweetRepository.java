package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.Tweet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.Optional;


@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    Set<Tweet> findByInReplyToAndDeletedFalse(Tweet parentTweet, Sort sort);
    Set<Tweet> getByDeletedFalse(Sort sort);
    Set<Tweet> findByHashtags_LabelAndDeletedFalse(String hashtagLabel, Sort sort);
    Optional<Tweet> findByIdAndDeletedFalse(Long id);
    Optional<Set<Tweet>> findByRepostOfAndDeletedFalse(Tweet tweet, Sort sort);
}
