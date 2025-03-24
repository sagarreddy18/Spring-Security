package com.dailycode.springsecurity.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dailycode.springsecurity.model.AppUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	private String secretKey = null;

	public String generateToken(AppUser appUser) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts
				.builder()
				.claims()
				.add(claims)
				.subject(appUser.getUsername())
				.issuer("TSR")
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 1000))  // 1 minute (60 seconds)
				.and()
				.signWith(generateKey())
			    .compact();
	}
	
	public String generateRefreshToken(AppUser appUser) {
	    Map<String, Object> claims = new HashMap<>();
	    return Jwts
	            .builder()
	            .claims()
	            .add(claims)
	            .subject(appUser.getUsername())
	            .issuer("TSR")
	            .issuedAt(new Date(System.currentTimeMillis()))
	            .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days expiry
	            .and()
	            .signWith(generateKey())
	            .compact();
	}

	
	
	private SecretKey generateKey() {
		byte[] decode = Decoders.BASE64.decode(getSecretKey()); 
		return Keys.hmacShaKeyFor(decode);
	}


	public String getSecretKey() {
		secretKey = "7b64e2ff13eaa752050221305c770ac4a6500e3e5a50d6db0b4fea932b594621";
		return secretKey;
	}


	public String extractUserName(String token) {
		return extractCliams(token,Claims::getSubject);
	}
 

	private <T> T extractCliams(String token, Function<Claims,T> claimsResolver) {
	Claims claims = extractCliams(token);
	return claimsResolver.apply(claims);
	}


	private Claims extractCliams(String token) {
		return Jwts 
		.parser()
		.verifyWith(generateKey())
		.build()
		.parseSignedClaims(token)
		.getPayload(); 
	}


	public boolean isTokenValid(String jwt, UserDetails userDetails) {
		final String userName = extractUserName(jwt);
		
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
	}
	
	public boolean isRefreshTokenValid(String token, AppUser userDetails) {
	    final String username = extractUserName(token);
	    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}



	private boolean isTokenExpired(String jwt) {
		return extractExpiration(jwt).before(new Date());
	}


	private Date extractExpiration(String jwt) {
		return extractCliams(jwt, Claims::getExpiration);
	}

}