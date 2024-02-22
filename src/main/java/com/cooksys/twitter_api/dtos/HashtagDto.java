package com.cooksys.twitter_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class HashtagDto {

  public String label;
  public Timestamp firstUsed;
  public Timestamp lastUsed;

}
