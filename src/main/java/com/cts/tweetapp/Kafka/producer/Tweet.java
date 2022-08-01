//package com.cts.tweetapp.Kafka.producer;
//
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.Transient;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@Getter
//@Setter
//@ToString
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public  class Tweet {
//
//    @Transient
//    public static final String SEQUENCE_NAME = "tweets_sequence";
//    @Id
//    private Integer id;
//    private String description;
//    private String tweetDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    private long likes;
//    private String username;
//    private String tags;
//    }
//
//
