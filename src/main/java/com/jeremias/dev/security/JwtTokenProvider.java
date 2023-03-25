package com.jeremias.dev.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtTokenProvider {
	
	@Value("${app.jwt-experation-milliseconds}") 	
	//@Value("${app.jwt-secret}")
	private String jwtSecret="2442264529482B4D6251655468576D5A7134743777217A25432A462D4A614E63";
	//
	@Value("${app.jwt-experation-milliseconds}")
	private int jwtExperationMs = 604800000;
	
	public int getJwtExperationMs() {
		return jwtExperationMs;
	}
	//we 
	public String extractUsername(String token) {
	    return extractClaim(token, Claims::getSubject);
	  }
	 public String generateToken(
			 UserDetails userDetails) {
		    return generateToken(new HashMap<>(), userDetails);
	 }
	

 
 public String generateToken(Map<String, Object> claims,
		 UserDetails userDetails) {
	 Date currentDate = new Date(); //instance of date equal to today date
		Date expireDate = new Date( currentDate.getTime()+jwtExperationMs);
	 return Jwts
			 .builder()
			 .setClaims(claims)
			 .setSubject(userDetails.getUsername())
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
			.signWith(getSignInKey(),SignatureAlgorithm.HS256)
			.compact();
 }
 
	//claim are head data from jwt
 //Here we create a function which contains a token and a function which are from claims, that way we can get 
 //we need like 
	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		    final Claims claims = extractAllClaims(token);
		    return claimsResolver.apply(claims);
		  }

	 
	public Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
				
	}
	public Key getSignInKey() {
		byte[] keyBites = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBites);
	}
	 public boolean isTokenValid(String token, UserDetails userDetails) {
		    final String username = extractUsername(token);
		    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
		  }
	 
	  private boolean isTokenExpired(String token) {
		    return extractExpiration(token).before(new Date());
		  }
	  
	  private Date extractExpiration(String token) {
		    return extractClaim(token, Claims::getExpiration);
		  }
	
}
