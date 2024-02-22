package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {

  private String content;
  private CredentialsDto credentials;

}
