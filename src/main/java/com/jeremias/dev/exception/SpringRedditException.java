package com.jeremias.dev.exception;

public class SpringRedditException extends RuntimeException{
	///this extends in this case we use a contructura por passing the mmessaje we need to show and the exception
	 public SpringRedditException(String exMessage, Exception exception) {
	        super(exMessage, exception);
	    }

	    public SpringRedditException(String exMessage) {
	        super(exMessage);
	    }
}
