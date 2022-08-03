package com.cts.tweetapp.service;

import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.exception.InvalidUsernameException;
import com.cts.tweetapp.model.Comments;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.repository.CommentsRepository;
import com.cts.tweetapp.repository.TweetRepository;
import com.cts.tweetapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TweetService {

    @Autowired
    UserService userService;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    public boolean isUsernamePresent(String username) {
        return userRepository.findByUsername(username) != null ? true : false;
    }

    public boolean isTweetPresent(int id) {

        return tweetRepository.findById(id) != null ? true : false;
    }

    // for post and update tweet
    public void proceedTweet(ConsumerRecord<Integer, String> consumerRecord) throws JsonMappingException, JsonProcessingException, InvalidUsernameException {
        Tweet tweet =objectMapper.readValue(consumerRecord.value(),Tweet.class);
        log.info("tweet {}",tweet);
        switch (tweet.getTweetType()) {
            case NEW:
                saveMethod(tweet);
                break;
            case UPDATE:
                validate(tweet);
                saveMethod(tweet);
                break;

            default:
                log.error("not valid data");
                break;
        }

    }

    private void validate(Tweet tweet) throws InvalidUsernameException {
        Optional<Tweet> tweetOptional=tweetRepository.findById(tweet.getId());
        if(tweetOptional.isEmpty()) {
            throw new IllegalArgumentException("tweet id is not valid");
        }
        if(tweet.getUsername().equals(tweetOptional.get().getUsername())) {
            tweet.setId(tweetOptional.get().getId());
            tweet.setUsername(tweetOptional.get().getUsername());
            log.info("Validation is successful for the tweet : {} ", tweetOptional.get());
        }
        else{
            throw new InvalidUsernameException("Invalid username");
        }

    }

    private void saveMethod(Tweet tweet) {
        tweetRepository.save(tweet);
        log.info("Successfully Persisted the tweet {} ", tweet);



    }


    public Tweet likes(Tweet tweet) {
        tweet.setLikes(tweet.getLikes() + 1);
        return tweetRepository.save(tweet);
    }

    public void deleteTweet(Tweet tweet) {
        tweetRepository.delete(tweet);
    }

    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    public List<Tweet> getAllTweetsByUsername(String username) {
        return tweetRepository.findByUsername(username);
    }


    public void deleteTweetById(int tweetId,String username) throws Exception_Tweet {
        if(isTweetPresent(tweetId)){
            tweetRepository.deleteById(tweetId);
        }
        throw new Exception_Tweet("Tweet is not present for this id.");

    }

    public Comments replyTweetById(int id, Comments comments,String username) throws Exception_Tweet {
        if (isTweetPresent(id)) {
            comments.setTweetId(id);
            comments.setUsername(username);
            commentsRepository.save(comments);
        } else {
            throw new Exception_Tweet("Incorrect or deleted tweet id.");
        }
        return comments;
    }

    public void likeTweetById(int tweetId) {
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if (tweet.isPresent()) {
            tweet.get().setLikes(tweet.get().getLikes() + 1);
            tweetRepository.save(tweet.get());
        }

    }

}
