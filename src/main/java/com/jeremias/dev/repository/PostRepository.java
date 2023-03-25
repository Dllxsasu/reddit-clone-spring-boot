package com.jeremias.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.Subreddit;
import com.jeremias.dev.models.User;

public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
