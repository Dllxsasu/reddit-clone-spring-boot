package com.jeremias.dev.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremias.dev.exception.SpringRedditException;
import com.jeremias.dev.models.RefreshToken;
import com.jeremias.dev.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        

        return this.refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
