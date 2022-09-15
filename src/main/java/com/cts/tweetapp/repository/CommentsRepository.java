package com.cts.tweetapp.repository;

import com.cts.tweetapp.model.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends MongoRepository<Comments, Integer> {
    List<Comments> findCommentsByTweetId(int tweetId);
}
