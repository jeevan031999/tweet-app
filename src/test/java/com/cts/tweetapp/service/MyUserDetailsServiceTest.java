package com.cts.tweetapp.service;

import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MyUserDetails myUserDetails;

    @Test
    void testLoadUserByUsername() {
        User u = new User(1, "Nik12", "123", "firstName", "lastName", "email", 12345);
        when(userRepository.findByUsername("Nik12")).thenReturn(u);
        UserDetails us = myUserDetailsService.loadUserByUsername("Nik12");
        assertNotNull(us);
    }
}