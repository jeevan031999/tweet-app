package com.cts.tweetapp.repository;

import com.cts.tweetapp.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<Tweet,Integer> {

    List<Tweet> findByUserUsername(String username);
}
