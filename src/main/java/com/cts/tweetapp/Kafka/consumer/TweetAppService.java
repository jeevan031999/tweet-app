package com.cts.tweetapp.Kafka.consumer;

import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.repository.TweetRepository;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
@Slf4j
public class TweetAppService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    TweetRepository tweetRepository;

    public void savetweet(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        Tweet tweet=objectMapper.readValue(consumerRecord.value(),Tweet.class);
        log.info("tweet is {}",tweet);
        //tweet.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        tweetRepository.save(tweet);
        log.info("Successfully post the tweet{} ", tweet);
    }

    public void  updatetweet(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        Tweet tweet=objectMapper.readValue(consumerRecord.value(),Tweet.class);
        log.info("tweet is {}",tweet);
        if(tweet.getId()==null) {
            throw new IllegalArgumentException("tweet id id is not valid here");
        }
        Optional<Tweet> tweetoptional=tweetRepository.findById(tweet.getId());
         if(tweetoptional.isEmpty()) {
            throw new IllegalArgumentException("tweet id id is not valid");
        }
         else{
             log.info("Validation is successful for the tweet : {} ", tweetoptional.get());
             tweetRepository.save(tweet);
             log.info("Successfully update the tweet{} ", tweet);
         }

    }
    }

