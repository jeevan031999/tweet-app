package com.cts.tweetapp.service;
import com.cts.tweetapp.Kafka.consumer.TweetAppConsumer;
import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.exception.InvalidUsernameException;
import com.cts.tweetapp.model.Comments;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.model.TweetType;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.CommentsRepository;
import com.cts.tweetapp.repository.TweetRepository;
import com.cts.tweetapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TweetServiceTest {

    @InjectMocks
    private TweetService tweetService;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private UserRepository userRepository;









    @Test
    void testDeleteTweetById() throws Exception_Tweet {
        User u= new User(1,"username","ata","email@gmail.com","hin12","1234",9643125);
        Tweet tweet2 = new Tweet(48, null, "description", "12-12-2022", 3, "username", "Dil");
        Tweet tweet = new Tweet(48, null, "description", "12-12-2022", 3, "username1", "Dil");
        when(userRepository.findByUsername(tweet.getUsername())).thenReturn(u);
        tweetService.deleteTweetById(tweet.getId(),tweet.getUsername());
        verify(tweetRepository).deleteById(tweet2.getId());
    }

    @Test
    void testGetAllTweets() {
        Tweet tweet = new Tweet(1, null, "description", "12-12-2022", 3, "username", "Dil");
        Tweet tweet1 = new Tweet(2, null, "description", "12-12-2022", 3, "username1", "Dil");
        List<Tweet> tweetList = Arrays.asList(tweet, tweet1);
        when(tweetRepository.findAll()).thenReturn(tweetList);
        List<Tweet> list = tweetService.getAllTweets();
        assertEquals(tweetList, list);
    }

    @Test
    void testGetAllTweetsByUsername() {
        Tweet tweet = new Tweet(1, null, "description", "12-12-2022", 3, "username", "Dil");
        Tweet tweet1 = new Tweet(2, null, "description", "12-12-2022", 3, "username", "Dil");
        List<Tweet> tweetList = Arrays.asList(tweet, tweet1);
        when(tweetRepository.findByUsername("username")).thenReturn(tweetList);
        List<Tweet> list = tweetService.getAllTweetsByUsername("username");
        assertEquals(tweetList, list);
    }

    @Test
    void testReplyTweetById() throws Exception_Tweet {
        Comments comments = new Comments(1, "description", "12-12-2022", 1, "username");
        when(commentsRepository.save(comments)).thenReturn(comments);
        Comments c = tweetService.replyTweetById(1, comments, "username");
        assertEquals(comments, c);

    }

    @Test
    void testLikeTweetById() {
        Tweet tweet = new Tweet(1, null, "description", "12-12-2022", 3, "username", "Dil");
        when(tweetRepository.findById(1)).thenReturn(Optional.of(tweet));
        long likes = tweet.getLikes() + 1;
        tweet.setLikes(likes);
        when(tweetRepository.save(tweet)).thenReturn(tweet);
        tweetService.likeTweetById(1);
        assertEquals(5, tweet.getLikes());

    }
}