package com.cooksys.twitter_api.services.implementations;

import com.cooksys.twitter_api.dtos.*;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;
import com.cooksys.twitter_api.exceptions.NotFoundException;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.TweetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // endpoints

    @Override
    public Set<TweetResponseDto> getAllTweets() {
        Set<Tweet> tweets = tweetRepository.getByDeletedFalse(Sort.by("posted").descending());
        return tweetMapper.entitiesToResponseDtos(tweets);
    }

    @Override
    @Transactional
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        if (tweetRequestDto.getContent() == null) {
            throw new BadRequestException("Tweet content cannot be empty");
        }
        User author = areCredentialsValid(tweetRequestDto.getCredentials());
        // Map request to Entity.
        Tweet newTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
        newTweet.setAuthor(author);
        author.getTweets().add(newTweet);
        userRepository.save(author);

        // Process hashtags.
        processHashtags(newTweet);

        // Process mentions.
        processMentions(newTweet);

        // Save the tweet with all relationships set, then return it.
        return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(newTweet));
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        Optional<Tweet> requestedTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (requestedTweet.isEmpty()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }
        return tweetMapper.entityToResponseDto(requestedTweet.get());
    }

    @Override
    public Set<TweetResponseDto> getRepliesToTweet(Long id) {
        Optional<Tweet> originalTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (originalTweet.isEmpty()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }
        return tweetMapper.entitiesToResponseDtos(tweetRepository.findByInReplyToAndDeletedFalse(originalTweet.get(), Sort.by("posted").descending()));
    }

    @Override
    public Set<UserResponseDto> getUsersMentionedInTweet(Long id) {
        if (!tweetRepository.existsById(id)) {
            throw new NotFoundException("No tweet found with id: " + id);
        }

        Set<User> mentionedUsers = userRepository.findUserByDeletedIsFalseAndMentionsId(id);
        return userMapper.entitiesToResponseDtos(mentionedUsers);
    }

    @Override
    public Set<TweetResponseDto> getRepostsOfTweet(Long id) {
        Optional<Tweet> originalTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (originalTweet.isEmpty()) {
            throw new NotFoundException("Original tweet doesn't exist or was deleted.");
        }
        Optional<Set<Tweet>> reposts = tweetRepository.findByRepostOfAndDeletedFalse(originalTweet.get(), Sort.by("posted").descending());
        if (reposts.isEmpty()) {
            return new HashSet<>();
        }
        return tweetMapper.entitiesToResponseDtos(reposts.get());
    }

    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        User author = areCredentialsValid(credentialsDto);

        // Retrieve the original tweet or repost.
        Optional<Tweet> tweetToRepost = tweetRepository.findByIdAndDeletedFalse(id);
        if (tweetToRepost.isEmpty()) {
            throw new NotFoundException("Tweet being reposted doesn't exist");
        }

        // Trace back to the original tweet if it's a repost.
        Tweet originalTweet = tweetToRepost.get();
        while (originalTweet.getRepostOf() != null) {
            originalTweet = originalTweet.getRepostOf();
        }

        // Create a new tweet as a repost of the original tweet.
        Tweet repost = new Tweet();
        repost.setRepostOf(originalTweet);
        repost.setDeleted(false);
        repost.setAuthor(author);
        repost.setPosted(Timestamp.from(Instant.now()));
        repost.setInReplyTo(null);
        repost.setReplies(new HashSet<>());
        repost.setContent(null);

        // Save the repost tweet and return its data.
        Tweet savedRepost = tweetRepository.saveAndFlush(repost);
        return tweetMapper.entityToResponseDto(savedRepost);
    }

    @Override
    public ContextDto getTweetContext(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty() || optionalTweet.get().isDeleted()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }

        Tweet tweet = optionalTweet.get();

        List<Tweet> before = getTweetContextBefore(tweet);

        List<Tweet> after = new ArrayList<>();
        getTweetContextAfter(tweet, after);
        after.sort(Comparator.comparing(Tweet::getPosted));
        after.removeIf(Tweet::isDeleted);

        return new ContextDto(
                tweetMapper.entityToResponseDto(tweet),
                tweetMapper.entitiesToResponseDtos(before),
                tweetMapper.entitiesToResponseDtos(after)
        );
    }

    @Override
    public Set<UserResponseDto> getLikesOnTweet(Long id) {
        Optional<Tweet> tweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (tweet.isEmpty()) {
            throw new NotFoundException("Tweet doesn't exist or was deleted.");
        }
        return userMapper.entitiesToResponseDtos(tweet.get().getLikes().stream().filter(user -> !user.isDeleted()).collect(Collectors.toSet()));
    }

    @Override
    public Set<HashtagDto> getHashtagsOnTweet(Long id) {
        if (!tweetRepository.existsById(id)) {
            throw new NotFoundException("No tweet found with id: " + id);
        }


        Set<Hashtag> tweetHashtags = hashtagRepository.findHashtagsByTweetsId(id);
        for (Hashtag hashtag : tweetHashtags) {
            System.out.println(hashtag.getLabel());
        }
        return hashtagMapper.entitiesToDto(tweetHashtags);
    }

    @Override
    public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
        Optional<Tweet> tweetToDelete = tweetRepository.findById(id);
        if (tweetToDelete.isEmpty() || tweetToDelete.get().isDeleted()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }

        User user = areCredentialsValid(credentialsDto);

        tweetToDelete.get().setDeleted(true);
        return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(tweetToDelete.get()));
    }

    @Override
    public void likeTweet(Long id, CredentialsDto credentialsDto) {
        Optional<Tweet> likedTweet = tweetRepository.findById(id);
        if (likedTweet.isEmpty() || likedTweet.get().isDeleted()) {
            throw new NotFoundException("No tweet found with id: " + id);
        }

        User user = areCredentialsValid(credentialsDto);

        likedTweet.get().getLikes().add(user);
        tweetRepository.saveAndFlush(likedTweet.get());
    }

    @Override
    public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
        // Credentials validation
        User author = areCredentialsValid(tweetRequestDto.getCredentials());

        // Find the parent tweet, handling the case where it might not exist.
        Optional<Tweet> parentTweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (parentTweet.isEmpty()) {
            throw new NotFoundException("Tweet is either deleted or never existed.");
        }

        // Map the request DTO to a new Tweet entity and set its properties.
        Tweet newReplyTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
        newReplyTweet.setAuthor(author);
        newReplyTweet.setInReplyTo(parentTweet.get());

        // Process hashtags and mentions for the reply tweet.
        processHashtags(newReplyTweet);
        processMentions(newReplyTweet);

        // Save the new reply tweet and update the parent tweet with the reply.
        Tweet savedReplyTweet = tweetRepository.saveAndFlush(newReplyTweet);
        parentTweet.get().getReplies().add(savedReplyTweet);
        tweetRepository.saveAndFlush(parentTweet.get());

        // Convert the saved reply tweet to a DTO and return it.
        return tweetMapper.entityToResponseDto(savedReplyTweet);
    }

    // helper methods

    public User areCredentialsValid(CredentialsDto credentialsDto) {
        if (credentialsDto == null || credentialsDto.getPassword() == null) {
            throw new NotAuthorizedException("Credentials are required.");
        }
        Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(credentialsDto.getUsername());
        if (user.isEmpty()) {
            throw new NotAuthorizedException();
        }
        if (credentialsDto.getPassword().equals(user.get().getCredentials().getPassword())) {
            return user.get();
        } else {
            throw new NotAuthorizedException("Credentials are invalid.");
        }
    }

    private Set<String> extractHashtags(String content) {
        Pattern hashtagPattern = Pattern.compile("#[A-Za-z0-9]+(?:\\b|$)");
        Matcher hashtagMatcher = hashtagPattern.matcher(content);
        Set<String> allHashtagStrings = new HashSet<>();
        while (hashtagMatcher.find()) {
            allHashtagStrings.add(hashtagMatcher.group());
        }
        return allHashtagStrings;
    }

    private Set<String> extractMentions(String content) {
        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9]+)(?:\\b|$)");
        Matcher mentionMatcher = mentionPattern.matcher(content);
        Set<String> mentionStrings = new HashSet<>();
        while (mentionMatcher.find()) {
            mentionStrings.add(mentionMatcher.group(1));
        }
        return mentionStrings;
    }

    private void processHashtags(Tweet tweet) {
        String content = tweet.getContent();
        // Scan text for hashtags, such as #hashtags.
        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9]+)(?:\\b|$)");
        Matcher hashtagMatcher = hashtagPattern.matcher(content);
        // Add all hashtags to a set, which ensures no duplicate hashtags.
        Set<String> allHashtagStrings = new HashSet<>();
        while (hashtagMatcher.find()) {
            allHashtagStrings.add(hashtagMatcher.group(1));
        }

        // Sets for new and existing hashtags.
        Set<Hashtag> newHashtagEntities = new HashSet<>();
        Set<Hashtag> existingHashtags = new HashSet<>();
        for (String eachHashtag : allHashtagStrings) {
            // Check if they are already represented in the Repository,
            // and put them in one of the two lists.
            Optional<Hashtag> hashtagsDatabaseEntry = hashtagRepository.findByLabel(eachHashtag);
            if (hashtagsDatabaseEntry.isEmpty()) {
                // Build a new hashtag entity for new hashtags.
                Hashtag newHashtagEntity = new Hashtag();
                newHashtagEntity.setFirstUsed(Timestamp.from(Instant.now()));
                newHashtagEntity.setLastUsed(newHashtagEntity.getFirstUsed());
                newHashtagEntity.setLabel(eachHashtag);

                // Initialize the set of tweets for the new hashtag and add the current tweet.
                newHashtagEntity.setTweets(new HashSet<>());
                newHashtagEntity.getTweets().add(tweet);

                newHashtagEntities.add(newHashtagEntity); // Add to new hashtags list.
            } else {
                // Add existing hashtag to the list.
                Hashtag existingHashtag = hashtagsDatabaseEntry.get();
                existingHashtags.add(existingHashtag);
                // Also, add the current tweet to the existing hashtag's tweets.
                existingHashtag.getTweets().add(tweet);
            }
        }

        // Save all new hashtags in a batch.
        hashtagRepository.saveAll(newHashtagEntities);

        // Add all new and existing hashtags to the tweet's hashtags.
        tweet.getHashtags().addAll(newHashtagEntities);
        tweet.getHashtags().addAll(existingHashtags);

        // For hashtags IN the repository:
        for (Hashtag eachExistingHashtag : existingHashtags) {
            // Set the Hashtag->Tweets relationship.
            eachExistingHashtag.getTweets().add(tweet);
            // Update lastUsed timestamp.
            eachExistingHashtag.setLastUsed(Timestamp.from(Instant.now()));
            // Save the Hashtag into the repository.
            hashtagRepository.save(eachExistingHashtag);
        }
    }

    private void processMentions(Tweet tweet) {
        String content = tweet.getContent();
        // Scan content for Mentions, such as @ACoolUsername
        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9]+)(?:\\b|$)");
        Matcher mentionMatcher = mentionPattern.matcher(content);
        // Add all mentions to a set, which ensures no duplicate mentions.
        Set<String> mentionStrings = new HashSet<>();
        while (mentionMatcher.find()) {
            mentionStrings.add(mentionMatcher.group(1));
        }
        Set<User> mentionedExistingUsers = new HashSet<>();
        for (String eachMention : mentionStrings) {
            Optional<User> mentionedUser = userRepository.findByCredentialsUsernameAndDeletedFalse(eachMention);
            if (mentionedUser.isPresent()) {
                tweet.getMentions().add(mentionedUser.get());
                mentionedUser.get().getMentions().add(tweet);
                mentionedExistingUsers.add(mentionedUser.get());
            }
        }
        userRepository.saveAllAndFlush(mentionedExistingUsers);
    }

    private List<Tweet> getTweetContextBefore(Tweet tweet) {
        List<Tweet> before = new ArrayList<>();
        while (tweet.getInReplyTo() != null) {
            before.add(tweet.getInReplyTo());
            tweet = tweet.getInReplyTo();
        }
        before.removeIf(Tweet::isDeleted);
        return before;
    }

    private void getTweetContextAfter(Tweet tweet, List<Tweet> after) {
        if (!tweet.getReplies().isEmpty()) {
            after.addAll(tweet.getReplies());
            for (Tweet nextTweet : tweet.getReplies()) {
                getTweetContextAfter(nextTweet, after);
            }
        }
    }

}
