package com.cts.tweetapp.controller;

import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cts.tweetapp.constants.Constants.*;
import static com.cts.tweetapp.model.User.SEQUENCE_NAME;

@RestController
@RequestMapping(value = BASE_URL)
public class TweetController {
    @Autowired
    private TweetService tweetService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @PostMapping(value=ADD_TWEET)
    public Tweet addTweet(@RequestBody Tweet tweet){
        tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        return tweetService.addTweet(tweet);
    }
}
