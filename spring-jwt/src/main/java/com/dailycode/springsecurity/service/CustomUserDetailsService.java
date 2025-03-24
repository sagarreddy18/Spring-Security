package com.dailycode.springsecurity.service;

import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.dailycode.springsecurity.CustomUserDetails;
import com.dailycode.springsecurity.model.AppUser;
import com.dailycode.springsecurity.repository.UserRepository;
@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	 
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user= userRepository.findByUsername(username);
		if(Objects.isNull(user)) {
			throw new UsernameNotFoundException("User not found");
		}
		return new CustomUserDetails(user);
	}

}
