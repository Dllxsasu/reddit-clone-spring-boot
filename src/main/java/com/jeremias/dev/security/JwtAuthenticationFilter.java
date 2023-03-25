package com.jeremias.dev.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	

	  private final JwtTokenProvider jwtService;
	  private final UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userMail;
		
		System.out.println("auth "+authHeader);
		
		if( authHeader == null ||!authHeader.startsWith("Bearer "))  {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		
		System.out.println("jwt "+jwt);
		userMail = jwtService.extractUsername(jwt);
		System.out.println("user mail "+userMail);
		
		
		
		if(userMail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			System.out.println("La vida esta aqui ");
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userMail);
		
			if(jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			            userDetails,
			            null,
			            userDetails.getAuthorities()
			        );
				
				authToken.setDetails(
			            new WebAuthenticationDetailsSource().buildDetails(request)
			        );
				  SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		  filterChain.doFilter(request, response);
		
	}

	
	
}
