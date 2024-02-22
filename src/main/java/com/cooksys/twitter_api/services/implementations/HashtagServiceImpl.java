package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    @Override
    public Set<HashtagDto> getAllHashtags() {
        return hashtagMapper.entitiesToDto(new HashSet<>(hashtagRepository.findAll()));
    }

    @Override
    public Set<TweetResponseDto> getTweetsByHashtag(String label) {
        if (!hashtagRepository.existsHashtagByLabel(label)) {
            throw new NotFoundException("No hashtag found with label: " + label);
        }

        return tweetMapper.entitiesToResponseDtos(tweetRepository.findByHashtags_LabelAndDeletedFalse(label, Sort.by("posted").descending()));
    }
}
