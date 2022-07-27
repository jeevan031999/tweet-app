package com.cts.tweetapp.repository;

import com.cts.tweetapp.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TweetRepository extends MongoRepository<Tweet,Integer> {

}
