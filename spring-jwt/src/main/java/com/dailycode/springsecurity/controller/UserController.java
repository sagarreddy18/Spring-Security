package com.dailycode.springsecurity.controller;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailycode.springsecurity.model.AppUser;
import com.dailycode.springsecurity.repository.UserRepository;
import com.dailycode.springsecurity.service.JWTService;
import com.dailycode.springsecurity.service.UserService;

@RestController
public class UserController {
	
	private final UserRepository userRepository;
	private final UserService userService;
	private final JWTService jwtService;

	
	public UserController(UserRepository userRepository,UserService userService, JWTService jwtService) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.jwtService = jwtService;
	}
	@PostMapping("/register")
	public AppUser register(@RequestBody AppUser user) {
		return userService.register(user);
		
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody AppUser appUser) {
	    Map<String, String> tokens = userService.verify(appUser);
	    return ResponseEntity.ok(tokens);
	}

	
	@PostMapping("/refresh-token")
	public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
		System.out.println("Hello from refresh-token endpoint");

		System.out.print(request);
	    String refreshToken = request.get("refreshToken");
	    if (refreshToken == null) {
	        return ResponseEntity.badRequest().body(Map.of("error", "Refresh token is required"));
	    }

	    String username = jwtService.extractUserName(refreshToken);
	    AppUser user = userRepository.findByUsername(username);

	    if (jwtService.isRefreshTokenValid(refreshToken, user)) {
	        String newAccessToken = jwtService.generateToken(user);
	        return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "refreshToken", refreshToken));
	    }

	    return ResponseEntity.badRequest().body(Map.of("error", "Invalid refresh token"));
	}

}
