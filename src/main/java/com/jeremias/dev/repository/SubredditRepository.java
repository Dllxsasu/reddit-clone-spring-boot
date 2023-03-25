package com.jeremias.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long>  {
	Optional<Subreddit> findByName(String subredditName);
}
