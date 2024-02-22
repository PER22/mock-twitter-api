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
public class BadRequestException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 2094794981553153179L;

  private String message;

}
