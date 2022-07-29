package com.cts.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comments {

    @Transient
    public static final String SEQ_NAME = "comments_sequence";
    @Id
    private int commentId;
    private String description;
    @CreatedDate
    private String commentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    private int tweetId;
    private String username;
}
