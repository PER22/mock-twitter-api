package com.cooksys.twitter_api.repositories;

import com.cooksys.twitter_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCredentialsUsernameAndDeletedFalse(String username);

    Optional<Set<User>> getUsersByDeletedIsFalse();

    boolean existsByCredentialsUsername(String username);

    Set<User> findUserByDeletedIsFalseAndMentionsId(Long id);

    Optional<User> findByCredentialsUsername(String desiredUsername);
}

