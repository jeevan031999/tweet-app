package com.cts.tweetapp.controller;

import com.cts.tweetapp.config.Authenticate;
import com.cts.tweetapp.exception.Exception_UserAlreadyExists;
import com.cts.tweetapp.exception.Exception_UserDoesNotExists;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.UserService;
import com.cts.tweetapp.util.JwtRequest;
import com.cts.tweetapp.util.JwtResponse;
import com.cts.tweetapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@Slf4j
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
    public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception_UserAlreadyExists {
        user.setId(sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME));
        User u=userService.addUser(user);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @PostMapping(value = LOGIN)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwtToken = jwtUtilToken.generateToken(userDetails);
        System.out.println("Received request to generate token for " + authenticationRequest);
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    @GetMapping(value = ALL_USER)
    public ResponseEntity<List<User>> getAllUser(@RequestHeader("Authorization") String authorization) throws Exception_UserDoesNotExists {
        List<User> u= userService.getAllUsers();
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping(value = BY_ID)
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username) throws Exception_UserDoesNotExists {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            User u = userService.getUserByUsername(username);
            return new ResponseEntity<>(u, HttpStatus.OK);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }
    @GetMapping(value = GET_UNAME)
    public String getUname(@RequestHeader("Authorization") String authorization) {
        String token =authorization.substring(7);
        return jwtUtilToken.getUsernameFromToken(token);
    }




  // Ignore this method////////

 /*   @GetMapping(value = COMMON_USERNAME)
    public ResponseEntity<?> getPartialSameNameUsers(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username) throws InvalidUsernameException {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            List<User> users = userService.getUserByPartialName(username);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }
    */



}
