package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {

  private CredentialsDto credentials;
  private ProfileDto profile;

}
