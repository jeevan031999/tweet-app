package com.cts.tweetapp.service;


import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.exception.InvalidUsernameException;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.model.TweetType;
import com.cts.tweetapp.repository.TweetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(topics = {"tweet-app"},partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
        , "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"
        , "retryListener.startup=false"})


@SpringBootTest
public class TweetServiceKafkaTest {

    // it is used to connect the server b/w producer and consumer . i did add some properties in application.yml.
    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    // it has all the folder of listener consumer
    @Autowired
    KafkaListenerEndpointRegistry endpointRegistry;

    @SpyBean
    private TweetService tweetService;



    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        for (MessageListenerContainer messageListenerContainer : endpointRegistry.getAllListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

//    @Test
//    public void postTweet() throws ExecutionException, InterruptedException, InvalidUsernameException, JsonProcessingException, Exception_Tweet {
//        Integer id = 34;
//        String json = " {\"id\":1,\"tweetType\":\"NEW\",\"description\":\"Kafka Using Spring Boot\",\"tweetDate\":\"2022-2-2\",\"likes\":2,\"username\":\"himanshu12\",\"tags\":\"#abs\"}";
//        kafkaTemplate.sendDefault(id, json).get();
//        //when
//        CountDownLatch latch = new CountDownLatch(1);
//        latch.await(3, TimeUnit.SECONDS);
//
//        verify(tweetAppConsumer, times(1)).tweetPost(isA(ConsumerRecord.class));
//        verify(tweetService, times(1)).proceedTweet(isA(ConsumerRecord.class));
//
//    }

//    @Test
//    public void updateTweet() throws JsonProcessingException, ExecutionException, InterruptedException, InvalidUsernameException {
//        //given
//        Integer id = 34;
//        String json = " {\"id\":1,\"tweetType\":\"NEW\",\"description\":\"Kafka Using Spring Boot\",\"tweetDate\":\"2022-2-2\",\"likes\":2,\"username\":\"himanshu12\",\"tags\":\"#abs\"}";
//        Tweet tweet = objectMapper.readValue(json, Tweet.class);
//        tweetRepository.save(tweet);
//        tweet.setTweetType(TweetType.UPDATE);
//        tweet.setDescription("Kafka Using Spring Boot2.xxxxxxx");
//        String updatejson = objectMapper.writeValueAsString(tweet);
//        kafkaTemplate.sendDefault(id, updatejson).get();
//
//        //when
//        CountDownLatch latch = new CountDownLatch(1);
//        latch.await(3, TimeUnit.SECONDS);
//
//        //then
//        verify(tweetAppConsumer, times(1)).tweetPost(isA(ConsumerRecord.class));
//        verify(tweetService, times(1)).proceedTweet(isA(ConsumerRecord.class));
//        Optional<Tweet> persistedtweet = tweetRepository.findById(tweet.getId());
//        Assertions.assertEquals("Kafka Using Spring Boot2.xxxxxxx", tweet.getDescription());
//
//
//    }
}