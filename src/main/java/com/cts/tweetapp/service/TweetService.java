package com.cts.tweetapp.service;

import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.TweetRepository;
import com.cts.tweetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    public Tweet postTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    public Tweet updateTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    public Tweet likes(Tweet tweet) {
        tweet.setLikes(tweet.getLikes() + 1);
        return tweetRepository.save(tweet);
    }

    public Tweet commentOnTweet(Tweet tweet, Tweet comment) {
        tweetRepository.save(comment);
        List<Tweet> tweetReplies = tweet.getComments();
        tweetReplies.add(comment);
        tweet.setComments(tweetReplies);
        tweetRepository.save(tweet);
        return comment;
    }

    public void deleteTweet(Tweet tweet) {
        tweetRepository.delete(tweet);
    }

    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    public List<Tweet> getAllTweetsByUsername(String username) {
        return tweetRepository.findByUserUsername(username);
    }

    public Tweet postTweetByUsername(Tweet tweet, String username) {
        User user = userRepository.findByUsername(username);
        tweet.setUser(user);
        return tweetRepository.save(tweet);

    }

    public void deleteTweetById(int tweetId) {
        tweetRepository.deleteById(tweetId);
    }

    public Tweet replyTweetById(Tweet comment, int tweetId) throws Exception {
        Optional<Tweet> parentTweet = tweetRepository.findById(tweetId);
        if (parentTweet.isPresent()) {
            List<Tweet> replies = parentTweet.get().getComments();
            replies.add(comment);
            tweetRepository.save(parentTweet.get());
        }
        else {
            throw new Exception("Incorrect or deleted tweet id.");
        }
        return comment;


    }

    public void likeTweetById(int tweetId){
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if (tweet.isPresent()) {
            tweet.get().setLikes(tweet.get().getLikes()+1);
            tweetRepository.save(tweet.get());
        }

    }


}
