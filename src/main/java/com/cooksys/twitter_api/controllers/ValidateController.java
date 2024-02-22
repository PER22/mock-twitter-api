package com.cooksys.twitter_api.controllers;

import com.cooksys.twitter_api.services.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {

    private final ValidateService validateService;

    @GetMapping("/tag/exists/{label}")
    public boolean validateTagExists(@PathVariable String label) {
        return validateService.tagExists(label);
    }

    @GetMapping("/username/exists/@{username}")
    public boolean validateUsernameExists(@PathVariable String username) {
        return validateService.usernameExists(username);
    }

    @GetMapping("/username/available/@{username}")
    public boolean validateUsernameAvailable(@PathVariable String username) {
        return validateService.usernameAvailable(username);
    }
}
