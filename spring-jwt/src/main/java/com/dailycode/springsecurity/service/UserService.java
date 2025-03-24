package com.dailycode.springsecurity.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dailycode.springsecurity.model.AppUser;
import com.dailycode.springsecurity.repository.UserRepository;

@Service
public class UserService {
private final UserRepository userRepository;
private final BCryptPasswordEncoder bCryptPasswordEncoder; 
private final AuthenticationManager authenticationManager; 
private final JWTService jwtService;
	
	public UserService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,AuthenticationManager authenticationManager, JWTService jwtService) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		
	}

	public AppUser register(AppUser user) {
		AppUser userData = null;
		try {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userData = userRepository.save(user) ;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userData;
	}

	public Map<String, String> verify(AppUser appUser) {
	    Authentication authenticate = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword())
	    );

	    if (authenticate.isAuthenticated()) {
	        String accessToken = jwtService.generateToken(appUser);
	        String refreshToken = jwtService.generateRefreshToken(appUser);

	        Map<String, String> tokens = new HashMap<>();
	        tokens.put("accessToken", accessToken);
	        tokens.put("refreshToken", refreshToken);
	        return tokens;
	    }

	    throw new RuntimeException("Invalid credentials");
	}

}
