package com.cts.tweetapp.Kafka.producer;

import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cts.tweetapp.constants.Constants.*;
import static com.cts.tweetapp.model.Tweet.SEQUENCE_NAME;


@RestController
@RequestMapping(value = BASE_URL)
public class TweetsController {



    @Autowired
    UserService userService;

    @Autowired
    TweetAppProducer tweetAppProducer;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;


    @PostMapping(value = ADD_TWEET)
        public ResponseEntity<Tweet> addTweet(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @RequestBody Tweet tweet) throws JsonProcessingException {

        tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
            tweet.setUsername(username);
             tweetAppProducer.posttweet(tweet);

        return ResponseEntity.status(HttpStatus.CREATED).body(tweet);

    }
    @PutMapping(value = EDIT_TWEET)
    public ResponseEntity<?> updatetweet(@PathVariable("username") String username, @PathVariable("id") int id, @RequestHeader("Authorization") String authorization, @RequestBody Tweet tweet) throws JsonProcessingException {
        if(tweet.getId()==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("please pass the libraryEventId");
        }
         tweetAppProducer.updateTweet(tweet);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tweet);
    }





}
