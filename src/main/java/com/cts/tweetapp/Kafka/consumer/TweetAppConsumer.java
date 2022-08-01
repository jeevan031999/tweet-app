package com.cts.tweetapp.Kafka.consumer;


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
    TweetAppService tweetAppService;

    @KafkaListener(topics = {"tweet-app"},groupId = "tweet-app-group")
    public void tweetpost(ConsumerRecord<Integer, String> consumerRecord) throws JsonMappingException, JsonProcessingException {
        tweetAppService.savetweet(consumerRecord);
        log.info("ConsumerRecord : {} ", consumerRecord);
    }
    @KafkaListener(topics = {"tweet-app"},groupId = "tweet-app-group-1")
    public void tweetupdate(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        tweetAppService.updatetweet(consumerRecord);
        log.info("ConsumerRecord : {} ", consumerRecord);
    }

}
