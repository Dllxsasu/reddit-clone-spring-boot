package com.jeremias.dev.exception;

public class SubredditNotFoundException extends RuntimeException {
	public SubredditNotFoundException(String message) {
        super(message);
    }
}
