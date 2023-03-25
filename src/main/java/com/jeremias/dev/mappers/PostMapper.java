package com.jeremias.dev.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jeremias.dev.dtos.PostRequest;
import com.jeremias.dev.dtos.PostResponse;
import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.Subreddit;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repository.CommentRepository;
import com.jeremias.dev.repository.VoteRepository;
import com.jeremias.dev.security.AuthService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
	@Autowired
    private CommentRepository commentRepository;

    
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
	 public abstract  Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    public abstract PostResponse mapToDto(Post post);
    
    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

     String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
