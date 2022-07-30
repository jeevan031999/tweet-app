package com.cts.tweetapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.logging.Logger;

@SpringBootApplication
@ComponentScan("com.cts")
@Slf4j
public class TweetAppServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(TweetAppServiceApplication.class, args);
		log.info("Starting the tweet app applicationS");
	}

}
