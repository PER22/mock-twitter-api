package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;

import java.util.Set;

public interface HashtagService {

    Set<HashtagDto> getAllHashtags();

    Set<TweetResponseDto> getTweetsByHashtag(String label);
}