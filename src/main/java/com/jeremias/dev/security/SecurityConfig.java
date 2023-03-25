package com.jeremias.dev.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jeremias.dev.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private  final JwtAuthenticationFilter jwtAuthFilter;

	private static final String[] AUTH_WHITE_LIST = {
			"/authenticate",
	        "/swagger-resources/**",
	        "/swagger-ui/**",
	        
	        "/v3/api-docs/**",
	        "/webjars/**"
    };
	@Bean
	  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http.cors().and().csrf().disable()
         .authorizeRequests()

         .requestMatchers("/api/auth/**")
         .permitAll()
         .requestMatchers(HttpMethod.GET, "/api/subreddit")
         .permitAll()
         .requestMatchers(HttpMethod.GET, "/api/posts/")
         .permitAll()
         .requestMatchers(HttpMethod.GET, "/api/posts/**")
         .permitAll()
  
         .requestMatchers(AUTH_WHITE_LIST).permitAll()
         //.requestMatchers("/**").permitAll()
         .anyRequest()
         .authenticated();
		 
         http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
         
		 /*http.cors().and()
	        .csrf()
	        .disable()
	        .authorizeHttpRequests()
	        .requestMatchers("/api/auth/**")
	        .permitAll()
	        .requestMatchers("/product/**")
	        .permitAll()
	        .anyRequest()
	        .authenticated()
	        .and()
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	       */
	       // .and()
	     //   .authenticationProvider(authenticationProvider)
	        
	       // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	  }
	 
	 @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	  }
	 
}
