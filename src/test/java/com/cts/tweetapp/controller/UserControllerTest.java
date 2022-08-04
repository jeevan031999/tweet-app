package com.cts.tweetapp.controller;


import com.cts.tweetapp.exception.Exception_UserAlreadyExists;
import com.cts.tweetapp.exception.Exception_UserDoesNotExists;
import com.cts.tweetapp.model.User;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.UserService;
import com.cts.tweetapp.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp(){
        mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
    }


   User user = new User(1,"Shahanshah","Zawal","shahanshah@gmail.com","shah12","1234",965431260);
    User user2 = new User(1,"Shahanshah","Zawal","shahanshah1@gmail.com","shah123","1234",965431260);
    User user3 = new User(1,"Shahanshah","Zawal","shahanshah2@gmail.com","shah1234","1234",965431260);
    @Test
    void testSaveUser() throws Exception {
       // User user = new User(1,"Shahanshah","Zawal","shahanshah@gmail.com","shah12","1234",965431260);
		String json = objectMapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
		mockMvc.perform(post("/api/v1.0/tweets/register").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test
    void testForgotPassword() throws Exception {
        String json = objectMapper.writeValueAsString(user);
        when(userService.forgotPassword(user.getUsername(),"Nik1234")).thenReturn(user);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1.0/tweets/shah12/forgot/Nik1234")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
	public void getUnameTest() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		when(jwtUtil.getUsernameFromToken(token)).thenReturn(token);
		mockMvc.perform(get("/api/v1.0/tweets/getUname").headers(httpHeaders).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

    @Test
    void testGetAllUser() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
        String token = map.get("Authorization").substring(7);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(map);
        String json = objectMapper.writeValueAsString(user);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1.0/tweets/allUser").headers(httpHeaders)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testGetUser() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
        String token = map.get("Authorization").substring(7);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(map);
        String json = objectMapper.writeValueAsString(user);
        when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1.0/tweets/allUser")
                                .headers(httpHeaders)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
