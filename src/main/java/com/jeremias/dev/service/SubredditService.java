package com.jeremias.dev.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremias.dev.dtos.SubredditDto;
import com.jeremias.dev.exception.SubredditNotFoundException;
import com.jeremias.dev.models.Subreddit;
import com.jeremias.dev.repository.SubredditRepository;
import com.jeremias.dev.security.AuthService;


import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditService {
	
	 private final SubredditRepository subredditRepository;
	    private final AuthService authService;

	  @Transactional(readOnly = true )
	    public List<SubredditDto> getAll() {
	        return subredditRepository.findAll()
	                .stream()
	                .map(this::mapToDto)
	                .collect(Collectors.toList());
	    }

	    @Transactional
	    public SubredditDto save(SubredditDto subredditDto) {
	        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));
	        subredditDto.setId(subreddit.getId());
	        return subredditDto;
	    }

	    @Transactional(readOnly = true)
	    public SubredditDto getSubreddit(Long id) {
	        Subreddit subreddit = subredditRepository.findById(id)
	                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id -" + id));
	        return mapToDto(subreddit);
	    }

	    private SubredditDto mapToDto(Subreddit subreddit) {
	        return SubredditDto.builder().name(subreddit.getName())
	                .id(subreddit.getId())
	                .numberOfPosts(subreddit.getPosts().size())
	                .build();
	    }

	    private Subreddit mapToSubreddit(SubredditDto subredditDto) {
	        return Subreddit.builder().name("/r/" + subredditDto.getName())
	                .description(subredditDto.getDescription())
	                .user(authService.getCurrentUser())
	                .createdDate(Instant.now()).build();
	    }
}
