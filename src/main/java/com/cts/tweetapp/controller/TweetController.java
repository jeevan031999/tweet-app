package com.cts.tweetapp.controller;

import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cts.tweetapp.constants.Constants.*;
import static com.cts.tweetapp.model.Tweet.SEQUENCE_NAME;

@RestController
@RequestMapping(value = BASE_URL)
public class TweetController {
    @Autowired
    private TweetService tweetService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @PostMapping(value = ADD_TWEET)
    public Tweet addTweet(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @RequestBody Tweet tweet) {
        tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        return tweetService.postTweet(tweet);
    }

    @PutMapping(value = EDIT_TWEET)
    public Tweet editTweet(@RequestParam("username") String username, @RequestParam("id") int id, @RequestHeader("Authorization") String authorization, @RequestBody Tweet tweet) {
        tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        return tweetService.updateTweet(tweet);
    }

    @GetMapping(value = ALL_TWEET)
    public List<Tweet> getAllTweets(@RequestHeader("Authorization") String authorization) {
        return tweetService.getAllTweets();
    }

    @GetMapping(value = TWEET_BY_ID)
    public List<Tweet> getAllTweetsOfUser(@RequestParam("username") String username) {
        return tweetService.getAllTweetsByUsername(username);
    }

    @DeleteMapping(value = DELETE_TWEET)
    public void deleteTweetOfUser(@PathVariable("username") String username, @PathVariable("id") int id) {
        tweetService.deleteTweetById(id);
    }

    @PutMapping(value = LIKE_TWEET)
    public void LikeTweet(@PathVariable("username") String username, @PathVariable("id") int id) {
        tweetService.likeTweetById(id);
    }

    @PostMapping(value = COMMENTS)
    public Tweet comments(@PathVariable("username") String username, @PathVariable("id") int id, @RequestBody Tweet tweet)
            throws Exception {
        return tweetService.replyTweetById(tweet, id);
    }


}
