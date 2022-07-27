package com.cts.tweetapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "tweets")
public class Tweet {

    @Transient
    public static final String SEQUENCE_NAME="tweets_sequence";
    @Id
    private int id;
    private String description;
    private LocalDateTime tweetDate;
    private String loginId;
    private List<String> comments;
    private String title;
    private String tags;
}
