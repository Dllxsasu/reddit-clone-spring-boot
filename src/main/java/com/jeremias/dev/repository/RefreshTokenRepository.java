package com.jeremias.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
