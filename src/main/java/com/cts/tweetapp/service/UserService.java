package com.cts.tweetapp.service;

import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user){
        return userRepository.save(user);
    }

    public User login(String userId, String password) {

        User us=userRepository.findByUserIdAndPassword(userId,password);
        return us!=null?us:null;

    }
}
