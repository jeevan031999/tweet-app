package com.cts.tweetapp.controller;

import com.cts.tweetapp.model.User;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.UserService;
import com.cts.tweetapp.util.JwtRequest;
import com.cts.tweetapp.util.JwtResponse;
import com.cts.tweetapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    //    @GetMapping(value = LOGIN)
//    public String loginUser(@RequestParam String userId, @RequestParam String password) {
//        User u = userService.login(userId, password);
//        return u != null ? "User is present " + u : "User is not available";
//    }
    @GetMapping("/hi")
    public String hello(@RequestHeader("Authorization") String authorization) {
        return "Hello World" + authorization;
    }

    @PostMapping(value = LOGIN)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getLoginId(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getLoginId());
        final String jwttoken = jwtUtilToken.generateToken(userDetails);
        System.out.println("Received request to generate token for " + authenticationRequest);
        return ResponseEntity.ok(new JwtResponse(jwttoken));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
