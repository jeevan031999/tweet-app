package com.cts.tweetapp.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tweets")
public  class Tweet {
   @Transient
    public static final String SEQUENCE_NAME = "tweets_sequence";
    @Id
    private int id;
    @Transient
    private TweetType tweetType;
    private String description;
    @CreatedDate
    private String tweetDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    private long likes;
    private String username;
    private String tags;
}
