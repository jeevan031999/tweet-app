package com.cts.tweetapp.Kafka.consumer;


import com.cts.tweetapp.exception.InvalidUsernameException;
import com.cts.tweetapp.service.TweetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TweetAppConsumer {

    @Autowired
    TweetService tweetService;

    @KafkaListener(topics = {"tweet-app"},groupId = "tweet-app-group")
    public void tweetpost(ConsumerRecord<Integer, String> consumerRecord) throws JsonMappingException, JsonProcessingException, InvalidUsernameException {
        tweetService.proceedTweet(consumerRecord);
        log.info("ConsumerRecord : {} ", consumerRecord);
    }

}
