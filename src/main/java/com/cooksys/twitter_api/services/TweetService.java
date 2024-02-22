package com.cooksys.twitter_api.services;

import com.cooksys.twitter_api.dtos.*;

import java.util.Set;

public interface TweetService {

    Set<TweetResponseDto> getAllTweets();

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto getTweetById(Long id);

    Set<TweetResponseDto> getRepliesToTweet(Long id);

    Set<UserResponseDto> getUsersMentionedInTweet(Long id);

    Set<TweetResponseDto> getRepostsOfTweet(Long id);

    TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);

    ContextDto getTweetContext(Long id);

    Set<UserResponseDto> getLikesOnTweet(Long id);

    Set<HashtagDto> getHashtagsOnTweet(Long id);

    TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto);

    void likeTweet(Long id, CredentialsDto credentialsDto);

    TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto);

}
