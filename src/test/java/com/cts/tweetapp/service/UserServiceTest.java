package com.cts.tweetapp.service;



import com.cts.tweetapp.exception.Exception_UserDoesNotExists;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @Mock
    private UsernameNotFoundException usernameNotFoundException;



    User u = new User(1, "Nik12", "123", "firstName", "lastName", "email1@gmail.com", 12345);
    User us = new User(2, "Nik123", "123", "firstName", "lastName", "email2@gmail.com", 12345);



    @Test
    void testAddUser() throws Exception{

        when(userRepository.save(u)).thenReturn(u);
        User newUser=userService.addUser(u);
        Assertions.assertNotNull(u);
    }

    @Test
    void testForgotPassword() throws Exception_UserDoesNotExists{
        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
        u.setPassword("Nikhil12");
        when(userRepository.save(u)).thenReturn(u);

        User u1=userService.forgotPassword(u.getUsername(),u.getPassword());

        assertEquals(u1,u);
    }

    @Test
    void testGetAllUsers() throws Exception_UserDoesNotExists {
        List<User> users = new ArrayList<>();
        users.add(u);
        users.add(us);
        when(userRepository.findAll()).thenReturn(users);
        List<User> userList = userService.getAllUsers();
        assertEquals(users, userList);
    }

    @Test
    void testGetUserByUsername() throws Exception_UserDoesNotExists {

        User u = new User(1, "Nik12", "123", "firstName", "lastName", "email", 12345);
        when(userRepository.findByUsername("Nik12")).thenReturn(u);
        User user = userService.getUserByUsername("Nik12");
        assertEquals("Nik12", user.getUsername());

    }

}
