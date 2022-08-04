package com.cts.tweetapp.Kafka.producer;


import com.cts.tweetapp.model.Tweet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class TweetAppProducer {
    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    String topics = "tweet-app";

    public void postTweet(Tweet tweet) throws JsonProcessingException {
        int key=Integer.valueOf(tweet.getId());
        String value = objectMapper.writeValueAsString(tweet);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key,value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess( key,value, result);

            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(value, ex);
            }
        });

    }

    protected void handleFailure(String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }

    }

    private void handleSuccess(int key,String value, SendResult<Integer, String> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", value, result.getRecordMetadata().partition());
    }

}


