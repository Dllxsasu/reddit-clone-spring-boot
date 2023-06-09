package com.jeremias.dev.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.dtos.SubredditDto;
import com.jeremias.dev.service.SubredditService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController {
	 private final SubredditService subredditService;

	    @GetMapping
	    public List<SubredditDto> getAllSubreddits() {
	        return subredditService.getAll();
	    }

	    @GetMapping("/{id}")
	    public SubredditDto getSubreddit(@PathVariable Long id) {
	        return subredditService.getSubreddit(id);
	    }

	    @PostMapping
	    public SubredditDto create(@RequestBody @Valid SubredditDto subredditDto) {
	        return subredditService.save(subredditDto);
	    }
}
