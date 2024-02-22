package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

  Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

  TweetResponseDto entityToResponseDto(Tweet tweet);

  Set<Tweet> requestDtosToEntities(Set<TweetRequestDto> tweetRequestDtos);

  Set<TweetResponseDto> entitiesToResponseDtos(Set<Tweet> tweets);

  List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> tweets);


}
