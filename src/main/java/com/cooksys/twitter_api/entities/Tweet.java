package com.cooksys.twitter_api.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude =
        {"author", "inReplyTo", "replies", "repostOf", "reposts", "hashtags", "likes", "mentions"})

public class Tweet {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @CreationTimestamp
  @Column(nullable = false)
  private Timestamp posted;

  private boolean deleted = false;

  private String content;

  @ManyToOne
  @JoinColumn(name = "`inReplyTo_id`")
  private Tweet inReplyTo;

  @OneToMany(mappedBy = "inReplyTo")
  private Set<Tweet> replies = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "`repostOf_id`")
  private Tweet repostOf;

  @OneToMany(mappedBy = "repostOf")
  private Set<Tweet> reposts = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "tweet_hashtags",
          joinColumns = @JoinColumn(name = "tweet_id"),
          inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
  private Set<Hashtag> hashtags = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "user_likes",
          joinColumns = @JoinColumn(name = "tweet_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> likes = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "user_mentions",
          joinColumns = @JoinColumn(name = "tweet_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> mentions = new HashSet<>();

}
