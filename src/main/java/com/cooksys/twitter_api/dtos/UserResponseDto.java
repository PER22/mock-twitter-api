package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.cooksys.twitter_api.embeddables.Profile;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserResponseDto {

  public String username;
  public ProfileDto profile;
  public Timestamp joined;

}
