package com.cooksys.twitter_api.services;

public interface ValidateService {

    boolean tagExists(String label);

    boolean usernameExists(String username);

    boolean usernameAvailable(String username);
}
