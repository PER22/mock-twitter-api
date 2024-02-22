package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;

    @Override
    public boolean tagExists(String label) {
        return hashtagRepository.existsHashtagByLabel(label);
    }

    @Override
    public boolean usernameExists(String username) {

        return userRepository.existsByCredentialsUsername(username);
    }

    @Override
    public boolean usernameAvailable(String username) {
        return !userRepository.existsByCredentialsUsername(username);
    }

}
