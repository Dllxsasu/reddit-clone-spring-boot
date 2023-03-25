package com.jeremias.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.Comment;
import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.User;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
