package com.cts.tweetapp.Kafka.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile("local")
public class TopicConfig {
    @Bean
    public NewTopic tweetApp(){
        return TopicBuilder.name("tweet-app")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
