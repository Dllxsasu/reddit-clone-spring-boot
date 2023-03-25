package com.jeremias.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	  Optional<User> findByUsername(String username);
	  
}
