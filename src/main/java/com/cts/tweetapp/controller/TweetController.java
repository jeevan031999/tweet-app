package com.cts.tweetapp.controller;

import com.cts.tweetapp.Kafka.producer.TweetAppProducer;
import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.model.Comments;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.model.TweetType;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.TweetService;
import com.cts.tweetapp.service.UserService;
import com.cts.tweetapp.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cts.tweetapp.constants.Constants.*;
import static com.cts.tweetapp.model.Comments.SEQ_NAME;
import static com.cts.tweetapp.model.Tweet.SEQUENCE_NAME;

@RestController
@RequestMapping(value = BASE_URL)
@Slf4j
public class TweetController {
    @Autowired
    private TweetService tweetService;

    @Autowired
    UserService userService;
    @Autowired
    TweetAppProducer tweetAppProducer;

    @Autowired
    private JwtUtil jwtUtilToken;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @PostMapping(value = ADD_TWEET)
    public ResponseEntity<?> addTweet(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @RequestBody Tweet tweet) throws JsonProcessingException {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            tweet.setTweetType(TweetType.NEW);
            tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
            tweet.setUsername(username);
            tweetAppProducer.posttweet(tweet);

            return ResponseEntity.status(HttpStatus.CREATED).body(tweet);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }

    }
    @PutMapping(value = EDIT_TWEET)
    public ResponseEntity<?> updatetweet(@PathVariable("username") String username, @PathVariable("id") int id, @RequestHeader("Authorization") String authorization, @RequestBody Tweet tweet) throws JsonProcessingException {
        String token = authorization.substring(7);
        String uname = jwtUtilToken.getUsernameFromToken(token);
        if (username.equals(uname)) {
            tweet.setTweetType(TweetType.UPDATE);
            tweet.setId(id);
            tweet.setUsername(username);
            tweetAppProducer.posttweet(tweet);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(tweet);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }

    @GetMapping(value = ALL_TWEET)
    public List<Tweet> getAllTweets(@RequestHeader("Authorization") String authorization) {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        return tweetService.getAllTweets();
    }

    @GetMapping(value = TWEET_BY_ID)
    public ResponseEntity<?> getAllTweetsOfUser(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username) {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)){
             List<Tweet> AllUsers_Tweet=tweetService.getAllTweetsByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(AllUsers_Tweet);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }

    }

    @DeleteMapping(value = DELETE_TWEET)
    public ResponseEntity<?> deleteTweetOfUser(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id) throws Exception_Tweet {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            tweetService.deleteTweetById(id, username);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("user is deleted");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }

    @PutMapping(value = LIKE_TWEET)
    public ResponseEntity<?> LikeTweet(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id) {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            tweetService.likeTweetById(id);
            return ResponseEntity.status(HttpStatus.OK).body("like successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }

    @PostMapping(value = COMMENTS)
    public ResponseEntity<?> comments(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id, @RequestBody Comments comments) throws Exception {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            comments.setCommentId(sequenceGeneratorService.getSequenceNumber(SEQ_NAME));
            tweetService.replyTweetById(id, comments, username);
            return ResponseEntity.status(HttpStatus.OK).body("like successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }
}
