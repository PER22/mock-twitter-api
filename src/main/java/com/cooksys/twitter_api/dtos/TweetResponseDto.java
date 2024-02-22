package com.cooksys.twitter_api.dtos;

import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class TweetResponseDto {

  private Long id;
  private UserResponseDto author;
  private Timestamp posted;
  private String content;
  private TweetResponseDto inReplyTo;
  private TweetResponseDto repostOf;

}
