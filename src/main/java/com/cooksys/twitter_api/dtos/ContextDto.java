package com.cooksys.twitter_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContextDto {

  public TweetResponseDto target;
  public List<TweetResponseDto> before;
  public List<TweetResponseDto> after;

}
