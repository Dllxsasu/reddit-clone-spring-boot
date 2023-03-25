package com.jeremias.dev.security;

import static com.jeremias.dev.util.Constants.ACTIVATION_EMAIL;
import static java.time.Instant.now;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.AuthenticationResponse;
import com.jeremias.dev.dtos.LoginRequest;
import com.jeremias.dev.dtos.RefreshTokenRequest;
import com.jeremias.dev.dtos.RegisterRequest;
import com.jeremias.dev.exception.SpringRedditException;
import com.jeremias.dev.models.NotificationEmail;
import com.jeremias.dev.models.User;
import com.jeremias.dev.models.VerificationToken;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.repository.VerificationTokenRepository;
import com.jeremias.dev.service.MailContentBuilder;
import com.jeremias.dev.service.MailService;
import com.jeremias.dev.service.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
	
	 	private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final VerificationTokenRepository verificationTokenRepository;
	    private final MailContentBuilder mailContentBuilder;
	    private final MailService mailService;
	    private final AuthenticationManager authenticationManager;
	    private final JwtTokenProvider jwtProvider;
	    private final RefreshTokenService refreshTokenService;
	    @Transactional
	    public void signup(RegisterRequest registerRequest) {
	        User user = new User();
	        user.setUsername(registerRequest.getUsername());
	        user.setEmail(registerRequest.getEmail());
	        user.setPassword(encodePassword(registerRequest.getPassword()));
	        user.setCreated(now());
	        user.setEnabled(false);

	        userRepository.save(user);
	        
	        String token = generateVerificationToken(user);
	        String message = mailContentBuilder.build("Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
	                + ACTIVATION_EMAIL + "/" + token);

	        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
	    
	    }
	    public AuthenticationResponse login(LoginRequest loginRequest) {
	    	 
	  		 
	  		/*    var user = repository.findByEmail(request.getEmail())
	  		        .orElseThrow();
	  		    var jwtToken = jwtService.generateToken(user);
	  		    return AuthenticationResponse.builder()
	  		        .token(jwtToken)
	  		        .build();
	    	
	    	*/
	    	  Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
	                loginRequest.getPassword()));
	        
	    	
	        SecurityContextHolder.getContext().setAuthentication(authenticate);
			User user = userRepository.findByUsername(loginRequest.getUsername())
					.orElseThrow();
	        String token = jwtProvider.generateToken(user);
	        
	        return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExperationMs()))
	                .username(loginRequest.getUsername())
	                .build();
	    }
	    @Transactional( )
	    public User getCurrentUser() {
	        User principal = (User) SecurityContextHolder.
	                getContext().getAuthentication().getPrincipal();
	        
	        return userRepository.findByUsername(principal.getUsername())
	                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	    }	
	    
	    private String generateVerificationToken(User user) {
	        String token = UUID.randomUUID().toString();
	        VerificationToken verificationToken = new VerificationToken();
	        verificationToken.setToken(token);
	        verificationToken.setUser(user);
	        verificationTokenRepository.save(verificationToken);
	        return token;
	    }


	    private String encodePassword(String password) {
	        return passwordEncoder.encode(password);
	    }
	    
	    public void verifyAccount(String token) {
	        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
	        verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
	        fetchUserAndEnable(verificationTokenOptional.get());
	    }

	    @Transactional
	    private void fetchUserAndEnable(VerificationToken verificationToken) {
	        String username = verificationToken.getUser().getUsername();
	        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User Not Found with id - " + username));
	        user.setEnabled(true);
	        userRepository.save(user);
	    }
	    
	    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
	        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
	        
	    	User user = userRepository.findByUsername(refreshTokenRequest.getUsername())
					.orElseThrow();
	        String token = jwtProvider.generateToken(user);

	        return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenRequest.getRefreshToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExperationMs()))
	                .username(refreshTokenRequest.getUsername())
	                .build();
	    }
}
