package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;


    @GetMapping
    public ResponseEntity<Set<TweetResponseDto>> getAllTweets(){
        return ResponseEntity.ok(tweetService.getAllTweets());
    }

    @PostMapping
    public ResponseEntity<TweetResponseDto> createTweet(@RequestBody TweetRequestDto tweetRequestDto){
        return ResponseEntity
                .created(URI.create(""))
                .body(tweetService.createTweet(tweetRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseDto> getTweetById(@PathVariable Long id){
        return ResponseEntity.ok(tweetService.getTweetById(id));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<Set<TweetResponseDto>> getRepliesToTweet(@PathVariable Long id){
        return ResponseEntity.ok(tweetService.getRepliesToTweet(id));
    }

    @GetMapping("/{id}/mentions")
    public ResponseEntity<Set<UserResponseDto>> getUsersMentionedInTweet(@PathVariable Long id){
        return ResponseEntity.ok(tweetService.getUsersMentionedInTweet(id));
    }

    @GetMapping("/{id}/reposts")
    public ResponseEntity<Set<TweetResponseDto>> getRepostsOfTweet(@PathVariable Long id){
        return ResponseEntity.ok(tweetService.getRepostsOfTweet(id));
    }

    @PostMapping("/{id}/repost")
    public ResponseEntity<TweetResponseDto> repostTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto){
        return ResponseEntity.ok(tweetService.repostTweet(id, credentialsDto));
    }

    @GetMapping("/{id}/context")
    public ResponseEntity<ContextDto> getTweetContext(@PathVariable Long id){
        return ResponseEntity.ok(tweetService.getTweetContext(id));
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<Set<UserResponseDto>> getLikesOnTweet(@PathVariable Long id){
        return ResponseEntity.ok(tweetService.getLikesOnTweet(id));
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Set<HashtagDto>> getHashtagsOnTweet(@PathVariable Long id){
        return ResponseEntity.ok(tweetService.getHashtagsOnTweet(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TweetResponseDto> deleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto){
        return ResponseEntity.ok(tweetService.deleteTweet(id, credentialsDto));
    }

    @PostMapping("/{id}/like")
    public void likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto){
        tweetService.likeTweet(id, credentialsDto);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<TweetResponseDto> replyToTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto){
        return ResponseEntity.ok(tweetService.replyToTweet(id, tweetRequestDto));
    }
}
