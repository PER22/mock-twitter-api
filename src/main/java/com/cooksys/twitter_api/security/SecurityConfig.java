package com.cooksys.twitter_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.cooksys.twitter_api.security.OAuth2AuthenticationSuccessHandler;
import com.cooksys.twitter_api.security.CustomOAuth2UserService;
import com.cooksys.twitter_api.security.JwtTokenProvider;

@Configuration
public class SecurityConfig {

    private final OAuth2AuthenticationSuccessHandler customOAuth2AuthSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(OAuth2AuthenticationSuccessHandler customOAuth2AuthSuccessHandler,
                          CustomOAuth2UserService customOAuth2UserService, JwtTokenProvider jwtTokenProvider) {
        this.customOAuth2AuthSuccessHandler = customOAuth2AuthSuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .cors(cors -> {}) // Enable CORS, configure further if needed
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth2/**").permitAll() // Permit all requests to auth and oauth2 endpoints
                        .anyRequest().authenticated() // Require authentication for any other request
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // Custom login page
                        .permitAll() // Allow access to the custom login page
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login") // Custom login page for OAuth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Custom service for Google OAuth2
                        )
                        .successHandler(customOAuth2AuthSuccessHandler) // Handle JWT after OAuth login
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
