package com.cts.tweetapp.controller;

import com.cts.tweetapp.config.Authenticate;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.UserService;
import com.cts.tweetapp.util.JwtRequest;
import com.cts.tweetapp.util.JwtResponse;
import com.cts.tweetapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cts.tweetapp.constants.Constants.*;
import static com.cts.tweetapp.model.User.SEQUENCE_NAME;

@RestController
@RequestMapping(value = BASE_URL)
public class UserController {

    @Autowired
    private Authenticate authenticate;
    @Autowired
    private UserService userService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtilToken;
    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping(value = REGISTER)
    public User saveUser(@RequestBody User user) {
        user.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        return userService.addUser(user);
    }

    @GetMapping("/hi")
    public String hello(@RequestHeader("Authorization") String authorization) {
        return "Hello World" + authorization;
    }

    @PostMapping(value = LOGIN)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwttoken = jwtUtilToken.generateToken(userDetails);
        System.out.println("Received request to generate token for " + authenticationRequest);
        return ResponseEntity.ok(new JwtResponse(jwttoken));
    }

    @GetMapping(value = ALL_USER)
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping(value = BY_ID)
    public User getUser(@RequestHeader("Authorization") String authorization, @RequestParam("username") String username) {
        return userService.getUserByUsername(username);
    }

//    public String forgotPassword(@RequestParam("loginId") String loginId,@RequestParam("password") String password,@RequestParam("confirmPassword") String confirmPassword){
//        userService.forgotPassword(loginId,password,confirmPassword);
//    }

}
