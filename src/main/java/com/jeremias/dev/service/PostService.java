package com.jeremias.dev.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremias.dev.dtos.PostRequest;
import com.jeremias.dev.dtos.PostResponse;
import com.jeremias.dev.exception.PostNotFoundException;
import com.jeremias.dev.exception.SubredditNotFoundException;
import com.jeremias.dev.mappers.PostMapper;
import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.Subreddit;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repository.PostRepository;
import com.jeremias.dev.repository.SubredditRepository;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.security.AuthService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.toList;;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
       
    }
    

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
