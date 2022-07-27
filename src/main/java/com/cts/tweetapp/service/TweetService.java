package com.cts.tweetapp.service;

import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    public Tweet addTweet(Tweet tweet){
        return tweetRepository.save(tweet);
    }
}
