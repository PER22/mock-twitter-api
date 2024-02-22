package com.cooksys.twitter_api.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
@Data
public class Credentials {

  @Column(nullable = false, unique = true)
  public String username;

  @Column(nullable = false)
  public String password;

}
