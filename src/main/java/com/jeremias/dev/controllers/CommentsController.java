package com.jeremias.dev.controllers;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.dtos.CommentsDto;
import com.jeremias.dev.service.CommentService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
	 private final CommentService commentService;

	    @PostMapping
	    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
	        commentService.createComment(commentsDto);
	        return new ResponseEntity<>(CREATED);
	       
	    }

	    @GetMapping("getforPosts/postId")
	    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable("postId") Long postId) {
	        return status(OK)
	                .body(commentService.getCommentByPost(postId));
	    }

	    @GetMapping("getAllCommentsByUser/postId")
	    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@RequestParam("userName") String userName) {
	        return status(OK).body(commentService.getCommentsByUser(userName));
	    }
}
