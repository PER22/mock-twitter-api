package com.cooksys.twitter_api.entities;

import com.cooksys.twitter_api.embeddables.Credentials;
import com.cooksys.twitter_api.embeddables.Profile;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Table(name = "user_table")
@Entity
@NoArgsConstructor
@Getter
@Setter
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

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "username", column = @Column(nullable = false, unique = true)),
          @AttributeOverride(name = "password", column = @Column(nullable = false))
  })
  private Credentials credentials;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "firstName", column = @Column(name="`firstName`")),
          @AttributeOverride(name = "lastName", column = @Column(name="`lastName`")),
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
