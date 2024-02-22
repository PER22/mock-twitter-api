package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping
    public ResponseEntity<Set<HashtagDto>> getAllHashtags() {
        return ResponseEntity.ok(hashtagService.getAllHashtags());
    }

    @GetMapping("/{label}")
    public ResponseEntity<Set<TweetResponseDto>> getTweetsByHashtag(@PathVariable String label) {
        return ResponseEntity.ok(hashtagService.getTweetsByHashtag(label));
    }

}
