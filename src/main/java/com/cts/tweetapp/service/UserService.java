package com.cts.tweetapp.service;

import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user){
        return userRepository.save(user);
    }

//    public User login(String userId, String password) {
//
//        User us=userRepository.findByUserIdAndPassword(userId,password);
//        return us!=null?us:null;
//
//    }

    public User updateUser(User user)
    {
        return userRepository.save(user);
    }

    public int deleteUser(String loginId){
        userRepository.deleteByLoginId(loginId);
        return 1;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById(String id){
        return userRepository.findByLoginId(id);
    }

    public List<User> getMultipleUser(String loginId){
        return userRepository.findByLoginIdContaining(loginId);
    }


    public void forgotPassword(String loginId, String password, String confirmPassword) {
        User u=userRepository.findByLoginId(loginId);
        if(u!=null){
            if(password==confirmPassword){
                u.setPassword(password);
                userRepository.save(u);
            }
        }

    }
}
