package com.cooksys.twitter_api.entities;

import com.cooksys.twitter_api.embeddables.Credentials;
import com.cooksys.twitter_api.embeddables.Profile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.cooksys.twitter_api.entities.enums.AuthProvider;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_table")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"tweets", "likes", "mentions", "followers", "following"})
public class User {

  @Id
  @GeneratedValue
  private Long id;

  @CreationTimestamp
  @Column(nullable = false)
  private Timestamp joined;

  private boolean deleted = false;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AuthProvider authProvider;  // Tracks the authentication method used

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "username", column = @Column(nullable = false, unique = true)),
          @AttributeOverride(name = "password", column = @Column(nullable = true))  // nullable for OAuth users
  })
  private Credentials credentials;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "firstName", column = @Column(name = "`firstName`")),
          @AttributeOverride(name = "lastName", column = @Column(name = "`lastName`")),
          @AttributeOverride(name = "email", column = @Column(nullable = false))
  })
  private Profile profile;

  @OneToMany(mappedBy = "author")
  private Set<Tweet> tweets = new HashSet<>();

  @ManyToMany(mappedBy = "likes")
  private Set<Tweet> likes = new HashSet<>();

  @ManyToMany(mappedBy = "mentions")
  private Set<Tweet> mentions = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "followers_following",
          joinColumns = @JoinColumn(name = "follower_id"),
          inverseJoinColumns = @JoinColumn(name = "following_id"))
  private Set<User> followers = new HashSet<>();

  @ManyToMany(mappedBy = "followers")
  private Set<User> following = new HashSet<>();
}
