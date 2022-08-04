package com.cts.tweetapp.controller;
import com.cts.tweetapp.Kafka.producer.TweetAppProducer;
import com.cts.tweetapp.exception.Exception_Tweet;
import com.cts.tweetapp.model.Comments;
import com.cts.tweetapp.model.Tweet;
import com.cts.tweetapp.model.TweetType;
import com.cts.tweetapp.service.SequenceGeneratorService;
import com.cts.tweetapp.service.TweetService;
import com.cts.tweetapp.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TweetControllerTest {

	@Autowired
	MockMvc mockMvc;
	ObjectMapper objectMapper=new ObjectMapper();
	@Mock
	TweetAppProducer tweetAppProducer;
	@Mock
	TweetService tweetService;
	@InjectMocks
	private TweetController tweetController;

	@Mock
	private SequenceGeneratorService sequenceGeneratorService;

	@Mock
	private JwtUtil jwtUtil;
	@BeforeEach
	void setUp(){
		mockMvc= MockMvcBuilders.standaloneSetup(tweetController).build();
	}


	Tweet tweet= new Tweet(1,TweetType.NEW,"hello","sdfdvfed",4,"sdfrgre","dfergre");
	@Test
	public void testAddTweet() throws Exception {

		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		when(jwtUtil.getUsernameFromToken(token)).thenReturn(tweet.getUsername());
		String json = objectMapper.writeValueAsString(tweet);
		tweetAppProducer.postTweet(tweet);
		mockMvc.perform(
						MockMvcRequestBuilders.post("/api/v1.0/tweets/sdfrgre/add").headers(httpHeaders)
								.content(json)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isCreated());
	}

	@Test
	void testUpdateTweet() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		when(jwtUtil.getUsernameFromToken(token)).thenReturn(tweet.getUsername());
		String json = objectMapper.writeValueAsString(tweet);
		tweetAppProducer.postTweet(tweet);
		mockMvc.perform(
						MockMvcRequestBuilders.put("/api/v1.0/tweets/sdfrgre/update/1").headers(httpHeaders)
								.content(json)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isAccepted());

	}

	@Test
	void testGetAllTweets() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		String json = objectMapper.writeValueAsString(tweet);
		when(tweetService.getAllTweets()).thenReturn(Arrays.asList(tweet));
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/v1.0/tweets/all").headers(httpHeaders)
								.content(json)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}

	@Test
	void testGetAllTweetsOfUser() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		String json = objectMapper.writeValueAsString(tweet);
		when(jwtUtil.getUsernameFromToken(token)).thenReturn(token);
		when(tweetService.getAllTweetsByUsername(tweet.getUsername())).thenReturn(Arrays.asList(tweet));
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/v1.0/tweets/all").headers(httpHeaders)
								.content(json)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}

	@Test
	void testDeleteTweetOfUser() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		when(jwtUtil.getUsernameFromToken(token)).thenReturn(tweet.getUsername());
		String json = objectMapper.writeValueAsString(tweet);
		doNothing().when(tweetService).deleteTweetById(tweet.getId(),tweet.getUsername());
		mockMvc.perform(
						MockMvcRequestBuilders.delete("/api/v1.0/tweets/sdfrgre/delete/1").headers(httpHeaders))
				.andExpect(status().isAccepted());
	}

	@Test
	void testLikeTweet() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		String json = objectMapper.writeValueAsString(tweet);
		doNothing().when(tweetService).likeTweetById(tweet.getId());
		mockMvc.perform(
						MockMvcRequestBuilders.put("/api/v1.0/tweets/shah12/like/1").headers(httpHeaders)
								.content(json)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());

	}

	@Test
	void testComments() throws Exception {
		Comments comment=new Comments(1,"Hello","31-12-2021",1,"shah12");
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", "Bearer djsdvjhjsnjvhfhj");
		String token = map.get("Authorization").substring(7);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);
		when(jwtUtil.getUsernameFromToken(token)).thenReturn(token);
		String json = objectMapper.writeValueAsString(tweet);
		when(tweetService.replyTweetById(tweet.getId(),comment,comment.getUsername())).thenReturn(comment);
		mockMvc.perform(
						MockMvcRequestBuilders.post("/api/v1.0/tweets/shah12/reply/1").headers(httpHeaders)
								.content(json)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());


	}






}

