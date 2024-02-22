package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.entities.Hashtag;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

  Hashtag dtoToEntity(HashtagDto hashtagDto);

  HashtagDto entityToDto(Hashtag hashtag);

  Set<HashtagDto> entitiesToDto(Set<Hashtag> hashtags);

  Set<Hashtag> DtoToEntities(Set<HashtagDto> hashtagDtos);

}
