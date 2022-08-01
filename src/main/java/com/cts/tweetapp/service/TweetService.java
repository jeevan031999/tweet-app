package com.cts.tweetapp.service;

import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.model.Comments;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.repository.CommentsRepository;
import com.cts.tweetapp.repository.TweetRepository;
import com.cts.tweetapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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

    public boolean isUsernamePresent(String username) {
        //
        return userRepository.findByUsername(username) != null ? true : false;
    }

    public boolean isTweetPresent(int id) {

        return tweetRepository.findById(id) != null ? true : false;
    }

//    public Tweet postTweetByUsername(String username, Tweet tweet)  {
//        if(isUsernamePresent(username)){
//            tweet.setUsername(username);
//            log.info(username);
//            log.info(tweet.toString());
//            return tweetRepository.save(tweet);
//        }
//        else{
//            log.error("username not found error");
//            throw new UsernameNotFoundException("username not found error");
//        }
//
//    }
//
//    public Tweet updateTweet(Tweet tweet) {
//        return tweetRepository.save(tweet);
//    }

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
