package com.jeremias.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	Optional<VerificationToken> findByToken(String token);
}
