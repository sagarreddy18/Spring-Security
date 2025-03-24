package com.dailycode.springsecurity.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	private final UserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public WebSecurityConfig(UserDetailsService userDetailsService,JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.csrf(csrf->csrf.disable())   //post put delete requests also works without token
		.authorizeHttpRequests(request -> request
				.requestMatchers("register","/refresh-token","/login").permitAll()
				.anyRequest().authenticated())   //remove this line to override auth process
//		.formLogin(Customizer.withDefaults())  //To show in form
		.httpBasic(Customizer.withDefaults())   // adding basic authentication
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	
	
//	@Bean
//      public UserDetailsService userDetailsService() {
//		UserDetails sagar
//				= User.withUsername("sagar")
//				.password("{noop}password")   //if we won't use password encoder add {noop}
//				.roles("USER")
//				.build();
//		UserDetails tharun
//		= User.withUsername("tharun")
//		.password("{noop}passwords")
//		.roles("USER")
//		.build();
//		return new InMemoryUserDetailsManager(sagar,tharun);
//	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(14);
	}
	@Bean
	public AuthenticationProvider authenticationProvider() {
	   DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	   provider.setUserDetailsService(userDetailsService);
//	   provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
	   provider.setPasswordEncoder(bCryptPasswordEncoder());
	   return provider;
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();	
	}
	
}
