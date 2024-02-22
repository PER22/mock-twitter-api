package com.cooksys.twitter_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileDto {

  public String firstName;
  public String lastName;
  public String email;
  public String phone;

}
