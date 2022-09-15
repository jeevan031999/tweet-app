package com.cts.tweetapp.controller;

import com.cts.tweetapp.config.Authenticate;
import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.exception.Exception_UserAlreadyExists;
import com.cts.tweetapp.exception.Exception_UserDoesNotExists;
import com.cts.tweetapp.model.Comments;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.model.TweetType;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.TweetService;
import com.cts.tweetapp.service.UserService;
import com.cts.tweetapp.util.JwtRequest;
import com.cts.tweetapp.util.JwtResponse;
import com.cts.tweetapp.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;

import static com.cts.tweetapp.constants.Constants.*;
import static com.cts.tweetapp.model.Comments.SEQ_NAME;
import static com.cts.tweetapp.model.User.SEQUENCE_NAME;


@RestController
@RequestMapping(value = BASE_URL)
@Slf4j
@CrossOrigin(origins = "*")
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

    @Autowired
    private TweetService tweetService;

    @RequestMapping(value = "/health",method = RequestMethod.GET)
    public ResponseEntity<?> health() throws Exception {
        try {
            return ResponseEntity.status(200).body("Ok");
        } catch (Exception e) {
            return (ResponseEntity<?>) ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping(value = REGISTER)
    public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception_UserAlreadyExists, InputMismatchException {
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

    @PostMapping(value = FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@PathVariable("username") String username,
                                                 @PathVariable("password") String forgotPassword) throws Exception_UserDoesNotExists {

        if (username.equals(null)) {
            return new ResponseEntity<>("\"user name not found\"", HttpStatus.BAD_REQUEST);
        }

        User u=userService.forgotPassword(username,forgotPassword);
        return new ResponseEntity<>(u,HttpStatus.OK);

    }

    @GetMapping(value = ALL_USER)
    public ResponseEntity<List<User>> getAllUser(@RequestHeader("Authorization") String authorization) throws Exception_UserDoesNotExists {
        List<User> u= userService.getAllUsers();
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping(value = BY_ID)
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username) throws Exception_UserDoesNotExists {
        if(username!=null) {
            User u = userService.getUserByUsername(username);
           // return new ResponseEntity<>(u, HttpStatus.OK);
            return ResponseEntity.status(HttpStatus.OK).body(u);
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

    @PostMapping(value = ADD_TWEET)
    public ResponseEntity<?> addTweet(@RequestHeader(name="Authorization") String authorization, @PathVariable(name="username") String username,  @RequestBody Tweet tweet) throws JsonProcessingException {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            tweet.setTweetType(TweetType.NEW);
            tweet.setId(sequenceGeneratorService.getSequenceNumber(Tweet.SEQUENCE_NAME));
            tweet.setUsername(uname);
            tweetService.saveMethod(tweet);

            return ResponseEntity.status(HttpStatus.CREATED).body(tweet);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }

    }
    @PutMapping(value = EDIT_TWEET)
    public ResponseEntity<?> updateTweet(@PathVariable("username") String username, @PathVariable("id") int id, @RequestHeader("Authorization") String authorization, @RequestBody Tweet tweet) throws JsonProcessingException {
        String token = authorization.substring(7);
        String uname = jwtUtilToken.getUsernameFromToken(token);
        if (username.equals(uname)) {
            tweet.setTweetType(TweetType.UPDATE);
            tweet.setId(id);
            tweet.setUsername(username);
            tweetService.saveMethod(tweet);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(tweet);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }

    @GetMapping(value = ALL_TWEET)
    public ResponseEntity<List<Tweet>> getAllTweets(@RequestHeader("Authorization") String authorization) {
        List<Tweet> tweet= tweetService.getAllTweets();
        return ResponseEntity.status(HttpStatus.OK).body(tweet);
    }

    @GetMapping(value = TWEET_BY_ID)
    public ResponseEntity<?> getAllTweetsOfUser( @PathVariable("username") String username) {
//        String token =authorization.substring(7);
//        String uname= jwtUtilToken.getUsernameFromToken(token);
//        if(username.equals(uname)){
            List<Tweet> AllUsers_Tweet=tweetService.getAllTweetsByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(AllUsers_Tweet);
//        }
//        else{
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
//        }

    }

    @DeleteMapping(value = DELETE_TWEET)
    public ResponseEntity<?> deleteTweetOfUser(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id) throws Exception_Tweet {
        String token =authorization.substring(7);
        String uname= jwtUtilToken.getUsernameFromToken(token);
        if(username.equals(uname)) {
            tweetService.deleteTweetById(id, username);
            return new ResponseEntity<>( HttpStatus.ACCEPTED );
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID USER");
        }
    }

    @PutMapping(value = LIKE_TWEET)
    public ResponseEntity<?> likeTweet(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id) {
        tweetService.likeTweetById(id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }


    @PostMapping(value = COMMENTS)
    public ResponseEntity<?> comments(@RequestHeader("Authorization") String authorization, @PathVariable("username") String username, @PathVariable("id") int id, @RequestBody Comments comments) throws Exception {
        comments.setCommentId(sequenceGeneratorService.getSequenceNumber(SEQ_NAME));
        tweetService.replyTweetById(id, comments, username);
        return ResponseEntity.status(HttpStatus.OK).body(comments);

    }

    @GetMapping(value=COMMENTSBYID)
    public ResponseEntity<List<Comments>> commentsById(@RequestHeader("Authorization") String authorization,@PathVariable("id") int id){
        List<Comments> comments=tweetService.getCommentsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }


}
