package com.cooksys.twitter_api.mappers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.embeddables.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

  Credentials dtoToEntity(CredentialsDto credentialsDto);

  CredentialsDto entityToDto(Credentials credentials);

}
