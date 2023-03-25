package com.jeremias.dev.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.jeremias.dev.models.User;
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTestEmbedded {
	 @Autowired
	    private UserRepository userRepository;
	 
	    @Test
	    public void shouldSaveUser() {
	    	//Given
	        User user = new User(null, "test user", "secret password", "user@email.com", Instant.now(), true);
	        //When
	        User savedUser = userRepository.save(user);
	        //Then
	        assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
	    }
	    
	    @Test
	    //Given
	    @Sql("classpath:test-data.sql")
	    public void shouldSaveUsersThroughSqlFile() {
	        //When
	    	Optional<User> test = userRepository.findByUsername("testuser_sql");
	        //Then
	    	assertThat(test).isNotEmpty();
	    }
}
