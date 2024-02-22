package com.cooksys.twitter_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "tweets")
public class Hashtag {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String label;

  @CreationTimestamp
  @Column(name = "`firstUsed`", nullable = false)
  private Timestamp firstUsed;

  @UpdateTimestamp
  @Column(name = "`lastUsed`", nullable = false)
  private Timestamp lastUsed;

  @ManyToMany(mappedBy = "hashtags")
  private Set<Tweet> tweets = new HashSet<>();

}
