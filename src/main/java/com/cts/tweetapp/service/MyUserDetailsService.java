package com.cts.tweetapp.service;

import com.cts.tweetapp.model.User;
import com.cts.tweetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

	  @Autowired
	  private UserRepository userRepository;

	  @Override
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User u=userRepository.findByUsername(username);
	        System.out.println(u);
	        if(u==null)
	            throw new UsernameNotFoundException("user not found!!");

	        return new MyUserDetails(u);
	  }

}
