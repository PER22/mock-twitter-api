package com.cooksys.twitter_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 8011499701505794149L;

  private String message;

}
