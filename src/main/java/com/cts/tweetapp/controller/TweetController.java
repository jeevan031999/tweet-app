package com.cts.tweetapp.controller;

import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.model.Comments;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.TweetService;
import com.cts.tweetapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SequenceGeneratorService sequenceGeneratorService;

    @PostMapping(value = ADD_TWEET)
    public Tweet addTweet(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @RequestBody Tweet tweet)  {

        tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        return tweetService.postTweetByUsername(username, tweet);
    }

    @PutMapping(value = EDIT_TWEET)
    public Tweet editTweet(@PathVariable("username") String username, @RequestParam("id") int id, @RequestHeader("Authorization") String authorization, @RequestBody Tweet tweet) {
        tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        return tweetService.updateTweet(username,id,tweet);
    }

    @GetMapping(value = ALL_TWEET)
    public List<Tweet> getAllTweets(@RequestHeader("Authorization") String authorization) {
        return tweetService.getAllTweets();
    }

    @GetMapping(value = TWEET_BY_ID)
    public List<Tweet> getAllTweetsOfUser(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username) {
        return tweetService.getAllTweetsByUsername(username);
    }

    @DeleteMapping(value = DELETE_TWEET)
    public void deleteTweetOfUser(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id) throws Exception_Tweet {
        tweetService.deleteTweetById(id, username);
    }

    @PutMapping(value = LIKE_TWEET)
    public void LikeTweet(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id) {
        tweetService.likeTweetById(id);
    }

    @PostMapping(value = COMMENTS)
    public Comments comments(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id, @RequestBody Comments comments) throws Exception {
        comments.setCommentId(sequenceGeneratorService.getSequenceNumber(SEQ_NAME));
        return tweetService.replyTweetById(id, comments, username);
    }
}
