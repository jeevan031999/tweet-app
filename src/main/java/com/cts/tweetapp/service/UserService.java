package com.cts.tweetapp.service;

import com.cts.tweetapp.exception.Exception_UserAlreadyExists;
import com.cts.tweetapp.exception.Exception_UserDoesNotExists;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user) throws Exception_UserAlreadyExists,InputMismatchException {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser==null) {
            String mail=user.getEmail();
            String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(mail);
            if(matcher.matches()==true) {
                log.info("user Added");
                return userRepository.save(user);

            }
            else{
                throw  new InputMismatchException("field not be in format");
            }
        }
        throw new Exception_UserAlreadyExists("User already exists");
    }

    public User forgotPassword(String username, String newPassword) throws Exception_UserDoesNotExists {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            existingUser.setPassword(newPassword);
            return userRepository.save(existingUser);
        }
        throw new Exception_UserDoesNotExists("can't change user does not exist");
    }
    public List<User> getAllUsers() throws Exception_UserDoesNotExists {
        List<User> user= userRepository.findAll();
        if(user==null){
            throw new Exception_UserDoesNotExists("No user Available");
        }
        log.info("No user found");
        return user;
    }

    public User getUserByUsername(String username) throws Exception_UserDoesNotExists {
        User user= userRepository.findByUsername(username);
        if(user==null) {
            throw new Exception_UserDoesNotExists("Account not exist");
        }
        log.info("No username found");
        return user;
    }




 //    ignore this method
//
//    public List<User> getUserByPartialName(String username) throws InvalidUsernameException {
//
//        if(userRepository.findByUsernameContaining(username)== null)
//            throw new InvalidUsernameException("Please enter a valid username");
//        log.info(" Getting user by username.."+username);
//        return userRepository.findByUsernameContaining(username);
//    }



}
