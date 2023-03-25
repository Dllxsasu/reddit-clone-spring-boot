package com.jeremias.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.jeremias.dev.config.SwaggerConfiguration;

//import com.jeremias.dev.config.SwaggerConfiguration;

@SpringBootApplication
@EnableAsync

public class RedditCloneRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditCloneRestApplication.class, args);
	}

}
