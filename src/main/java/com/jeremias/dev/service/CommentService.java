package com.jeremias.dev.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremias.dev.dtos.CommentsDto;
import com.jeremias.dev.exception.PostNotFoundException;
import com.jeremias.dev.exception.SpringRedditException;
import com.jeremias.dev.mappers.CommentMapper;
import com.jeremias.dev.models.Comment;
import com.jeremias.dev.models.NotificationEmail;
import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repository.CommentRepository;
import com.jeremias.dev.repository.PostRepository;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.security.AuthService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;;
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {
	//TODO: Construct POST URL
    private static final String POST_URL = "";

    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
        //here we get the string html for email sending	
        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }
    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new SpringRedditException("Su comentario contiene un lenguaje que no esta permitido.");
        }
        return true;
    }
}
