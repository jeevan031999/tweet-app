package com.cts.tweetapp.repository;

import com.cts.tweetapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,Integer> {

//    @Query("select user from users where userId: userId & password: password")
//    public User findByUserIdAndPassword(String userId,String password);
    User findByUsername(String loginId);
    int deleteByUsername(String loginId);

    List<User> findByUsernameContaining(String username);
}
