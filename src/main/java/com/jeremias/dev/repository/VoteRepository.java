package com.jeremias.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.User;
import com.jeremias.dev.models.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
