package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserRequestDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.createUser(userRequestDto.getCredentials(), userRequestDto.getProfile()));
    }

    @GetMapping("/@{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PatchMapping("/@{username}")
    public ResponseEntity<UserResponseDto> updateUserProfile(
            @PathVariable String username,
            @RequestBody UserRequestDto userRequestDto
    ) {
        return ResponseEntity.ok(userService.updateUserProfile(username, userRequestDto));
    }

    @DeleteMapping("/@{username}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(userService.deleteUser(username, credentials));
    }

    @PostMapping("/@{username}/follow")
    public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        userService.followUser(username, credentials);
    }

    @PostMapping("/@{username}/unfollow")
    public void unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentials) {
        userService.unfollowUser(username, credentials);
    }

    @GetMapping("/@{username}/feed")
    public ResponseEntity<List<TweetResponseDto>> getUserFeed(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserFeed(username));
    }

    @GetMapping("/@{username}/tweets")
    public ResponseEntity<List<TweetResponseDto>> getUserTweets(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserTweets(username));
    }

    @GetMapping("/@{username}/mentions")
    public ResponseEntity<List<TweetResponseDto>> getMentions(@PathVariable String username) {
        return ResponseEntity.ok(userService.getMentions(username));
    }

    @GetMapping("/@{username}/followers")
    public ResponseEntity<Set<UserResponseDto>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowers(username));
    }

    @GetMapping("/@{username}/following")
    public ResponseEntity<Set<UserResponseDto>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowing(username));
    }

}
