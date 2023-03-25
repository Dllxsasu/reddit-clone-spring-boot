package com.jeremias.dev.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthentificationEntryPoint implements AuthenticationEntryPoint {
	///we override a commence from authentifaciton entryPoint
	//this function is call when a request without a jwt token try to get from rresorcese
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		
		// TODO Auto-generated method stub
		
	}

}
