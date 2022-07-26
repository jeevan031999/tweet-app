package com.cts.tweetapp.controller;

import com.cts.tweetapp.model.User;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.cts.tweetapp.constants.Constants.*;
import static com.cts.tweetapp.model.User.SEQUENCE_NAME;

@RestController
@RequestMapping(value = BASE_URL)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;


    @PostMapping(value = REGISTER)
    public User saveUser(@RequestBody User user){
        user.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        return userService.addUser(user);
    }

    @GetMapping(value=LOGIN)
    public String loginUser(@RequestParam String userId,@RequestParam String password){
        User u=userService.login(userId,password);
        return u!=null?"User is present "+u:"User is not available";
    }
}
