package com.jeremias.dev.service;


import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremias.dev.dtos.VoteDto;
import com.jeremias.dev.enums.VoteType;
import com.jeremias.dev.exception.PostNotFoundException;
import com.jeremias.dev.exception.SpringRedditException;
import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.Vote;
import com.jeremias.dev.repository.PostRepository;
import com.jeremias.dev.repository.VoteRepository;
import com.jeremias.dev.security.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
	 private final VoteRepository voteRepository;
	    private final PostRepository postRepository;
	    private final AuthService authService;

	    @Transactional
	    public void vote(VoteDto voteDto) {
	        Post post = postRepository.findById(voteDto.getPostId())
	                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
	        
	        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
	        //cHECK if is alredy register 
	        if (voteByPostAndUser.isPresent() &&
	                voteByPostAndUser.get().getVoteType()
	                        .equals(voteDto.getVoteType())) {
	            throw new SpringRedditException("You have already "
	                    + voteDto.getVoteType() + "'d for this post");
	        }
	        //increse or decrease
	        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
	            post.setVoteCount(post.getVoteCount() + 1);
	        } else {
	            post.setVoteCount(post.getVoteCount() - 1);
	        }
	        //save new Vote
	        voteRepository.save(mapToVote(voteDto, post));
	        //save post, counter
	        postRepository.save(post);
	    }

	    private Vote mapToVote(VoteDto voteDto, Post post) {
	        return Vote.builder()
	                .voteType(voteDto.getVoteType())
	                .post(post)
	                .user(authService.getCurrentUser())
	                .build();
	    }
}
